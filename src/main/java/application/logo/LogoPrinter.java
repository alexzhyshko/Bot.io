package application.logo;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class LogoPrinter {

	private LogoPrinter() {
	}

	public static void printLogo() throws FileNotFoundException {
		Scanner scanner = new Scanner(LogoPrinter.class.getResourceAsStream("logo.txt"));
		while (scanner.hasNextLine()) {
			String data = scanner.nextLine();
			System.out.println(data);
		}
		scanner.close();
	}

}
