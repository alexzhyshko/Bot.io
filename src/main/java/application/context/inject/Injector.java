package application.context.inject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;

import application.context.ApplicationContext;
import application.context.annotation.Inject;
import application.context.annotation.UserServiceMarker;

public class Injector {

	public static void inject()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		System.out.printf("[INFO] %s Dependency injection started\n", LocalDateTime.now().toString());
		HashMap<Class, Object> allComponents = new HashMap<>();
		allComponents.putAll(ApplicationContext.getSingletonComponents());
		allComponents.putAll(ApplicationContext.getPrototypeComponents());
		Object userService = ApplicationContext.getUserServiceComponent();
		Class userServiceClass = userService.getClass();
		for (Entry<Class, Object> entry : allComponents.entrySet()) {
			Class clazz = entry.getKey();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Inject.class)) {
					if (field.getType() == userServiceClass) {
						field.setAccessible(true);
						Object injectingObject = userService;
						field.set(entry.getValue(), injectingObject);
						field.setAccessible(false);
					} else {
						Object injectingObject = ApplicationContext.getSingletonComponent(field.getType());
						if (injectingObject == null) {
							injectingObject = ApplicationContext.getPrototypeComponent(field.getType());
						}
						if (injectingObject == null) {
							throw new NullPointerException("Component for type " + field.getType()
									+ " not found in Application Context. Couldn't inject into " + clazz.getName());
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
		System.out.printf("[INFO] %s Dependency injection finished\n", LocalDateTime.now().toString());
	}

}
