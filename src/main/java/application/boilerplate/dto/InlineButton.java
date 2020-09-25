package application.boilerplate.dto;

import java.util.Optional;

public class InlineButton {

	private String text;
	private String command;
	private Optional<String> url;
	
	
	public InlineButton(String text, String command) {
		this.text = text;
		this.command = command;
		this.url = Optional.empty();
	} 
	
	public InlineButton(String text, String command, String url) {
		this.text = text;
		this.command = command;
		this.url = Optional.of(url);
	}
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Optional<String> getUrl() {
		return url;
	}
	public void setUrl(Optional<String> url) {
		this.url = url;
	}
	
	
	
}
