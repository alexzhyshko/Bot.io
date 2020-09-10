package application.logo;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class LogoPrinter {

	private LogoPrinter() {
	}

	public static void printLogo() throws FileNotFoundException {
		InputStream is = LogoPrinter.class.getClassLoader().getResourceAsStream("logo.txt");
		Scanner scanner = new Scanner(is);
		while (scanner.hasNextLine()) {
			String data = scanner.nextLine();
			System.out.println(data);
		}
		scanner.close();
	}

}
