package main.service;

import java.util.HashMap;

import application.context.annotation.Component;
import application.context.annotation.UserServiceMarker;

@Component
@UserServiceMarker
public class UserService {

	private HashMap<Integer, Integer> userStates = new HashMap<>();
	private HashMap<Integer, HashMap<String, Object>> userSessions = new HashMap<>();
	
	public int getUserState(int userid) {
		return this.userStates.get(userid)!=null?this.userStates.get(userid):0;
	}
	
	public void setUserState(int userid, int state) {
		this.userStates.put(userid, state);
	}
	
	public void incrementUserState(int userid) {
		this.userStates.put(userid, getUserState(userid)+1);
	}
}
