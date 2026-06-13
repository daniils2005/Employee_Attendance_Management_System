package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Department;
import lv.venta.model.Employee;

public interface IEmployeeRepo extends CrudRepository<Employee, Long>{

	boolean existsByPersonCode(String personCode);

	ArrayList<Employee> findByDepartment(Department departmentForDeleting);

}
