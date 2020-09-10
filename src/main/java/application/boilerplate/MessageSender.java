package application.boilerplate;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;


@Component
public class MessageSender {

	@Inject
	private BotControllerBoilerplate controller = ApplicationContext.getComponent(BotControllerBoilerplate.class);
	
	public void sendMessage(SendMessage message) {
		controller.sendMessage(message);
	}
	
}
