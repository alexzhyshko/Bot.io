package application.context;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationContext {

	private static List<Class> configurationClasses = new ArrayList<>();

	protected static void addConfig(Class configClass) {
		configurationClasses.add(configClass);
	}

	protected static void performConfiguration() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		for (Class config : configurationClasses) {
			ConfigurationInvoker.invoke(config);
		}
	}

}
