package application.boilerplate.dto;

public class Button {

	private String text;
	private boolean setAsContactButton;
	private boolean setAsLocationButton;
	
	public Button(String text) {
		this.text = text;
	}

	public Button(String text, boolean setAsContactButton, boolean setAsLocationButton) {
		this.text = text;
		this.setAsContactButton = setAsContactButton;
		this.setAsLocationButton = setAsLocationButton;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSetAsContactButton() {
		return setAsContactButton;
	}
	public void setSetAsContactButton(boolean setAsContactButton) {
		this.setAsContactButton = setAsContactButton;
	}
	public boolean isSetAsLocationButton() {
		return setAsLocationButton;
	}
	public void setSetAsLocationButton(boolean setAsLocationButton) {
		this.setAsLocationButton = setAsLocationButton;
	}
	
	
}
