package main.cases;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import application.boilerplate.MessageSender;
import application.context.annotation.Case;
import application.context.annotation.Component;
import application.context.annotation.Inject;

@Component
@Case
public class Case0 {

	@Inject
	MessageSender sender;
	
	@Case(caseNumber=0)
	public void onRouteReceived(Update update) {
		SendMessage msg = new SendMessage();
		int userid = update.getMessage().getFrom().getId();
		msg.setChatId(Integer.toString(userid));
		msg.setText("case 0 works");
		sender.sendMessage(msg);
	}
	
}
