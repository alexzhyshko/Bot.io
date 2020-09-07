package main.controller;

import java.io.IOException;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.routing.Router;
import main.service.UserService;


@Component
public class Controller extends TelegramLongPollingBot {
	@Inject
	Router router;
	
	@Inject
	UserService userService;
	
	private String token;
	private String username;
	
	public Controller() {
		Properties properties = new Properties();
		try {
			properties.load(Controller.class.getClassLoader().getResourceAsStream("configuration.properties"));
			this.token = properties.getProperty("bot.token");
			this.username = properties.getProperty("bot.username");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpdateReceived(Update update) {
		int userid = update.getMessage().getFrom().getId();
		int caseNumber = userService.getUserCase(userid);
		router.route(update, caseNumber);
	}

	@Override
	public String getBotUsername() {
		return this.username;
	}

	@Override
	public String getBotToken() {
		return this.token;
	}
	
	public void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
