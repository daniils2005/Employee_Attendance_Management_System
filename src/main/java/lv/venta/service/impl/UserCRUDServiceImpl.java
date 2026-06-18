package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Authority;
import lv.venta.model.Employee;
import lv.venta.model.User;
import lv.venta.repo.IUserRepo;
import lv.venta.service.IUserCRUDService;

@Service
public class UserCRUDServiceImpl implements IUserCRUDService {

	@Autowired
	private IUserRepo userRepo;
	
	@Override
	public ArrayList<User> selectAllUsers() throws Exception {
		ArrayList<User> result = (ArrayList<User>)userRepo.findAll();
		return result;
	}

	@Override
	public User selectUserById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!userRepo.existsById(id)) {
			throw new Exception("User with id = " + id + " doesnt exist");
		}
		return userRepo.findById(id).get();
	}

	@Override
	public void deleteUserById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!userRepo.existsById(id)) {
			throw new Exception("User with id = " + id + " doesn't exist");
		}
		User userForDeleting = userRepo.findById(id).get();		
		userRepo.delete(userForDeleting);
	}

	@Override
	public void insertNewUser(String newUsername, String newPassword, Employee newEmployee, Authority newAuthority) throws Exception {
		if(newUsername == null || !newUsername.matches("^.{3,}$") || newPassword == null || newEmployee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(userRepo.existsByUsername(newUsername)) {
			throw new Exception("User with username " + newUsername + " already exists");
		}
		User newUser = new User(newUsername, newPassword, newEmployee, newAuthority);
		userRepo.save(newUser);
	}

	@Override
	public void updateUserById(long id, String newUsername, String newPassword, Employee newEmployee, Authority newAuthority) throws Exception {
		if(id <= 0 || newUsername == null || !newUsername.matches("^.{3,}$") || newPassword == null || newEmployee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!userRepo.existsById(id)) {
			throw new Exception("User with id = " + id + " doesn't exist");
		}
		User userForUpdating = userRepo.findById(id).get();
		if(!userForUpdating.getUsername().equals(newUsername)) {
			if(userRepo.existsByUsername(newUsername)) {
				throw new Exception("Cant cange username to " + newUsername + " because it is already taken");
			}
		}
		userForUpdating.setUsername(newUsername);
		userForUpdating.setPassword(newPassword);
		userForUpdating.setEmployee(newEmployee);	
		userRepo.save(userForUpdating);
	}

}
