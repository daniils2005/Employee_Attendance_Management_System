package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.repo.IDepartmentRepo;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.service.IDepartmentCRUDService;

@Service
public class DepartmentCRUDServiceImpl implements IDepartmentCRUDService {

	@Autowired
	private IDepartmentRepo departmentRepo;
	
	@Autowired
	private IEmployeeRepo employeeRepo;
	
	@Override
	public ArrayList<Department> selectAllDepartments() throws Exception {
		return(ArrayList<Department>)departmentRepo.findAll();
	}

	@Override
	public Department selectDepartmentById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!departmentRepo.existsById(id)) {
			throw new Exception("Department with id=" + id + " doesn't exist");
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
			throw new Exception("Department with id=" + id + " doesn't exist");
		}
		Department departmentForDeleting = departmentRepo.findById(id).get();
		ArrayList<Employee> employeesFromDepartment = employeeRepo.findByDepartment(departmentForDeleting);
		Department defaultDepartment = departmentRepo.findByDepartmentName("Nav_minets");
		for(var employee : employeesFromDepartment) {
			employee.setDepartment(defaultDepartment);
		}
		employeeRepo.saveAll(employeesFromDepartment);
		departmentRepo.delete(departmentForDeleting);
	}

	@Override
	public void insertNewDepartment(String departmentName, String description) throws Exception {
		if(departmentName == null || description == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(departmentRepo.existsByDepartmentName(departmentName)) {
			throw new Exception(departmentName + " department already exists");
		}
		Department newDepartment = new Department(departmentName, description);
		departmentRepo.save(newDepartment);
	}

	@Override
	public void updateDepartmentById(long id, String departmentName, String description) throws Exception {
		if(id <= 0 || departmentName == null || description == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!departmentRepo.existsById(id)) {
			throw new Exception("Department with id=" + id + " doesn't exist");
		}

		Department departmentForUpdating = departmentRepo.findById(id).get();
		departmentForUpdating.setDepartmentName(departmentName);
		departmentForUpdating.setDescription(description);
		departmentRepo.save(departmentForUpdating);
	}
}
