package com.ecommerce.server.service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.server.dao.UserDaoImpl;
import com.ecommerce.server.entity.Users;
import com.ecommerce.server.exception.EntityNotFound;
import com.ecommerce.server.utility.GlobalResources;


/**
 * 
 * @author Sumeet Patil
 * This is Service Class
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserServiceInterface {

	@Autowired
	private UserDaoImpl daoObject;
	@Autowired
	private EntityManager entityManager;

	private Logger logger = GlobalResources.getLogger(UserServiceImpl.class);

	/**
	 * This method checks wheter username is taken or not.
	 * @param users
	 * @return boolean 
	 * 
	 */
	public boolean isUsernameTaken(Users users) throws EntityNotFound{
		try {
			String methodName = "isUsernameTaken()";
			logger.info(methodName + " called");
			String command = "select user.username from Users user where user.username =: uName";
			TypedQuery<String> query = entityManager.createQuery(command, String.class);
			query.setParameter("uName", users.getUsername());
			String username = query.getSingleResult();

			return false;
		} catch (Exception e) {			 
			return true;
		}

	}

	/*
	 * This methods creates account of the user
	 * @param users Instance of User class
	 * @return boolean status
	 * 
	 */
	@Transactional
	@Override
	public boolean registerUser(Users users) throws EntityNotFound {

		String methodName = "registerUser()";
		logger.info(methodName + " called");

		Integer userId = 1;

		if (!isUsernameTaken(users)) {
			return false;
		}

		try {
			String command = "select count(ud.userId) from Users ud";
			TypedQuery<Long> query = entityManager.createQuery(command, Long.class);
			long count = query.getSingleResult();
			if (count > 0) {
				command = "select max(ud.userId) from Users ud";
				TypedQuery<Integer> query2 = entityManager.createQuery(command, Integer.class);
				Integer uid = query2.getSingleResult();
				userId = uid + 1;
			}
			users.setUserId(userId);
			return daoObject.registerUser(users);
		} catch (Exception e) {
			 
			throw new EntityNotFound("Cannot Register ..!");
		}
	}
	
	/**
	 * This method validates username and password for login into system
	 * @param uName
	 * @param password
	 * @return Integer
	 */

	@Override
	public Integer validateUser(String uName, String password) throws EntityNotFound{

		String methodName = "validateUser()";
		logger.info(methodName + " called");

		try {
			String command = "select ud.userId from Users ud where ud.username = :uName and ud.password = :pass";
			TypedQuery<Integer> query = entityManager.createQuery(command, Integer.class);
			query.setParameter("uName", uName);
			query.setParameter("pass", password);
			Integer uid = 0;
			uid = query.getSingleResult();
			return uid;
		} catch (Exception e) {
			 
			throw new EntityNotFound("User Invalid");
		}
	}


}
