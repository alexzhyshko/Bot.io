package application.boilerplate;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import application.context.annotation.Prototype;
import application.exception.IllegalClassStateException;

@Component
@Prototype
/**
 * Use this class to build and send a document to user
 * @author alexzhyshko
 *
 */
public class DocumentSender {

	@Inject
	private BotControllerBoilerplate controller = ApplicationContext.getComponent(BotControllerBoilerplate.class);

	private SendDocument document;

	public DocumentSender() {
		this.document = new SendDocument();
	}

	public void setFile(InputFile file) {
		this.document.setDocument(new InputFile().setMedia(file.getNewMediaStream(), file.getMediaName()));
	}

	public void setChatId(int userid) {
		this.document.setChatId((long) userid);
	}

	/**
	 * Send a pre-built document and, after a success, restores object's state to default
	 */
	public void sendDocument() {
		if (this.document.getDocument() == null) {
			throw new IllegalClassStateException(
					"Document can't be sent if its document is not set. Use DocumentSender#setDocument() before");
		}
		if (this.document.getChatId() == null || this.document.getChatId().isEmpty()) {
			throw new IllegalClassStateException(
					"Document can't be sent if its chatId is not set. Use DocumentSender#setDocument() before");
		}
		controller.sendDocument(this.document);
		this.document = new SendDocument();
	}

}
