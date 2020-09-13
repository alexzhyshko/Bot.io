package application.exception;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class RouteNotFoundException extends RuntimeException{

	public RouteNotFoundException(String message) {
		super(message);
	}
	
}
