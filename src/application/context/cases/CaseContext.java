package application.context.cases;

import java.util.ArrayList;
import java.util.List;

public class CaseContext {

	private static List<Class> caseContext = new ArrayList<>();
	
	public static void add(Class caseClass) {
		caseContext.add(caseClass);
	}
	
	public static void init() {
		for(Class clazz : caseContext) {
			CaseReader.read(clazz);
		}
	}
	
}
