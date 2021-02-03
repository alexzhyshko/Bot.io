package application.context.states;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class StateContext {

	private static List<Class> stateContext = new ArrayList<>();
	
	private StateContext() {}
	
	public static void add(Class stateClass) {
	    stateContext.add(stateClass);
	}
	
	public static void init() {
		if(stateContext.isEmpty()) {
			return;
		}
		processAllStateClasses();
		printMessage();
	}
	
	private static void processAllStateClasses() {
	    for(Class clazz : stateContext) {
            StateReader.read(clazz);
        }
    }

    private static void printMessage() {
	    System.out.printf("[INFO] %s Case context init finished, found %d cases%n", LocalDateTime.now().toString(), stateContext.size());
	}
	
}
