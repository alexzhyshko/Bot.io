package application.context;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class Scanner {

	
	//key - filename
	//value - relative path
	protected static Map<String, String> getAllFilesInProject(String path) throws IOException {
		System.out.println("[INFO] File scan started");
		HashMap<String, String> result = new HashMap<>();
		Properties properties = new Properties();
		properties.load(Scanner.class.getClassLoader().getResourceAsStream("configuration.properties"));
		path+=properties.getProperty("rootScanDirectory");
		Collection<File> files = FileUtils.listFiles(new File(path), new String[] {"java"}, true);
		for(File file : files) {
			String relativePath = file.getAbsolutePath().split("src")[1].substring(1).replace("\\", ".");
			result.put(file.getName().split(".class")[0], relativePath.split(".java")[0]);
		}
		System.out.println("[INFO] File scan finished");
		return result;
	}

}
