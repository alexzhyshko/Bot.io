package application.context.configuration;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class ConfigurationContext {

	private static List<Class> configurationClasses = new ArrayList<>();

	private ConfigurationContext() {}
	
	public static void addConfig(Class configClass) {
		configurationClasses.add(configClass);
	}

	public static void performConfiguration() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(configurationClasses.isEmpty()) {
			return;
		}
		for (Class config : configurationClasses) {
			ConfigurationInvoker.invoke(config);
		}
		System.out.printf("[INFO] %s Configuration finished, used %d config class(-es)%n", LocalDateTime.now().toString(), configurationClasses.size());
	}

}
