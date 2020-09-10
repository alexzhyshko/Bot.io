package application.context.reader;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import application.boilerplate.BotControllerBoilerplate;
import application.boilerplate.DocumentLoader;
import application.boilerplate.DocumentSender;
import application.boilerplate.MessageSender;
import application.context.ApplicationContext;
import application.context.annotation.Async;
import application.context.annotation.Case;
import application.context.annotation.Component;
import application.context.annotation.Configuration;
import application.context.annotation.Prototype;
import application.context.annotation.UserServiceMarker;
import application.context.async.AsyncContext;
import application.context.cases.CaseContext;
import application.context.configuration.ConfigurationContext;
import application.context.configuration.ConfigurationInvoker;
import application.routing.Router;

public class AnnotationReader {

	public static void process(Map<String, String> files)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		for (Entry<String, String> entry : files.entrySet()) {
			Class temp = Class.forName(entry.getValue());
			if (hasComponentAnnotation(temp)) {
				if (hasAsyncAnnotation(temp))
					AsyncContext.addAsync(temp);
				if (!hasPrototypeAnnotation(temp))
					ApplicationContext.putIntoSingletonContext(getInstanceOfClass(temp));
				else
					ApplicationContext.putIntoPrototypeContext(getInstanceOfClass(temp));
				if(hasCaseAnnotation(temp)) {
					CaseContext.add(temp);
				}
				if(hasUserServiceAnnotation(temp)) {
					ApplicationContext.setUserService(temp);
				}
			} else if (hasConfigurationAnnotation(temp))
				ConfigurationContext.addConfig(temp);

		}
		//addCoreFiles();
		System.out.printf("[INFO] %s Annotation reading finished%n", LocalDateTime.now().toString());
	}

	private static void addCoreFiles() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ApplicationContext.putIntoSingletonContext(getInstanceOfClass(BotControllerBoilerplate.class));
		ApplicationContext.putIntoSingletonContext(getInstanceOfClass(ConfigurationInvoker.class));
		ApplicationContext.putIntoSingletonContext(getInstanceOfClass(Router.class));
		ApplicationContext.putIntoPrototypeContext(getInstanceOfClass(MessageSender.class));
		ApplicationContext.putIntoPrototypeContext(getInstanceOfClass(DocumentSender.class));
		ApplicationContext.putIntoPrototypeContext(getInstanceOfClass(DocumentLoader.class));
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

	private static boolean hasCaseAnnotation(Class clazz) {
		return clazz.getDeclaredAnnotation(Case.class) != null;
	}
	
	private static boolean hasUserServiceAnnotation(Class clazz) {
		return clazz.getDeclaredAnnotation(UserServiceMarker.class) != null;
	}
	
	private static Object getInstanceOfClass(Class clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getDeclaredConstructor().newInstance();
	}

}
