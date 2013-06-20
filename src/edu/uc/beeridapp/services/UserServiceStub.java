package edu.uc.beeridapp.services;

import edu.uc.beeridapp.dao.IUserDAO;
import edu.uc.beeridapp.dao.UserDAOStub;
import edu.uc.beeridapp.dto.User;

public class UserServiceStub implements IUserService {

	IUserDAO userDAO;

	@Override
	public boolean logon(String email, String password) throws Exception {

		userDAO = new UserDAOStub();
		User testUser = userDAO.fetch(email);
		if (testUser.getPassword().equals(password)) {
			return true;
		} else if(testUser.equals(null)) {
			return false;
		}

		return false;

	}

}
