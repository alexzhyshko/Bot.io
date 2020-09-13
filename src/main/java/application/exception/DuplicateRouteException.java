package application.exception;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class DuplicateRouteException extends RuntimeException{

	public DuplicateRouteException(String message) {
		super(message);
	}
	
}
