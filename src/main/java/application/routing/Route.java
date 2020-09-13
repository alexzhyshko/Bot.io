package application.routing;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class Route {

	private int caseNumber;
	private String methodName;
	private Class routeClass;

	public Route(int caseNumber, String methodName, Class routeClass) {
		this.caseNumber = caseNumber;
		this.methodName = methodName;
		this.routeClass = routeClass;
	}

	public int getCase() {
		return this.caseNumber;
	}

	public String getMethodName() {
		return methodName;
	}

	public Class getRouteClass() {
		return routeClass;
	}
}
