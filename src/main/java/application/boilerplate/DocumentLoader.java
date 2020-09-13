package application.boilerplate;

import java.io.File;

import org.telegram.telegrambots.meta.api.methods.GetFile;

import application.context.ApplicationContext;
import application.context.annotation.Component;
import application.context.annotation.Inject;



/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
public class DocumentLoader {

	@Inject
	private BotControllerBoilerplate controller = ApplicationContext.getComponent(BotControllerBoilerplate.class);
	
	public File loadDocument(int documentId) {
		GetFile getFile = new GetFile().setFileId(Integer.toString(documentId));
		return controller.loadDocument(getFile);
	}
	
}
