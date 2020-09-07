package application.boilerplate;

import java.io.IOException;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.routing.Router;
import main.controller.Controller;
import main.service.UserService;

@Component
public class BotControllerBoilerplate extends TelegramLongPollingBot{

	@Inject
	Router router;
	
	@Inject
	UserService userService;
	
	private String token;
	private String username;
	
	public BotControllerBoilerplate() {
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
		System.out.println("received");
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
	
}
