package main.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import application.boilerplate.BotControllerBoilerplate;
import application.context.annotation.Component;

@Component
public class Controller extends BotControllerBoilerplate {
	
	
	public Controller() {
		super();
	}

	public void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
