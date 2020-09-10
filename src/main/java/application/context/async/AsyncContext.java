package application.context.async;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsyncContext {
	private static List<Class> asyncClasses = new ArrayList<>();

	public static void addAsync(Class asyncClass) {
		asyncClasses.add(asyncClass);
	}

	public static void runAsync()
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(asyncClasses.isEmpty()) {
			return;
		}
		for (Class config : asyncClasses) {
			AsyncInvoker.invoke(config);
		}
		System.out.printf("[INFO] %s Async init finished, found %d async class(-es)\n", LocalDateTime.now().toString(), asyncClasses.size());
	}
}
