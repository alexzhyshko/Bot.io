package application.context;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class Scanner {

	
	//key - filename
	//value - relative path
	protected static Map<String, String> getAllFilesInProject(String path) throws IOException {
		System.out.printf("[INFO] %s File scan started\n", LocalDateTime.now().toString());
		HashMap<String, String> result = new HashMap<>();
		Properties properties = new Properties();
		properties.load(Scanner.class.getClassLoader().getResourceAsStream("configuration.properties"));
		String rootScanDirectory = properties.getProperty("rootScanDirectory");
		if(rootScanDirectory==null) {
			System.out.printf("[INFO] %s Root scan directory is set to NULL\n", LocalDateTime.now().toString(), rootScanDirectory);
			throw new NullPointerException("Root scan directory cannot be null, please set it as 'rootScanDirectory' is configuration.properties file");
		}
		System.out.printf("[INFO] %s Root scan directory is set to '%s'\n", LocalDateTime.now().toString(), rootScanDirectory);
		path+=rootScanDirectory;
		Collection<File> files = FileUtils.listFiles(new File(path), new String[] {"java"}, true);
		for(File file : files) {
			//System.out.println(file);
			String relativePath = file.getAbsolutePath().split("src")[1].substring(1).replace("\\", ".");
			result.put(file.getName().split(".java")[0], relativePath.split(".java")[0]);
		}
		System.out.printf("[INFO] %s File scan finished\n", LocalDateTime.now().toString());
		return result;
	}

}
