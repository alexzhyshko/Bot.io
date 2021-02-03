package application.session;

import java.util.HashMap;

import application.context.annotation.Component;


/**
 * 
 * @author Oleksandr Zhyshko
 *
 */
@Component
public class SessionManager {

	private HashMap<Integer, Session> sessions = new HashMap<>();
	
	private Session currentSession;
	
	public void load(int userid) {
		if(!this.sessions.containsKey(userid)) {
			this.sessions.put(userid, new Session());
		}
		this.currentSession = this.sessions.get(userid);
	}
	
	public <T> T getProperty(String key, Class<T> targetClass) {
		return this.currentSession.getProperty(key, targetClass);
	}
	
	public <T> void setProperty(String key, T value) {
		this.currentSession.setProperty(key, value);
	}
	
}
