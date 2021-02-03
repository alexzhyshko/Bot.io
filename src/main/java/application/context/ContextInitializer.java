package application.context;

import application.boilerplate.UserService;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class ContextInitializer{

	public static void init() {
		String path = System.getProperty("user.dir");
		try {
			ApplicationContext.setUserService(UserService.class);
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
