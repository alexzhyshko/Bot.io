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

	private HashMap<Integer, Route> routes;
	private HashMap<Integer, Route> callbackRoutes;

	public Router() {
		this.routes = new HashMap<>();
		this.callbackRoutes = new HashMap<>();
	}

	public void route(Update update, int caseNumber) {
		try {
			Route destinationRoute = getRouteByCase(caseNumber);
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

	public void routeCallback(Update update, int caseNumber) {
		try {
			Route destinationRoute = getCallbackRouteByCase(caseNumber);
			if (destinationRoute == null) {
				throw new RouteNotFoundException(
						"Callback route for caseNumber=" + caseNumber + " not found in context");
			}
			Class destinationClass = destinationRoute.getRouteClass();
			Method destinationMethod = destinationClass.getMethod(destinationRoute.getMethodName(), Update.class);
			destinationMethod.invoke(ApplicationContext.getComponent(destinationClass), update);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	private Route getCallbackRouteByCase(int caseNum) {
		return callbackRoutes.get(caseNum);
	}

	private Route getRouteByCase(int caseNum) {
		return routes.get(caseNum);
	}

	public Router add(int caseNumber, String methodName, Class className) {
		if (this.routes.containsKey(caseNumber)) {
			Route existed = this.routes.get(caseNumber);
			throw new DuplicateRouteException("Route for caseNumber=" + caseNumber + " already exists in context. See "
					+ existed.getClass() + "#" + existed.getMethodName());
		}
		this.routes.put(caseNumber, new Route(caseNumber, methodName, className));
		return this;
	}

	public Router addCallback(int caseNumber, String methodName, Class className) {
		if (this.callbackRoutes.containsKey(caseNumber)) {
			Route existed = this.callbackRoutes.get(caseNumber);
			throw new DuplicateRouteException("Callback route for caseNumber=" + caseNumber
					+ " already exists in context. See " + existed.getClass() + "#" + existed.getMethodName());
		}
		this.callbackRoutes.put(caseNumber, new Route(caseNumber, methodName, className));
		return this;
	}

	protected void routeToClass(int userid, Class routeClass) {
		int caseNumber = -1;
		for(Route route : this.routes.values()) {
			if(route.getRouteClass() == routeClass) {
				caseNumber = route.getCase();
				break;
			}
		}
		ApplicationContext.setUserState(userid, caseNumber);
	}

	protected void routeCallbackToClass(int userid, Class routeClass) {
		int caseNumber = -1;
		for(Route route : this.callbackRoutes.values()) {
			if(route.getRouteClass() == routeClass) {
				caseNumber = route.getCase();
				break;
			}
		}
		ApplicationContext.setUserState(userid, caseNumber);
	}

}
