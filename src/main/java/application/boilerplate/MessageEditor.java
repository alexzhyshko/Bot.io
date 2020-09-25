package application.boilerplate;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import application.boilerplate.dto.InlineButton;
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
public class MessageEditor {

	@Inject
	private BotControllerBoilerplate controller = ApplicationContext.getComponent(BotControllerBoilerplate.class);

	private EditMessageText message;

	public MessageEditor() {
		this.message = new EditMessageText();
	}

	public void setText(String text) {
		this.message.setText(text);
	}

	public void setMessageId(int id) {
		this.message.setMessageId(id);
	}

	public void setChatId(int userid) {
		this.message.setChatId((long) userid);
	}

	/**
	 * Use this method to remove inline buttons from the message you are editing
	 */
	public void clearInlineButtons() {
		this.message.setReplyMarkup(null);
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
		if (this.message.getMessageId() == null) {
			throw new IllegalClassStateException(
					"Message can't be sent if its messageId is empty. Use MessageSender#setMessageId() before");
		}
		controller.editMessage(this.message);
		this.message = new EditMessageText();
	}

	/**
	 * Use this method to set inline buttons to the edited message
	 * 
	 * @param buttons    - Buttons to be set to the message(ordered)
	 */
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
