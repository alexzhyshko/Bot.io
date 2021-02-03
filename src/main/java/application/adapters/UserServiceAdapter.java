package application.adapters;

public interface UserServiceAdapter {

	public int getUserState(int userId);
	public void setUserState(int userId, int state);
	
}
