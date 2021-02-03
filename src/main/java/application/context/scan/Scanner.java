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
import java.util.stream.Stream;

import application.exception.PackageNameException;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
public class Scanner {

	private Scanner() {}
	
	// key - filename
	// value - FQN
	public static Map<String, String> getAllFilesInPackage(String packageName) throws IOException, URISyntaxException {
		HashMap<String, String> result = new HashMap<>();
		URI uri;
		try {
			uri = Scanner.class.getResource("/" + packageName).toURI();
		} catch (NullPointerException e) {
			throw new PackageNameException(
					"Package with name '" + packageName + "' not found or it is not a root package");
		}
		Path pathToProject;
		boolean runningFromJar = false;
		FileSystem fileSystem = null;
		try {
		 // check if now we load from filesystem or jar
			if (uri.getScheme().equals("jar")) {
				fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
				pathToProject = fileSystem.getPath("/", packageName);
				runningFromJar = true;
			} else {
			    pathToProject = Paths.get(uri);
			}

			// loading all files with 'infinite' depth
			try (Stream<Path> fileWalk = createFileWalkStream(pathToProject)) {
				int classFilesCount = 0;
				// run through all files
				for (Iterator<Path> fileIterator = fileWalk.iterator(); fileIterator.hasNext();) {
					Path pathToFile = fileIterator.next();
					// check if it is source class, not metadata for instance
					if (pathToFile.getFileName().toString().endsWith("class")) {
					    classFilesCount++;
						String fullyQualifiedName;
						// construct FQN using absolute path
						if (runningFromJar) {
							// for loading from jar
						    fullyQualifiedName = constructFQNForLoadingFromJar(pathToFile);
						} else {
							// for loading from filesystem
						    fullyQualifiedName = constructFQNForLoadingFromFilesystem(pathToFile);
						}
						// construct FQN
						String className = constructFileName(pathToFile);
						result.put(className, fullyQualifiedName);
					}
				}
				printMessage(packageName, classFilesCount);
			}
		} finally {
			if (fileSystem != null) {
				fileSystem.close();
			}
		}
		return result;
	}
	
	private static Stream<Path> createFileWalkStream(Path path) throws IOException{
	    return Files.walk(path, Integer.MAX_VALUE);
	}
	
	private static String constructFQNForLoadingFromJar(Path path) {
	    return path.toAbsolutePath().toString().substring(1).replace("\\", "/")
                .split(".class")[0].replace("/", ".");
	}
	
	private static String constructFQNForLoadingFromFilesystem(Path path) {
	    return path.toAbsolutePath().toString().replace("\\", "/").split("/classes/")[1]
                .split(".class")[0].replace("/", ".");
	}
	
	private static String constructFileName(Path path) {
	    return path.getFileName().toString().split(".class")[0];
	}
	
	private static void printMessage(String packageName, int classFilesCount) {
        System.out.printf("[INFO] %s File scan finished in %s package, found %d files%n",
                LocalDateTime.now().toString(), packageName, classFilesCount);
    }

}
