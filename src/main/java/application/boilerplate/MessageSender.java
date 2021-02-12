package application.boilerplate;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import application.boilerplate.dto.Button;
import application.boilerplate.dto.InlineButton;
import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.context.annotation.Prototype;
import application.exception.IllegalClassStateException;


/**
 * Use this class to build and send a message to user. You also can add a
 * keyboard using this class
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
@Prototype
public class MessageSender {

	@Inject
	private BotControllerBoilerplate controller = ApplicationContext.getComponent(BotControllerBoilerplate.class);

	private SendMessage message;

	private List<Button> buttons;

	public MessageSender() {
		this.message = new SendMessage();
	}

	public void setText(String text) {
		this.message.setText(text);
	}

	public void setChatId(int userid) {
		this.message.setChatId((long) userid);
	}

	/**
	 * Send a pre-built message and, after a success, restores object's state to
	 * default
	 */
	public void sendMessage() {
		if (this.message.getText().isEmpty()) {
			throw new IllegalClassStateException(
					"Message can't be sent if its text is empty. Use MessageSender#setText() before");
		}
		if (this.message.getChatId() == null || this.message.getChatId().isEmpty()) {
			throw new IllegalClassStateException(
					"Message can't be sent if its chatId is empty. Use MessageSender#setChatId() before");
		}
		controller.sendMessage(this.message);
		this.buttons = null;
		this.message = new SendMessage();
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
		setButtons();
	}
	
	public void setButtons(List<Button> buttons, int columnCount) {
		this.buttons = buttons;
		setButtonsWhithColumnCount(columnCount);
	}

	
	/**
	 * Sets buttons to the reply message using the texts field, which has to be set
	 * before setting buttons. Uses default columnCount, which is set to 2
	 */
	private void setButtons() {
		if (this.buttons == null || this.buttons.isEmpty()) {
			throw new IllegalClassStateException(
					"Butttons can't be set if texts are not set. Use MessageSender#setTexts() before");
		}
		setButtonsWhithColumnCount(2);
	}

	/**
	 * Sets buttons to the reply message using the texts field, which has to be set
	 * before setting buttons.
	 * 
	 * @param columnCount - count of columns for the reply keyboard.
	 *                    ContactButtonIndex has to be greater than zero
	 */
	public void setButtonsWhithColumnCount(int columnCount) {
		if (this.buttons == null || this.buttons.isEmpty()) {
			throw new IllegalClassStateException(
					"Butttons can't be set if texts are not set. Use MessageSender#setTexts() before");
		}
		setSpecialButtons(columnCount);
	}


	/**
	 * Sets buttons to the reply message using the texts field, which has to be set
	 * before setting buttons.
	 * 
	 * @param columnCount         - count of columns for the reply keyboard.
	 *                            columnCount has to be equal or greater than zero
	 */
	public void setSpecialButtons(int columnCount) {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);
		int rowsCount = (int) Math.round(buttons.size() / (double) columnCount);
		List<KeyboardRow> keyboard = new ArrayList<>(rowsCount);
		int buttonNum = 0;
		int totalButtons = buttons.size();
		for (int i = 0; i < rowsCount; i++) {
			KeyboardRow row = new KeyboardRow();
			int iter = 0;
			for (int j = buttonNum; j < totalButtons; j++) {
				Button button = buttons.get(buttonNum);
				KeyboardButton btn = new KeyboardButton(button.getText());
				if (button.isSetAsContactButton()) {
					btn.setRequestContact(true);
				}
				if (button.isSetAsLocationButton()) {
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

	public void setInlineButtons(List<InlineButton> buttons) {
		if (buttons == null || buttons.isEmpty()) {
			throw new IllegalClassStateException("Inline buttons can't be set if the list is null or empty");
		}
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
		for (int i = 0; i < Math.round(buttons.size() / 2.0d); i++) {
			List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
			try {
				InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
				inlineKeyboardButton1.setText(buttons.get(i * 2).getText());
				inlineKeyboardButton1.setCallbackData(buttons.get(i * 2).getCommand());
				try {
					inlineKeyboardButton1
							.setUrl(buttons.get(i * 2).getUrl().orElseThrow(() -> new NullPointerException()));
				} catch (NullPointerException npe) {
					//do not add url if empty
				}
				keyboardButtonsRow1.add(inlineKeyboardButton1);
			} catch (Exception e) {
				// Do nothing if no such index
			}
			try {
				InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
				inlineKeyboardButton2.setText(buttons.get(i * 2 + 1).getText());
				inlineKeyboardButton2.setCallbackData(buttons.get(i * 2 + 1).getCommand());
				try {
					inlineKeyboardButton2
							.setUrl(buttons.get(i * 2 + 1).getUrl().orElseThrow(() -> new NullPointerException()));
				} catch (NullPointerException npe) {
					//do not add url if empty
				}
				keyboardButtonsRow1.add(inlineKeyboardButton2);
			} catch (Exception e) {
				// Do nothing if no such index
			}
			rowList.add(keyboardButtonsRow1);
		}
		inlineKeyboardMarkup.setKeyboard(rowList);
		this.message.setReplyMarkup(inlineKeyboardMarkup);
	}

}
