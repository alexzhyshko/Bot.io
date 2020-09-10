package application.context;

public class ContextInitializer{

	public static void init() {
		String path = System.getProperty("user.dir");
		try {
			ApplicationContext.init(path);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void destroy() {
		ApplicationContext.destroy();
	}

}
