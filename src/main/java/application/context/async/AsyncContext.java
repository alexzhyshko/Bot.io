package application.context.async;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class AsyncContext {
	private static List<Class> asyncClasses = new ArrayList<>();

	private AsyncContext() {}
	
	public static void addAsync(Class asyncClass) {
		asyncClasses.add(asyncClass);
	}

	public static void runAsync()
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(asyncClasses.isEmpty()) {
			return;
		}
		invokeAsyncMethods();
		printMessage();
	}
	
	private static void invokeAsyncMethods() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
	    for (Class config : asyncClasses) {
            AsyncInvoker.invoke(config);
        }
    }

    private static void printMessage() {
	    System.out.printf("[INFO] %s Async init finished, found %d async class(-es)%n", LocalDateTime.now().toString(), asyncClasses.size());
	}
}
