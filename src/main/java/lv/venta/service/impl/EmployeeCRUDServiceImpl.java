package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.User;
import lv.venta.model.enums.Position;
import lv.venta.model.enums.Status;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.repo.IUserRepo;
import lv.venta.service.IEmployeeCRUDService;

@Service
public class EmployeeCRUDServiceImpl implements IEmployeeCRUDService {

	@Autowired
	private IEmployeeRepo employeeRepo;
	
	@Autowired
	private IUserRepo userRepo;
	
	private String nameRegex = "^[A-Z][a-zA-Z]{1,29}$";
	private String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	
	@Override
	public ArrayList<Employee> selectAllEmployees() throws Exception {
		ArrayList<Employee> result = (ArrayList<Employee>)employeeRepo.findAll();
		return result;
	}

	@Override
	public Employee selectEmployeeById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		return employeeRepo.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteEmployeeById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!employeeRepo.existsById(id)) {
			throw new Exception("Employee with id = " + id + " doesnt exist");
		}
		Employee employeeForDeleting = employeeRepo.findById(id).get();
		User userTiedToEmployee = userRepo.findByEmployee(employeeForDeleting);
		userRepo.delete(userTiedToEmployee);;
		employeeRepo.delete(employeeForDeleting);
	}

	@Override
	public void insertNewEmployee(String newName, String newSurname, String newPersonCode, String newNumber, String newEmail, double newHourlyRate, Department newDepartment, Status newStatus, Position newPosition) throws Exception {
		if(newName == null || newSurname == null || newPersonCode == null || newStatus == null || !newName.matches(nameRegex) || !newSurname.matches(nameRegex) || !newPersonCode.matches("^[0-9]{6}-[0-9]{5}$") || newNumber == null || !newNumber.matches("^[0-9]{8}$") || newEmail == null || !newEmail.matches(emailRegex) || newHourlyRate < 0 || newPosition == null || newDepartment == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(employeeRepo.existsByPersonCode(newPersonCode)) {
			throw new Exception("Employee with person code " + newPersonCode + " already exists");
		}
		Employee newEmployee = new Employee(newName, newSurname, newPersonCode, newNumber, newEmail, newHourlyRate, newDepartment, newStatus, newPosition);
		employeeRepo.save(newEmployee);
	}

	@Override
	public void updateEmployeeById(long id, String newName, String newSurname, String newNumber, String newEmail, double newHourlyRate, Department newDepartment, Status newStatus, Position newPosition) throws Exception {
		if(!employeeRepo.existsById(id)) {
			throw new Exception("Employee with id = " + id + " doesn't exist");
		}
		Employee employeeForUpdating = employeeRepo.findById(id).get();
		employeeForUpdating.setName(newName);
		employeeForUpdating.setSurname(newSurname);
		employeeForUpdating.setNumber(newNumber);
		employeeForUpdating.setEmail(newEmail);
		employeeForUpdating.setHourlyRate(newHourlyRate);
		employeeForUpdating.setDepartment(newDepartment);
		employeeForUpdating.setStatus(newStatus);
		employeeForUpdating.setPosition(newPosition);
		employeeRepo.save(employeeForUpdating);
	}

}
