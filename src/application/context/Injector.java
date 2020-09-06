package application.context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map.Entry;

import application.context.annotation.Inject;

public class Injector {

	protected static void inject() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		System.out.println("[INFO] Dependency injection started");
		HashMap<Class, Object> allComponents = new HashMap<>();
		allComponents.putAll(ApplicationContext.singletonComponents);
		allComponents.putAll(ApplicationContext.prototypeComponents);
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
					field.setAccessible(false);
				}
			}
		}
		System.out.println("[INFO] Dependency injection finished");
	}

}
