package application.boilerplate;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.context.filter.FilterContext;
import application.context.reader.PropertyReader;
import application.exception.FileLoadException;
import application.routing.Router;
import application.session.SessionManager;

/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
public class BotControllerBoilerplate extends TelegramLongPollingBot {

	@Inject
	private Router router;

	@Inject
	SessionManager sessionManager;

	private String token;
	private String username;

	public BotControllerBoilerplate() {
		this.token = PropertyReader.getProperty("bot.token");
		this.username = PropertyReader.getProperty("bot.username");
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (FilterContext.filter(update)) {
			try {
				int userid = -1;
				if (update.hasCallbackQuery()) {
					userid = update.getCallbackQuery().getFrom().getId();
					sessionManager.load(userid);
					int caseNumber = ApplicationContext.getUserState(userid);
					String command = update.getCallbackQuery().getData();
					router.routeCallback(update, caseNumber, command);
				} else {
					userid = update.getMessage().getFrom().getId();
					sessionManager.load(userid);
					int caseNumber = ApplicationContext.getUserState(userid);
					String message = update.getMessage().getText();
					router.route(update, caseNumber, message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	protected void editMessage(EditMessageText editMessage) {
		try {
			execute(editMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	protected void deleteMessage(DeleteMessage deleteMessage) {
		try {
			execute(deleteMessage);
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
