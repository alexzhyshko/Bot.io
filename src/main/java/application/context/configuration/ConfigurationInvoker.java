package application.context.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import application.configurators.ConfiguratorAdapter;
import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.routing.Router;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
public class ConfigurationInvoker {

	private ConfigurationInvoker() {}
	
	protected static <T extends ConfiguratorAdapter> void invoke(Class<T> configurator)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Router router = ApplicationContext.getComponent(Router.class);
		Method configure = configurator.getMethod("configure", Router.class);
		configure.invoke(null, router);
	}

}
