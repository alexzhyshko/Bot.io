package application.context;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationContext {

	private static List<Class> configurationClasses = new ArrayList<>();

	protected static void addConfig(Class configClass) {
		configurationClasses.add(configClass);
	}

	protected static void performConfiguration() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		System.out.printf("[INFO] %s Configuration started\n", LocalDateTime.now().toString());
		for (Class config : configurationClasses) {
			ConfigurationInvoker.invoke(config);
		}
		System.out.printf("[INFO] %s Configuration finished\n", LocalDateTime.now().toString());
	}

}
