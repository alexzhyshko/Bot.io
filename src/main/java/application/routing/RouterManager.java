package application.routing;

import application.context.annotation.Component;
import application.context.annotation.Inject;

@Component
public class RouterManager {

	@Inject
	private Router router;
	
	public void routeToClass(int userid, Class routeClass) {
		this.router.routeToClass(userid, routeClass);
	}

	public void routeCallbackToClass(int userid, Class routeClass) {
		this.router.routeCallbackToClass(userid, routeClass);
	}
	
}
