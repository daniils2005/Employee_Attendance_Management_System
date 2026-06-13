package lv.venta.service.impl;

import java.util.ArrayList;

import lv.venta.model.Employee;
import lv.venta.model.User;
import lv.venta.model.enums.Role;
import lv.venta.service.IUserCRUDService;

public class UserCRUDServiceImpl implements IUserCRUDService {

	@Override
	public ArrayList<User> selectAllUsers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectUserById(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserById(long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertNewUser(String newUsername, String newPassword, Role newRole, Employee newEmployee)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserById(long id, String newUsername, String newPassword, Role newRole, Employee newEmployee)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
