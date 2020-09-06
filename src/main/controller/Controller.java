package main.controller;

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

	Router router = Router.getInstance();
	
	@Inject
	UserService userService;
	
	@Override
	public void onUpdateReceived(Update update) {
		int userid = update.getMessage().getFrom().getId();
		int caseNumber = userService.getUserCase(userid);
		router.route(update, caseNumber);
	}

	@Override
	public String getBotUsername() {
		return "my1stjob_employers_bot";
	}

	@Override
	public String getBotToken() {
		return "1178225428:AAE09N7caDg6QVV2sxFvWRUuUj-Pdip-uYY";
	}
	
	public void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
