package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Department;
import lv.venta.model.enums.DepartmentName;

public interface IDepartmentCRUDService {
	
	public abstract ArrayList<Department> selectAllDepartments() throws Exception;
	
	public abstract Department selectDepartmentById(long id) throws Exception;
	
	public abstract void deleteDepartmentById(long id) throws Exception;
	
	public abstract void insertNewDepartment(DepartmentName departmentName, String description) throws Exception;
	
	public abstract void updateDepartmentById(long id, DepartmentName departmentName, String description) throws Exception;
}
