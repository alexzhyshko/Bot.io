package application.context.cases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CaseContext {

	private static List<Class> caseContext = new ArrayList<>();
	
	public static void add(Class caseClass) {
		caseContext.add(caseClass);
	}
	
	public static void init() {
		System.out.printf("[INFO] %s Case context init started\n", LocalDateTime.now().toString());
		for(Class clazz : caseContext) {
			CaseReader.read(clazz);
		}
		System.out.printf("[INFO] %s Case context init finished, found %d cases\n", LocalDateTime.now().toString(), caseContext.size());
	}
	
}
