package main.cases;

import java.util.Optional;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import application.context.annotation.Case;
import application.context.annotation.Component;
import application.context.annotation.Inject;
import main.controller.Controller;
import main.service.UserService;

@Component
@Case
public class Case0 {

	@Inject
	Controller controller;
	
	@Inject
	UserService userService;
	
	@Case(caseNumber = 0)
	public void onRouteReceived(Update update) {
		SendMessage msg = new SendMessage();
		int userid = update.getMessage().getFrom().getId();
		msg.setChatId(Integer.toString(userid));
		StackWalker walker = StackWalker.getInstance();
	    Optional<String> methodName = walker.walk(frames -> frames
	      .findFirst()
	      .map(StackWalker.StackFrame::getMethodName));
	    Optional<String> className = walker.walk(frames -> frames
	  	      .findFirst()
	  	      .map(StackWalker.StackFrame::getClassName));
		msg.setText(className.get()+"#"+methodName.get());
		controller.sendMessage(msg);
		userService.incrementUserState(userid);
	}
	
}
