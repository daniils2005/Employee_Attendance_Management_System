package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Authority;
import lv.venta.model.Employee;
import lv.venta.model.User;

public interface IUserCRUDService {

	public abstract ArrayList<User> selectAllUsers() throws Exception;
	
	public abstract User selectUserById(long id) throws Exception;
	
	public abstract void deleteUserById(long id) throws Exception;
	
	public abstract void insertNewUser(String newUsername, String newPassword, Employee newEmployee, Authority newAuthority) throws Exception;
	
	public abstract void updateUserById(long id, String newUsername, String newPassword, Employee newEmployee, Authority newAuthority) throws Exception;
}
