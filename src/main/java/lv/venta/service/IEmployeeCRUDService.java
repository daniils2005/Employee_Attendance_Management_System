package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.Position;
import lv.venta.model.enums.Status;

public interface IEmployeeCRUDService {

	public abstract ArrayList<Employee> selectAllEmployees() throws Exception;
	
	public abstract Employee selectEmployeeById(long id) throws Exception;
	
	public abstract void deleteEmployeeById(long id) throws Exception;
	
	public abstract void insertNewEmployee(String newName, String newSurname, String newPersonCode, String newNumber, String newEmail, double newHourlyRate, Department newDepartment, Status newStatus, Position newPosition) throws Exception;
	
	public abstract void updateEmployeeById(long id, String newName, String newSurname, String newNumber, String newEmail, double newHourlyRate, Department newDepartment, Status newStatus, Position newPosition) throws Exception;
}
