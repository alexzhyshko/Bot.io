package main.config;

import application.configurators.RouterConfiguratorAdapter;
import application.context.annotation.Configuration;
import application.routing.Router;

@Configuration
public class Configurator extends RouterConfiguratorAdapter {

	private Configurator() {
	}

	public static void configure(Router router) {
//		for (int i = 0; i < 12; i++) {
//			try {
//				router.add(i, "onRouteReceived", Class.forName("main.cases.Case"+i));
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
		
	}

}
