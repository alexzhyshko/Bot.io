package application.context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import application.context.async.AsyncContext;
import application.context.cases.CaseContext;
import application.context.configuration.ConfigurationContext;
import application.context.inject.Injector;
import application.context.reader.AnnotationReader;
import application.context.scan.Scanner;

public class ApplicationContext {

	private ApplicationContext() {
	}

	protected static HashMap<Class, Object> singletonComponents = new HashMap<>();
	protected static HashMap<Class, Object> prototypeComponents = new HashMap<>();
	protected static Class userServiceClass;
	protected static Object userServiceObject;
	
	protected static void init(String path) throws IOException {
		System.out.printf("[INFO] %s Application context initialization started\n", LocalDateTime.now().toString());
		try {
		Map<String, String> files = Scanner.getAllFilesInProject(path);
		AnnotationReader.process(files);
		Injector.inject();
		ConfigurationContext.performConfiguration();
		CaseContext.init();
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

	public static void setUserService(Class clazz) {
		userServiceClass = clazz;
	}
	
	public static Object getUserServiceComponent(){
		try {
			if(userServiceObject==null) {
				userServiceObject = userServiceClass.getDeclaredConstructor().newInstance();
			}
			return userServiceObject;
		} catch (Exception e) {
			throw new IllegalArgumentException("No UserService class found");
		}
		
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
			throw new NullPointerException("Class not found in prototype context");
		}
		return instanceClass.getDeclaredConstructor().newInstance();
	}
	
	public static <T> T getComponent(Class<T> clazz) {
		if (singletonComponents.get(clazz) != null) {
			return (T)singletonComponents.get(clazz);
		}
		return (T)prototypeComponents.get(clazz);
	}
	
	public static int getUserState(int userid) {
		try {
			return (Integer)userServiceClass.getDeclaredMethod("getUserState", int.class).invoke(userServiceObject, userid);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException("No method getUserState() specified in a class marked with @UserServiceMarker");
		}
	}

}
