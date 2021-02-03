package application.routing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.telegram.telegrambots.meta.api.objects.Update;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.exception.DuplicateRouteException;
import application.exception.RouteNotFoundException;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
public class Router {

	private HashMap<Integer, HashMap<String, Route>> routes;
	private HashMap<Integer, HashMap<String, Route>> callbackRoutes;

	public Router() {
		this.routes = new HashMap<>();
		this.callbackRoutes = new HashMap<>();
	}

	public void route(Update update, int caseNumber, String message) {
		try {
			Route destinationRoute = getRouteByCaseAndMessage(caseNumber, message);
			if (destinationRoute == null) {
				throw new RouteNotFoundException("Route for caseNumber=" + caseNumber + " not found in context");
			}
			Class destinationClass = destinationRoute.getRouteClass();
			Method destinationMethod = destinationClass.getMethod(destinationRoute.getMethodName(), Update.class);
			destinationMethod.invoke(ApplicationContext.getComponent(destinationClass), update);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void routeCallback(Update update, int caseNumber, String command) {
		try {
			Route destinationRoute = getCallbackRouteByCaseAndCommand(caseNumber, command);
			Class destinationClass = destinationRoute.getRouteClass();
			Method destinationMethod = destinationClass.getMethod(destinationRoute.getMethodName(), Update.class);
			destinationMethod.invoke(ApplicationContext.getComponent(destinationClass), update);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	private Route getCallbackRouteByCaseAndCommand(int caseNum, String command) {
		HashMap<String, Route> commandRoutes = callbackRoutes.get(caseNum);
		if (commandRoutes == null) {
			throw new NullPointerException("No case class found for " + caseNum + " case");
		}
		Route result = null;
		try {
			result = commandRoutes.computeIfAbsent(command, a -> {
				throw new NullPointerException(
						"No callback route found for " + caseNum + " case and '" + command + "' command");
			});
		} catch (NullPointerException npe) {
			result = commandRoutes.computeIfAbsent("*", a -> {
				throw new NullPointerException("No callback route found for " + caseNum + " case command");
			});
		}
		return result;
	}

	private Route getRouteByCaseAndMessage(int caseNum, String message) {
		HashMap<String, Route> regularRoutes = this.routes.get(caseNum);
		if (regularRoutes == null) {
			throw new NullPointerException("No case class found for " + caseNum + " case");
		}
		Route result = null;
		try {
			result = regularRoutes.computeIfAbsent(message, a -> {
				throw new NullPointerException("No route found for " + caseNum + " case and '" + message + "' message");
			});
		} catch (NullPointerException npe) {
			result = regularRoutes.computeIfAbsent("*", a -> {
				throw new NullPointerException("No route found for " + caseNum + " case message");
			});
		}
		return result;
	}

	public Router add(int caseNumber, String methodName, Class className, String message) {
		HashMap<String, Route> caseRoutes = this.routes.get(caseNumber);
		Route routeToAdd = new Route(caseNumber, methodName, className, message);
		if (caseRoutes == null) {
			caseRoutes = new HashMap<>();
			this.routes.put(caseNumber, caseRoutes);
		}
		if (caseRoutes.containsKey(message)) {
			throw new DuplicateRouteException(
					"Route for caseNumber=" + caseNumber + " and '" + message + "' message already exists in context");
		}
		this.routes.get(caseNumber).put(message, routeToAdd);
		return this;
	}

	public Router addCallback(int caseNumber, String methodName, Class className, String command) {
		HashMap<String, Route> commandRoutes = this.callbackRoutes.get(caseNumber);
		Route routeToAdd = new Route(caseNumber, methodName, className, command);
		if (commandRoutes == null) {
			commandRoutes = new HashMap<>();
			this.callbackRoutes.put(caseNumber, commandRoutes);
		}
		if (commandRoutes.containsKey(command)) {
			throw new DuplicateRouteException("Callback oute for caseNumber=" + caseNumber + " and '" + command
					+ "' command already exists in context");
		}
		this.callbackRoutes.get(caseNumber).put(command, routeToAdd);
		return this;
	}

	protected void routeToClass(int userid, Class routeClass) {
		int caseNumber = -1;
		for (HashMap<String, Route> routes : this.routes.values()) {
			for (Route route : routes.values()) {
				if (route.getRouteClass() == routeClass) {
					caseNumber = route.getCase();
					break;
				}
			}
		}
		if (caseNumber == -1) {
			throw new NullPointerException("No route found for " + routeClass);
		}
		ApplicationContext.setUserState(userid, caseNumber);
	}

	protected void routeCallbackToClass(int userid, Class routeClass) {
		int caseNumber = -1;
		for (HashMap<String, Route> routes : this.callbackRoutes.values()) {
			for (Route route : routes.values()) {
				if (route.getRouteClass() == routeClass) {
					caseNumber = route.getCase();
					break;
				}
			}
		}
		if (caseNumber == -1) {
			throw new NullPointerException("No callback route found for " + routeClass);
		}
		ApplicationContext.setUserState(userid, caseNumber);
	}

}
