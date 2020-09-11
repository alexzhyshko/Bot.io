package application.session;

import java.util.HashMap;

public class Session {

	private HashMap<String, Object> values = new HashMap<>();
	
	public void setProperty(String key, Object value) {
		values.put(key, value);
	}
	
	public <T> T getProperty(String key, Class<T> targetClass){
		T result = (T)this.values.get(key);
		if(result == null) {
			throw new NullPointerException("No value for this key");
		}
		return result;
	}
	
}
