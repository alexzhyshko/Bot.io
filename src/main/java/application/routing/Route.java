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
	private String command;
	
	public Route(int caseNumber, String methodName, Class routeClass, String command) {
		this.caseNumber = caseNumber;
		this.methodName = methodName;
		this.routeClass = routeClass;
		this.command = command;
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
	
	public String getCommand() {
		return command;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + caseNumber;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((routeClass == null) ? 0 : routeClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Route other = (Route) obj;
		if (caseNumber != other.caseNumber)
			return false;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (routeClass == null) {
			if (other.routeClass != null)
				return false;
		} else if (!routeClass.equals(other.routeClass))
			return false;
		return true;
	}
	
	
}
