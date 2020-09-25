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
		Case classAnnotation = (Case)clazz.getAnnotation(Case.class);
		int caseNumber = classAnnotation.caseNumber();
		boolean foundAnnotation = false;
		for(Method method : clazz.getDeclaredMethods()) {
			Case caseAnnotation = method.getAnnotation(Case.class);
			Callback callbackAnnotation = method.getAnnotation(Callback.class);
			if(caseAnnotation!=null) {
				foundAnnotation = true;
				router.add(caseNumber, method.getName(), clazz, caseAnnotation.message());
			}
			if(callbackAnnotation!=null) {
				foundAnnotation = true;
				router.addCallback(caseNumber, method.getName(), clazz, callbackAnnotation.command());
			}
		}
		if(!foundAnnotation) {
			throw new NullPointerException("No case/callback method specified in "+clazz);
		}
	}
	
}
