package application.context.async;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import application.configurators.ConfiguratorAdapter;
import application.context.ApplicationContext;
import application.context.annotation.Async;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class AsyncInvoker {

	private AsyncInvoker() {}
	
	protected static <T extends ConfiguratorAdapter> void invoke(Class<T> async)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Method method : async.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Async.class)) {
				new Thread(() -> {
					try {
						method.invoke(ApplicationContext.getComponent(async));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}).start();
			}
		}

	}

}
