package application.boilerplate;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.context.annotation.Prototype;
import application.exception.IllegalClassStateException;


@Component
@Prototype
/**
 * Use this class to build and send a message to user. You also can add a keyboard using this class
 * @author alexzhyshko
 *
 */
public class MessageSender {

	@Inject
	private BotControllerBoilerplate controller = ApplicationContext.getComponent(BotControllerBoilerplate.class);
	
	private SendMessage message;
	
	private List<String> texts;
	
	public MessageSender() {
		this.message = new SendMessage();
	}
	
	public void setText(String text) {
		this.message.setText(text);
	}
	
	public void setChatId(int userid) {
		this.message.setChatId((long)userid);
	}
	
	
	/**
	 * Send a pre-built message and, after a success, restores object's state to default
	 */
	public void sendMessage() {
		if(this.message.getText().isEmpty()) {
			throw new IllegalClassStateException("Message can't be sent if its text is empty. Use MessageSender#setText() before");
		}
		if(this.message.getChatId()==null||this.message.getChatId().isEmpty()) {
			throw new IllegalClassStateException("Message can't be sent if its chatId is empty. Use MessageSender#setChatId() before");
		}
		controller.sendMessage(this.message);
		this.texts = null;
		this.message = new SendMessage();
	}
	
	public void setButtonTexts(List<String> texts) {
		this.texts = texts;
	}
	
	
	/**
	 * Sets buttons to the reply message using the texts field, which has to be set before setting buttons.
	 * Uses default columnCount, which is set to 2
	 */
	public void setButtons() {
		if(this.texts==null||this.texts.isEmpty()) {
			throw new IllegalClassStateException("Butttons can't be set if texts are not set. Use MessageSender#setTexts() before");
		}
		setButtonsWhithColumnCount(2);
	}
	
	/**
	 * Sets buttons to the reply message using the texts field, which has to be set before setting buttons.
	 * @param columnCount - count of columns for the reply keyboard. ContactButtonIndex has to be greater than zero
	 */
	public void setButtonsWhithColumnCount(int columnCount) {
		if(this.texts==null||this.texts.isEmpty()) {
			throw new IllegalClassStateException("Butttons can't be set if texts are not set. Use MessageSender#setTexts() before");
		}
		setSpecialButtons(columnCount, -1, -1);
	}
	
	
	/**
	 * Sets buttons to the reply message using the texts field, which has to be set before setting buttons.
	 * Also sets a location request button by its index 
	 * @param columnCount - count of columns for the reply keyboard. columnCount has to be equal or greater than zero
	 * @param locationButtonIndex - index of the button, which will be set as a location request button. locationButtonIndex has to be greater than zero
	 */
	public void setLocationButton(int columnCount, int locationButtonIndex) {
		if(this.texts==null||this.texts.isEmpty()) {
			throw new IllegalClassStateException("Butttons can't be set if texts are not set. Use MessageSender#setTexts() before");
		}
		setSpecialButtons(columnCount, -1, locationButtonIndex);
	}
	
	
	/**
	 * Sets buttons to the reply message using the texts field, which has to be set before setting buttons.
	 * Also sets a contact request button by its index 
	 * @param columnCount - count of columns for the reply keyboard. columnCount has to be equal or greater than zero
	 * @param contactButtonIndex - index of the button, which will be set as a contact request button. contactButtonIndex has to be greater than zero
	 */
	public void setContactButton(int columnCount, int contactButtonIndex) {
		if(this.texts==null||this.texts.isEmpty()) {
			throw new IllegalClassStateException("Butttons can't be set if texts are not set. Use MessageSender#setTexts() before");
		}
		setSpecialButtons(columnCount, contactButtonIndex, -1);
	}
	
	/**
	 * Sets buttons to the reply message using the texts field, which has to be set before setting buttons.
	 * @param columnCount - count of columns for the reply keyboard. columnCount has to be equal or greater than zero
	 * @param contactButtonIndex - index of the button, which will be set as a contact request button. contactButtonIndex has to be greater than zero
	 * @param locationButtonIndex - ndex of the button, which will be set as a location request button. locationButtonIndex has to be greater than zero
	 */
	public void setSpecialButtons(int columnCount, int contactButtonIndex, int locationButtonIndex) {
		if(this.texts==null||this.texts.isEmpty()) {
			throw new IllegalClassStateException("Butttons can't be set if texts are not set. Use MessageSender#setTexts() before");
		}
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);
		int rowsCount = (int) Math.round(texts.size() / (double) columnCount);
		List<KeyboardRow> keyboard = new ArrayList<>(rowsCount);
		int buttonNum = 0;
		int totalButtons = texts.size();
		for (int i = 0; i < rowsCount; i++) {
			KeyboardRow row = new KeyboardRow();
			int iter = 0;
			for (int j = buttonNum; j < totalButtons; j++) {
				KeyboardButton btn = new KeyboardButton(texts.get(buttonNum));
				if (buttonNum == contactButtonIndex) {
					btn.setRequestContact(true);
				}
				if(buttonNum == locationButtonIndex) {
					btn.setRequestLocation(true);
				}
				row.add(btn);
				buttonNum++;
				if (iter == 1) {
					break;
				}
				iter++;
			}
			keyboard.add(row);
		}
		replyKeyboardMarkup.setKeyboard(keyboard);
		this.message.setReplyMarkup(replyKeyboardMarkup);
	}
	
	
}
