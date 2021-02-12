package application.context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import application.context.async.AsyncContext;
import application.context.inject.Injector;
import application.context.reader.AnnotationReader;
import application.context.reader.PropertyReader;
import application.context.scan.Scanner;
import application.context.states.StateContext;
import application.exception.PackageNameException;
import application.logo.LogoPrinter;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class ApplicationContext {

	private ApplicationContext() {
	}

	protected static HashMap<Class, Object> singletonComponents = new HashMap<>();
	protected static HashMap<Class, Object> prototypeComponents = new HashMap<>();
	protected static Class userServiceClass;
	protected static Object userServiceObject;

	private static int CURRENT_USER_ID;
	
	private static final String CORE_PACKAGE_NAME = "application";

	protected static void init(String path) throws IOException {
		try {
		    printLogo();
		    printWarningMessage();
		    loadProperties();
			String rootPackageName = getRootPackageName();
			if(rootPackageName == null || rootPackageName.isEmpty()) {
				throw new PackageNameException("Root package shouldn't be null or empty. Please encapsulate your project in a separate package");
			}
			Map<String, String> coreFiles = getAllFilesInCorePackage();
			Map<String, String> projectFiles = Scanner.getAllFilesInPackage(rootPackageName);
			combineFileMaps(coreFiles, projectFiles);
			performAnnotationReadingAmongGivenFiles(coreFiles);
			injectDependencies();
			initializeCasesContext();
			startAsynchronousCode();
		} catch (Exception e) {
			e.printStackTrace();
			destroy();
		}
		System.out.printf(
				"[INFO] %s Application context initialization finished with %d singleton classes and %d prototype(-s)%n",
				LocalDateTime.now().toString(), singletonComponents.size(), prototypeComponents.size());
	}
	
    private static void combineFileMaps(Map<String, String> firstMap, Map<String, String> secondMap) {
	    firstMap.putAll(secondMap);
    }

    private static void printLogo() throws FileNotFoundException {
	    LogoPrinter.printLogo();
	}

	private static void printWarningMessage() {
	    System.out.printf("[WARNING] It is not recommended to create package '%s' as it might interfere with frameworks's core package '%s'%n", CORE_PACKAGE_NAME, CORE_PACKAGE_NAME);
	}
	
	private static void loadProperties() {
	    PropertyReader.load();
	}
	
	private static String getRootPackageName() {
	    return PropertyReader.getProperty("rootPackage");
	}
	
	private static Map<String, String> getAllFilesInCorePackage() throws IOException, URISyntaxException{
	    return Scanner.getAllFilesInPackage(CORE_PACKAGE_NAME);
	}
	
	private static void performAnnotationReadingAmongGivenFiles(Map<String, String> files) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	    AnnotationReader.process(files);
	}
	
	private static void injectDependencies() throws IllegalAccessException {
	    Injector.inject();
	}
	
	private static void initializeCasesContext() {
        StateContext.init();
    }
	
    private static void startAsynchronousCode() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        AsyncContext.runAsync();
    }
	
	protected static void destroy() {
		System.out.println("Destroyed");
		System.exit(1);
	}

	public static void setUserService(Class clazz) {
		userServiceClass = clazz;
	}

	public static Object getUserServiceComponent() {
		try {
			if (userServiceObject == null) {
				userServiceObject = userServiceClass.getDeclaredConstructor().newInstance();
			}
			return userServiceObject;
		} catch (Exception e) {
			throw new IllegalArgumentException("No UserService class found");
		}

	}

	public static HashMap<Class, Object> getSingletonComponents() {
		return singletonComponents;
	}

	public static HashMap<Class, Object> getPrototypeComponents() {
		return prototypeComponents;
	}

	public static void putIntoSingletonContext(Object object) {
		singletonComponents.put(object.getClass(), object);
	}

	public static Object getSingletonComponent(Class instanceClass) {
		return singletonComponents.get(instanceClass);
	}

	public static void putIntoPrototypeContext(Object object) {
		prototypeComponents.put(object.getClass(), object);
	}

	public static Object getPrototypeComponent(Class instanceClass)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (!prototypeComponents.containsKey(instanceClass)) {
			throw new NullPointerException("Class not found in prototype context");
		}
		return instanceClass.getDeclaredConstructor().newInstance();
	}

	public static <T> T getComponent(Class<T> clazz) {
		if (singletonComponents.get(clazz) != null) {
			return (T) singletonComponents.get(clazz);
		}
		return (T) prototypeComponents.get(clazz);
	}

	public static int getUserState(int userid) {
		try {
			return (Integer) userServiceClass.getDeclaredMethod("getUserState", int.class).invoke(userServiceObject,
					userid);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException(
					"No method getUserState(userid) specified in a class marked with @UserServiceMarker");
		}
	}
	
	public static int getCurrentUserState() {
	    return getUserState(getCurrentUserId());
	}
	
	public static void setUserState(int userid, int state) {
		try {
			userServiceClass.getDeclaredMethod("setUserState", int.class, int.class).invoke(userServiceObject,
					userid, state);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException(
					"No method setUserState(userid, state) specified in a class marked with @UserServiceMarker");
		}
	}
	
	/**
	 * Do not use this method in asynchronous code, as it may lead to unexpected return values
	 * @return Current User's Id
	 */
	public static int getCurrentUserId() {
	    return CURRENT_USER_ID;
	}
	
	
	/**
     * Do not use this method in asynchronous code, as it may lead to unexpected consequences
     */
	public static void setCurrentUserId(int userid) {
	    CURRENT_USER_ID = userid;
	}

	
    public static Object getComponentByQualifier(String qualifier) {
        Optional<Object> component = singletonComponents.entrySet().stream().filter(entry->entry.getKey().getSimpleName().equalsIgnoreCase(qualifier)).map(entry->entry.getValue()).findFirst(); 
        return component.isPresent()?component.get():null;
    }
	

}
