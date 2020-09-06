package application.context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
		}catch(Exception e) {
			e.printStackTrace();
			destroy();
			System.exit(1);
		}
		System.out.printf("[INFO] %s Application context initialization finished\n", LocalDateTime.now().toString());
	}

	protected static void destroy() {
		System.out.println("Destroyed");
	}

	protected static void putIntoSingletonContext(Object object) {
		singletonComponents.put(object.getClass(), object);
	}

	protected static Object getSingletonComponent(Class instanceClass) {
		return singletonComponents.get(instanceClass);
	}

	protected static void putIntoPrototypeContext(Object object) {
		prototypeComponents.put(object.getClass(), object);
	}

	protected static Object getPrototypeComponent(Class instanceClass)
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
