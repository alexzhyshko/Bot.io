package application.context.states;

import java.lang.reflect.Method;

import application.context.ApplicationContext;
import application.context.annotation.Callback;
import application.context.annotation.Message;
import application.context.annotation.State;
import application.routing.Router;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class StateReader {

	private StateReader() {}
	
	public static void read(Class clazz) {
		Router router = ApplicationContext.getComponent(Router.class);
		State classAnnotation = (State)clazz.getAnnotation(State.class);
		int stateNumber = classAnnotation.value();
		boolean foundAnnotation = false;
		for(Method method : clazz.getDeclaredMethods()) {
			Message caseAnnotation = method.getAnnotation(Message.class);
			Callback callbackAnnotation = method.getAnnotation(Callback.class);
			if(caseAnnotation!=null) {
				foundAnnotation = true;
				router.add(stateNumber, method.getName(), clazz, caseAnnotation.message());
			}
			if(callbackAnnotation!=null) {
				foundAnnotation = true;
				router.addCallback(stateNumber, method.getName(), clazz, callbackAnnotation.command());
			}
		}
		if(!foundAnnotation) {
			throw new NullPointerException("No case/callback method specified in "+clazz);
		}
	}
	
}
