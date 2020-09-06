package application.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import application.configurators.ConfiguratorAdapter;
import application.routing.Router;

public class ConfigurationInvoker {

	private static Router router = Router.getInstance();

	protected static <T extends ConfiguratorAdapter> void invoke(Class<T> configurator)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method configure;
		configure = configurator.getMethod("configure", Router.class);
		configure.invoke(null, router);
	}

}
