package application.context.inject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;

import application.context.ApplicationContext;
import application.context.annotation.Inject;
import application.context.annotation.UserServiceMarker;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class Injector {

	private Injector() {}
	
	public static void inject()
			throws IllegalAccessException{
		HashMap<Class, Object> allComponents = new HashMap<>();
		allComponents.putAll(ApplicationContext.getSingletonComponents());
		allComponents.putAll(ApplicationContext.getPrototypeComponents());
		Object userService = ApplicationContext.getUserServiceComponent();
		allComponents.put(userService.getClass(), userService);
		Class userServiceClass = userService.getClass();
		for (Entry<Class, Object> entry : allComponents.entrySet()) {
			Class clazz = entry.getKey();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Inject.class)) {
				    Inject annotation = field.getAnnotation(Inject.class);
					if (field.getType() == userServiceClass) {
						field.setAccessible(true);
						Object injectingObject = userService;
						field.set(entry.getValue(), injectingObject);
						field.setAccessible(false);
					} else {
						Object injectingObject = ApplicationContext.getComponent(field.getType());
						if (injectingObject == null) {
						    String qualifier = annotation.value();
						    if(qualifier.isEmpty()) {
						        throw new NullPointerException("Component for " + field.getType()
                                +"not found in Application Context and no qualifier specified explicitly. Couldn't inject into " + clazz.getName()); 
						    }
						    injectingObject = ApplicationContext.getComponentByQualifier(qualifier);
							if(injectingObject == null) {
							    throw new NullPointerException("Component for " + field.getType()
                                + " or qualifier '"+qualifier+"' not found in Application Context. Couldn't inject into " + clazz.getName()); 
							}
						}
						field.setAccessible(true);
						field.set(entry.getValue(), injectingObject);
						field.setAccessible(false);
					}
				}
				if (field.isAnnotationPresent(UserServiceMarker.class)) {
					field.setAccessible(true);
					Object injectingObject = userService;
					field.set(entry.getValue(), injectingObject);
					field.setAccessible(false);
				}
			}
		}
		printMessage();
	}
	
	private static void printMessage() {
	    System.out.printf("[INFO] %s Dependency injection successful%n", LocalDateTime.now().toString());
	}

}
