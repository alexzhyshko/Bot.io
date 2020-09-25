package application.boilerplate;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.context.annotation.Prototype;
import application.exception.IllegalClassStateException;

/**
 * Use this class to build and send an edited message to user. You also can add
 * an inline keyboard using this class
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
@Prototype
public class MessageDeleter {

	@Inject
	private BotControllerBoilerplate controller = ApplicationContext.getComponent(BotControllerBoilerplate.class);

	private DeleteMessage message;

	public MessageDeleter() {
		this.message = new DeleteMessage();
	}

	public void setMessageId(int id) {
		this.message.setMessageId(id);
	}

	public void setChatId(int userid) {
		this.message.setChatId((long) userid);
	}


	/**
	 * Delete a message and, after a success, restores object's state to
	 * default
	 */
	public void deleteMessage() {
		try {
			this.message.validate();
		}catch(TelegramApiValidationException e) {
			throw new IllegalClassStateException(
					"Message is not valid");
		}
		controller.deleteMessage(this.message);
		this.message = new DeleteMessage();
	}

}
