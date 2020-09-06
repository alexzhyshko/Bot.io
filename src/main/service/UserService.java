package main.service;

import java.util.HashMap;

import application.context.annotation.Component;

@Component
public class UserService {

	private HashMap<Integer, Integer> userStates = new HashMap<>();
	private HashMap<Integer, HashMap<String, Object>> userSessions = new HashMap<>();
	
	public int getUserCase(int userid) {
		return this.userStates.get(userid)!=null?this.userStates.get(userid):0;
	}
	
	public void setUserCase(int userid, int state) {
		this.userStates.put(userid, state);
	}
	
	public void incrementUserState(int userid) {
		this.userStates.put(userid, getUserCase(userid)+1);
	}
}
