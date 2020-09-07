package application.context.async;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import application.configurators.ConfiguratorAdapter;
import application.context.annotation.AsyncMethod;

public class AsyncInvoker {

	protected static <T extends ConfiguratorAdapter> void invoke(Class<T> async)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method asyncMethod = null;
		for(Method method : async.getDeclaredMethods()) {
			if(method.isAnnotationPresent(AsyncMethod.class)) {
				asyncMethod = method;
				break;
			}
		}
		if(asyncMethod == null) {
			throw new NullPointerException("No method with @AsyncMethod annotation present in "+async.getCanonicalName()+" class");
		}
		Method methodToInvoke = asyncMethod;
		new Thread(() -> {
			try {
				methodToInvoke.invoke(null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}).start();
	}

}
