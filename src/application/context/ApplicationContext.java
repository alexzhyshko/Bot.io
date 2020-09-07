package application.context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import application.context.async.AsyncContext;
import application.context.configuration.ConfigurationContext;
import application.context.inject.Injector;
import application.context.reader.AnnotationReader;
import application.context.scan.Scanner;

public class ApplicationContext {

	private ApplicationContext() {
	}

	protected static HashMap<Class, Object> singletonComponents = new HashMap<>();
	protected static HashMap<Class, Object> prototypeComponents = new HashMap<>();

	protected static void init(String path) throws IOException {
		System.out.printf("[INFO] %s Application context initialization started\n", LocalDateTime.now().toString());
		try {
		Map<String, String> files = Scanner.getAllFilesInProject(path);
		AnnotationReader.process(files);
		Injector.inject();
		ConfigurationContext.performConfiguration();
		AsyncContext.runAsync();
		}catch(Exception e) {
			e.printStackTrace();
			destroy();
			System.exit(1);
		}
		System.out.printf("[INFO] %s Application context initialization finished with %d singleton classes and %d prototype(-s)\n", LocalDateTime.now().toString(), singletonComponents.size(), prototypeComponents.size());
	}

	protected static void destroy() {
		System.out.println("Destroyed");
	}

	
	
	public static HashMap<Class, Object> getSingletonComponents() {
		return singletonComponents;
	}

	public static HashMap<Class, Object> getPrototypeComponents() {
		return prototypeComponents;
	}

	public static void putIntoSingletonContext(Object object) {
		singletonComponents.put(object.getClass(), object);
	}

	public static Object getSingletonComponent(Class instanceClass) {
		return singletonComponents.get(instanceClass);
	}

	public static void putIntoPrototypeContext(Object object) {
		prototypeComponents.put(object.getClass(), object);
	}

	public static Object getPrototypeComponent(Class instanceClass)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (!prototypeComponents.containsKey(instanceClass)) {
			return null;
		}
		return instanceClass.getDeclaredConstructor().newInstance();
	}
	
	public static <T> T getInstance(Class<T> clazz) {
		if (singletonComponents.get(clazz) != null) {
			return (T)singletonComponents.get(clazz);
		}
		return (T)prototypeComponents.get(clazz);
	}

}
