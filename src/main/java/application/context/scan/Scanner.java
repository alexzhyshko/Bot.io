package application.context.scan;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scanner {

	// key - filename
	// value - relative path
	public static Map<String, String> getAllFilesInProject(String packageName) throws IOException, URISyntaxException {
		HashMap<String, String> result = new HashMap<>();
		URI uri = Scanner.class.getResource("/" + packageName).toURI();
		Path myPath;
		boolean runningFromJar = false;
		//check if now we load from filesystem or jar
		if (uri.getScheme().equals("jar")) {
			FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
			myPath = fileSystem.getPath("/", packageName);
			runningFromJar = true;
		} else {
			myPath = Paths.get(uri);
		}
		
		//loading all files with 'infinite' depth
		Stream<Path> walk = Files.walk(myPath, Integer.MAX_VALUE);
		int count = 0;
		//run through all files
		for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
			Path next = it.next();
			//check if it is source class, not metadata for instance
			if (next.getFileName().toString().endsWith("class")) {
				count++;
				String relativePath = "";
				//construct FQN using absolute path
				if (runningFromJar) {
					//for loading from jar
					relativePath = next.toAbsolutePath().toString().substring(1).replace("\\", "/").split(".class")[0].replace("/",".");
					
				} else {
					//for loading from filesystem
					relativePath = next.toAbsolutePath().toString().replace("\\", "/").split("/classes/")[1]
							.split(".class")[0].replace("/", ".");
				}
				//construct filename
				String filename = next.getFileName().toString().split(".class")[0];
				result.put(filename, relativePath);
			}
		}
		walk.close();
		System.out.printf("[INFO] %s File scan finished in %s package, found %d files%n", LocalDateTime.now().toString(), packageName ,count);
		return result;
	}

}
