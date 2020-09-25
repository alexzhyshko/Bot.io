package application.routing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	private HashMap<Integer, List<Route>> routes;
	private HashMap<Integer, List<Route>> callbackRoutes;

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
		List<Route> commandRoutes = callbackRoutes.get(caseNum);
		Route result = null;
		try {
			result = commandRoutes.stream().filter(route -> route.getCommand().equals(command)).findFirst()
					.orElseThrow(() -> new NullPointerException(
							"No callback route found for " + caseNum + " case and '" + command + "' command"));
		} catch (NullPointerException npe) {
			result = commandRoutes.stream().filter(route -> route.getCommand().equals("*")).findFirst().orElseThrow(
					() -> new NullPointerException("No callback route found for " + caseNum + " case command"));
		}
		return result;
	}

	private Route getRouteByCaseAndMessage(int caseNum, String message) {
		List<Route> regularRoutes = routes.get(caseNum);
		Route result = null;
		try {
			result = regularRoutes.stream().filter(route -> route.getCommand().equals(message)).findFirst()
					.orElseThrow(() -> new NullPointerException(
							"No route found for " + caseNum + " case and '" + message + "' message"));
		} catch (NullPointerException npe) {
			result = regularRoutes.stream().filter(route -> route.getCommand().equals("*")).findFirst()
					.orElseThrow(() -> new NullPointerException("No route found for " + caseNum + " case command"));
		}
		return result;
	}

	public Router add(int caseNumber, String methodName, Class className, String message) {
		List<Route> caseRoutes = this.routes.get(caseNumber);
		Route routeToAdd = new Route(caseNumber, methodName, className, message);
		if(caseRoutes==null) {
			caseRoutes = new ArrayList<>();
			this.routes.put(caseNumber, caseRoutes);
		}
		if(caseRoutes.contains(routeToAdd)) {
			throw new DuplicateRouteException("Route for caseNumber=" + caseNumber + " and '" + message
					+ "' message already exists in context");
		}
		this.routes.get(caseNumber).add(routeToAdd);
		return this;
	}

	public Router addCallback(int caseNumber, String methodName, Class className, String command) {
		List<Route> commandRoutes = this.callbackRoutes.get(caseNumber);
		Route routeToAdd = new Route(caseNumber, methodName, className, command);
		if(commandRoutes==null) {
			commandRoutes = new ArrayList<>();
			this.callbackRoutes.put(caseNumber, commandRoutes);
		}
		if(commandRoutes.contains(routeToAdd)) {
			throw new DuplicateRouteException("Callback oute for caseNumber=" + caseNumber + " and '" + command
					+ "' command already exists in context");
		}
		this.callbackRoutes.get(caseNumber).add(routeToAdd);
		return this;
	}

	protected void routeToClass(int userid, Class routeClass) {
		int caseNumber = -1;
		for (List<Route> routes : this.routes.values()) {
			for(Route route : routes) {
				if (route.getRouteClass() == routeClass) {
					caseNumber = route.getCase();
					break;
				}
			}
		}
		ApplicationContext.setUserState(userid, caseNumber);
	}

	protected void routeCallbackToClass(int userid, Class routeClass) {
		int caseNumber = -1;
		for (List<Route> routes : this.callbackRoutes.values()) {
			for(Route route : routes) {
				if (route.getRouteClass() == routeClass) {
					caseNumber = route.getCase();
					break;
				}
			}
		}
		ApplicationContext.setUserState(userid, caseNumber);
	}

}
