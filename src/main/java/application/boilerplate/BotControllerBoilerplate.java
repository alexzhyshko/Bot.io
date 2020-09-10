package application.boilerplate;

import java.io.IOException;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.context.annotation.UserServiceMarker;
import application.exception.FileLoadException;
import application.routing.Router;

@Component
public class BotControllerBoilerplate extends TelegramLongPollingBot{

	@Inject
	Router router;
	
	@UserServiceMarker
	Object userService;
	
	private String token;
	private String username;
	
	public BotControllerBoilerplate() {
		Properties properties = new Properties();
		try {
			properties.load(BotControllerBoilerplate.class.getClassLoader().getResourceAsStream("application.properties"));
			this.token = properties.getProperty("bot.token");
			this.username = properties.getProperty("bot.username");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpdateReceived(Update update) {
		int userid = update.getMessage().getFrom().getId();
		int caseNumber = ApplicationContext.getUserState(userid);
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
	
	protected void sendMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	protected void sendDocument(SendDocument sendDocument) {
		try {
			execute(sendDocument);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	protected java.io.File loadDocument(GetFile getFile) {
		try {
			String filePath = execute(getFile).getFilePath();
			return downloadFile(filePath);
		} catch (TelegramApiException e) {
			throw new FileLoadException("Loading file encountered a problem. File couldn't be loaded");
		}
		
	}
	
	
	
	
}
