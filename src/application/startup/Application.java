package application.startup;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import application.boilerplate.BotControllerBoilerplate;
import application.context.ApplicationContext;
import application.context.ContextInitializer;

public class Application {

	private Application() {}
	
	
	/**
	 * A method, which has to be called to start the application
	 */
	public static void start() {
		ApiContextInitializer.init();
		ContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();
		try {
			botsApi.registerBot((LongPollingBot)ApplicationContext.getComponent(BotControllerBoilerplate.class));
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		
	}
	
}
