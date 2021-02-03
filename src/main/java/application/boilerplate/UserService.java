package application.boilerplate;

import java.util.HashMap;

import application.context.annotation.Component;

@Component
public class UserService{

	HashMap<Integer, Integer> cases = new HashMap<>();

	public int getUserState(int userid) {
		if (cases.get(userid) == null) {
			setUserState(userid, 0);
		}
		return cases.get(userid);
	}

	public void setUserState(int userid, int state) {
		cases.put(userid, state);
	}
	
}
