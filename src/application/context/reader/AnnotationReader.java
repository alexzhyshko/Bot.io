package application.context.reader;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import application.context.ApplicationContext;
import application.context.annotation.Async;
import application.context.annotation.Component;
import application.context.annotation.Configuration;
import application.context.annotation.Prototype;
import application.context.async.AsyncContext;
import application.context.configuration.ConfigurationContext;

public class AnnotationReader {

	public static void process(Map<String, String> files)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		System.out.printf("[INFO] %s Annotation reading started\n", LocalDateTime.now().toString());
		for (Entry<String, String> entry : files.entrySet()) {
			Class temp = Class.forName(entry.getValue());
			if (hasComponentAnnotation(temp)) {
				if (hasAsyncAnnotation(temp)) {
					AsyncContext.addAsync(temp);
				} else {
					// System.out.println(temp.getName());
					if (!hasPrototypeAnnotation(temp))
						ApplicationContext.putIntoSingletonContext(getInstanceOfClass(temp));
					else
						ApplicationContext.putIntoPrototypeContext(getInstanceOfClass(temp));
				}
			} else if (hasConfigurationAnnotation(temp))
				ConfigurationContext.addConfig(temp);

		}
		System.out.printf("[INFO] %s Annotation reading finished\n", LocalDateTime.now().toString());
	}
	
	

	private static boolean hasAsyncAnnotation(Class clazz) {
		return clazz.getDeclaredAnnotation(Async.class) != null;
	}

	private static boolean hasComponentAnnotation(Class clazz) {
		return clazz.getDeclaredAnnotation(Component.class) != null;
	}

	private static boolean hasConfigurationAnnotation(Class clazz) {
		return clazz.getDeclaredAnnotation(Configuration.class) != null;
	}

	private static boolean hasPrototypeAnnotation(Class clazz) {
		return clazz.getDeclaredAnnotation(Prototype.class) != null;
	}

	private static Object getInstanceOfClass(Class clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getDeclaredConstructor().newInstance();
	}

}
