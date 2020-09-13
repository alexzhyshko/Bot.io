package application.context.cases;

import java.lang.reflect.Method;

import application.context.ApplicationContext;
import application.context.annotation.Callback;
import application.context.annotation.Case;
import application.routing.Router;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class CaseReader {

	private CaseReader() {}
	
	public static void read(Class clazz) {
		Router router = ApplicationContext.getComponent(Router.class);
		boolean foundAnnotation = false;
		for(Method method : clazz.getDeclaredMethods()) {
			Case caseAnnotation = method.getAnnotation(Case.class);
			Callback callbackAnnotation = method.getAnnotation(Callback.class);
			if(caseAnnotation!=null) {
				foundAnnotation = true;
				router.add(caseAnnotation.caseNumber(), method.getName(), clazz);
			}
			if(callbackAnnotation!=null) {
				foundAnnotation = true;
				router.addCallback(callbackAnnotation.caseNumber(), method.getName(), clazz);
			}
		}
		if(!foundAnnotation) {
			throw new NullPointerException("No case method specified in "+clazz);
		}
	}
	
}
