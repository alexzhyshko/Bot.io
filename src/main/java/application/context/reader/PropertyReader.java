package application.context.reader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

import application.context.annotation.Component;
import application.exception.FileLoadException;
import application.exception.IllegalClassStateException;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
public class PropertyReader {

	private static Properties properties = new Properties();
	
	private static final String FILENAME = "application.properties";
	
	public PropertyReader() {}
	
	public static void load() {
		try {
			properties.load(PropertyReader.class.getClassLoader().getResourceAsStream(FILENAME));
		} catch (IOException e) {
			throw new FileLoadException("Couldn't load properties file");
		}
		System.out.printf("[INFO] %s Properties loaded%n", LocalDateTime.now().toString());
	}
	
	public static String getProperty(String key) {
		if(properties == null) {
			throw new IllegalClassStateException("Can't use this method before properties are loaded");
		}
		return properties.getProperty(key);
	}
	
}
