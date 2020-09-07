package application.context.scan;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;

public class Scanner {

	
	//key - filename
	//value - relative path
	public static Map<String, String> getAllFilesInProject(String path) throws IOException {
		System.out.printf("[INFO] %s File scan started\n", LocalDateTime.now().toString());
		HashMap<String, String> result = new HashMap<>();
		Properties properties = new Properties();
		try {
			String filepath = new File(Scanner.class.getProtectionDomain().getCodeSource().getLocation()
				    .toURI()).getPath();
			JarFile jarFile = new JarFile(filepath);
			Enumeration<JarEntry> e = jarFile.entries();
			int index=0;
			while(e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				if(index==0) {
					index++;
					continue;
				}
				String[] pathPoints = entry.getName().split("/");
				String name = pathPoints[pathPoints.length-1].split("\\.")[0];
				String pathToFile = entry.getName().split("\\.")[0].replace("/", ".");
				result.put(name, pathToFile);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		properties.load(Scanner.class.getClassLoader().getResourceAsStream("application.properties"));
		String rootScanDirectory = properties.getProperty("rootScanDirectory");
		if(rootScanDirectory==null) {
			System.out.printf("[INFO] %s Root scan directory is set to NULL\n", LocalDateTime.now().toString(), rootScanDirectory);
			throw new NullPointerException("Root scan directory cannot be null, please set it as 'rootScanDirectory' is configuration.properties file");
		}
		System.out.printf("[INFO] %s Root scan directory is set to '%s'\n", LocalDateTime.now().toString(), rootScanDirectory);
		path+=rootScanDirectory;
		Collection<File> files = FileUtils.listFiles(new File(path), new String[] {"java"}, true);
		for(File file : files) {
			String relativePath = file.getAbsolutePath().split("src")[1].substring(1).replace("\\", ".");
			result.put(file.getName().split(".java")[0], relativePath.split(".java")[0]);
		}
		System.out.printf("[INFO] %s File scan finished, found %d files\n", LocalDateTime.now().toString(), result.size());
		return result;
	}

}
