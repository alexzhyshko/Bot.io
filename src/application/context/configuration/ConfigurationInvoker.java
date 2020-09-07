package application.context.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import application.configurators.ConfiguratorAdapter;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.routing.Router;

@Component
public class ConfigurationInvoker {

	@Inject
	private static Router router;

	protected static <T extends ConfiguratorAdapter> void invoke(Class<T> configurator)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method configure;
		configure = configurator.getMethod("configure", Router.class);
		configure.invoke(null, router);
	}

}
