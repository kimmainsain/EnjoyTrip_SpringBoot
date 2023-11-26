package com.ssafy.user.service;

import com.ssafy.user.dto.User;
import com.ssafy.user.dto.UserSecret;

public interface UserService {
	User login(String id, String pw);
	boolean signUp(UserSecret user);
	boolean dropOut(String id, String pw);
	boolean reset(String id);
	boolean edit(UserSecret user);
	User select(String id);
}
