package application.context.reader;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import application.adapters.UserServiceAdapter;
import application.context.ApplicationContext;
import application.context.annotation.Async;
import application.context.annotation.Component;
import application.context.annotation.Filter;
import application.context.annotation.Prototype;
import application.context.annotation.State;
import application.context.async.AsyncContext;
import application.context.filter.FilterContext;
import application.context.states.StateContext;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class AnnotationReader {

	private AnnotationReader() {
	}

	@SuppressWarnings("unchecked")
	public static void process(Map<String, String> files) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (Entry<String, String> entry : files.entrySet()) {
			Class currentClass = Class.forName(entry.getValue());
			if (currentClass.isAnnotationPresent(Component.class)) {
				if (Arrays.asList(currentClass.getGenericInterfaces()).contains(UserServiceAdapter.class))
					ApplicationContext.setUserService(currentClass);
				if (currentClass.isAnnotationPresent(Async.class))
					AsyncContext.addAsync(currentClass);
				if (currentClass.isAnnotationPresent(Prototype.class))
					ApplicationContext.putIntoPrototypeContext(getInstanceOfClass(currentClass));
				else
					ApplicationContext.putIntoSingletonContext(getInstanceOfClass(currentClass));
				if (currentClass.isAnnotationPresent(State.class)) 
					StateContext.add(currentClass);
				if (currentClass.isAnnotationPresent(Filter.class)) 
					FilterContext.addFilter(currentClass);
			}

		}
		System.out.printf("[INFO] %s Annotation reading finished%n", LocalDateTime.now().toString());
	}

	
	private static Object getInstanceOfClass(Class clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getDeclaredConstructor().newInstance();
	}

}
