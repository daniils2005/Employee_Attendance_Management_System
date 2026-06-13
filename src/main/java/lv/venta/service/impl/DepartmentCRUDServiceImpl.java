package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.enums.DepartmentName;
import lv.venta.repo.IDepartmentRepo;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.service.IDepartmentCRUDService;

public class DepartmentCRUDServiceImpl implements IDepartmentCRUDService {

	@Autowired
	private IDepartmentRepo departmentRepo;
	
	@Autowired
	private IEmployeeRepo employeeRepo;
	
	@Override
	public ArrayList<Department> selectAllDepartments() throws Exception {
		if(departmentRepo.count() == 0) {
			throw new Exception("Department table is empty");
		}
		ArrayList<Department> result = (ArrayList<Department>)departmentRepo.findAll();
		return result;
	}

	@Override
	public Department selectDepartmentById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!departmentRepo.existsById(id)) {
			throw new Exception("Department with id = " + id + " doesnt exist");
		}
		return departmentRepo.findById(id).get();
	}

	@Override
	@Transactional
	public void deleteDepartmentById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!departmentRepo.existsById(id)) {
			throw new Exception("Department with id = " + id + " doesnt exist");
		}
		Department departmentForDeleting = departmentRepo.findById(id).get();
		ArrayList<Employee> employeesFromDepartment = employeeRepo.findByDepartment(departmentForDeleting);
		Department defaultDepartment = departmentRepo.findByDepartmentName(DepartmentName.Nav_minēts);
		for(var employee : employeesFromDepartment) {
			employee.setDepartment(defaultDepartment);
		}
		employeeRepo.saveAll(employeesFromDepartment);
		departmentRepo.delete(departmentForDeleting);
	}

	@Override
	public void insertNewDepartment(DepartmentName departmentName, String description) throws Exception {
		if(departmentName == null || description == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(departmentRepo.existsByDepartmentNameCode(departmentName)) {
			throw new Exception(departmentName + " department already exists");
		}
		Department newDepartment = new Department(departmentName, description);
		departmentRepo.save(newDepartment);
	}

	@Override
	public void updateDepartmentById(long id, DepartmentName departmentName, String description) throws Exception {
		if(id <= 0 || departmentName == null || description == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!departmentRepo.existsById(id)) {
			throw new Exception("Department with id = " + id + " doesn't exist");
		}
		Department departmentForUpdating = departmentRepo.findById(id).get();
		departmentForUpdating.setDepartmentName(departmentName);
		departmentForUpdating.setDescription(description);
		departmentRepo.save(departmentForUpdating);
	}
}
