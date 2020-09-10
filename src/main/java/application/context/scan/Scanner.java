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

	// key - filename
	// value - relative path
	public static Map<String, String> getAllFilesInProject(String path) throws IOException {
		System.out.printf("[INFO] %s File scan started\n", LocalDateTime.now().toString());
		HashMap<String, String> result = new HashMap<>();
		Properties properties = new Properties();
		properties.load(Scanner.class.getClassLoader().getResourceAsStream("application.properties"));
		String rootScanDirectory = properties.getProperty("rootScanDirectory");
		if (rootScanDirectory == null) {
			System.out.printf("[INFO] %s Root scan directory is set to NULL\n", LocalDateTime.now().toString(),
					rootScanDirectory);
			throw new NullPointerException(
					"Root scan directory cannot be null, please set it as 'rootScanDirectory' is configuration.properties file");
		}
		System.out.printf("[INFO] %s Root scan directory is set to '%s'\n", LocalDateTime.now().toString(),
				rootScanDirectory);
		path += rootScanDirectory;
		Collection<File> files = FileUtils.listFiles(new File(path), new String[] { "java" }, true);
		for (File file : files) {
			String relativePath = file.getAbsolutePath().replace("\\", "/").split(rootScanDirectory)[1]
					.split(".java")[0].replace("/", ".");
			String filename = file.getName().split(".java")[0];
			result.put(filename, relativePath);
		}
		System.out.printf("[INFO] %s File scan finished, found %d files\n", LocalDateTime.now().toString(),
				result.size());
		return result;
	}

}
