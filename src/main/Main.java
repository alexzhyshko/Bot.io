package main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import application.context.ApplicationContext;
import application.context.ContextInitializer;
import main.controller.Controller;

public class Main {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		ContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();
		try {
			botsApi.registerBot((LongPollingBot)ApplicationContext.getComponent(Controller.class));
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		
	}

}
