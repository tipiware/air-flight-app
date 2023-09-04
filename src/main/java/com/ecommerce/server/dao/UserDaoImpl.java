package com.ecommerce.server.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ecommerce.server.entity.Users;
import com.ecommerce.server.exception.EntityNotFound;
import com.ecommerce.server.utility.GlobalResources;

@Repository
public class UserDaoImpl implements UserDaoInterface {

	@Autowired
	private EntityManager entityManager;
	
	private Logger logger = GlobalResources.getLogger(UserDaoImpl.class);
	
	/**
	 * Creates account of user
	 * @param users Instance of Users class
	 */
	@Override
	public boolean registerUser(Users users)throws EntityNotFound {
	
		String methodName = "registerUser()";
		logger.info(methodName + " called");
		
		Session cs = entityManager.unwrap(Session.class);
		cs.saveOrUpdate(users);
		return true;
	}
}
