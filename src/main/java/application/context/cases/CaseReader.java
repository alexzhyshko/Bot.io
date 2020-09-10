package application.context.cases;

import java.lang.reflect.Method;

import application.context.ApplicationContext;
import application.context.annotation.Case;
import application.routing.Router;


public class CaseReader {

	
	public static void read(Class clazz) {
		Router router = ApplicationContext.getComponent(Router.class);
		boolean foundAnnotation = false;
		for(Method method : clazz.getDeclaredMethods()) {
			Case annotation = method.getAnnotation(Case.class);
			if(annotation!=null) {
				foundAnnotation = true;
				router.add(annotation.caseNumber(), method.getName(), clazz);
			}
		}
		if(!foundAnnotation) {
			throw new NullPointerException("No case method specified in "+clazz);
		}
	}
	
}
