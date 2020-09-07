package application.routing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.telegram.telegrambots.meta.api.objects.Update;

import application.context.ApplicationContext;
import application.context.annotation.Component;


@Component
public class Router {

	private HashMap<Integer, Route> routes;

	public Router() {
		this.routes = new HashMap<>();
	}

//	public static Router getInstance() {
//		return instance == null ? (instance = new Router()) : instance;
//	}

	public void route(Update update, int caseNumber) {
		try {
			Route destinationRoute = getRouteyCase(caseNumber);
			Class destinationClass = destinationRoute.getRouteClass();
			Method destinationMethod = destinationClass.getMethod(destinationRoute.getMethodName(), Update.class);
			destinationMethod.invoke(ApplicationContext.getComponent(destinationClass), update);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	private Route getRouteyCase(int caseNum) {
		return routes.get(caseNum);
	}

	public Router add(int caseNumber, String methodName, Class className) {
		this.routes.put(caseNumber, new Route(caseNumber, methodName, className));
		return this;
	}

}
