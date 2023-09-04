package com.ecommerce.server.service;

import com.ecommerce.server.entity.Users;

public interface UserServiceInterface {

	boolean registerUser(Users users);
	
	Integer validateUser(String uname, String password);
	
}
