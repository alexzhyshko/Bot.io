package application.context.inject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;

import application.context.ApplicationContext;
import application.context.annotation.Inject;

public class Injector {

	public static void inject() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		System.out.printf("[INFO] %s Dependency injection started\n", LocalDateTime.now().toString());
		HashMap<Class, Object> allComponents = new HashMap<>();
		allComponents.putAll(ApplicationContext.getSingletonComponents());
		allComponents.putAll(ApplicationContext.getPrototypeComponents());
		for (Entry<Class, Object> entry : allComponents.entrySet()) {
			Class clazz = entry.getKey();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Inject.class)) {
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
					//System.out.println("Injected "+ injectingObject.getClass()+" to "+clazz.getName()+"#"+field.getName());
					field.setAccessible(false);
				}
			}
		}
		System.out.printf("[INFO] %s Dependency injection finished\n", LocalDateTime.now().toString());
	}

}
