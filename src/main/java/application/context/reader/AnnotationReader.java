package application.context.reader;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import application.context.ApplicationContext;
import application.context.annotation.Async;
import application.context.annotation.Case;
import application.context.annotation.Component;
import application.context.annotation.Configuration;
import application.context.annotation.Filter;
import application.context.annotation.Prototype;
import application.context.annotation.UserServiceMarker;
import application.context.async.AsyncContext;
import application.context.cases.CaseContext;
import application.context.configuration.ConfigurationContext;
import application.context.filter.FilterContext;

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
			Class temp = Class.forName(entry.getValue());
			if (temp.isAnnotationPresent(Component.class)) {
				if (temp.isAnnotationPresent(Async.class))
					AsyncContext.addAsync(temp);
				if (!temp.isAnnotationPresent(Prototype.class))
					ApplicationContext.putIntoSingletonContext(getInstanceOfClass(temp));
				else
					ApplicationContext.putIntoPrototypeContext(getInstanceOfClass(temp));
				if (temp.isAnnotationPresent(Case.class)) {
					CaseContext.add(temp);
				}
				if (temp.isAnnotationPresent(UserServiceMarker.class)) {
					ApplicationContext.setUserService(temp);
				}
				if(temp.isAnnotationPresent(Filter.class)) {
					FilterContext.addFilter(temp);
				}
			} else if (temp.isAnnotationPresent(Configuration.class))
				ConfigurationContext.addConfig(temp);

		}
		System.out.printf("[INFO] %s Annotation reading finished%n", LocalDateTime.now().toString());
	}

	
	private static Object getInstanceOfClass(Class clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getDeclaredConstructor().newInstance();
	}

}
