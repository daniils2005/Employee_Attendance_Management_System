package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.Position;
import lv.venta.model.enums.Status;

public interface IEmployeeRepo extends CrudRepository<Employee, Long>{

	boolean existsByPersonCode(String personCode);

	ArrayList<Employee> findByDepartment(Department departmentForDeleting);

	ArrayList<Employee> findBySurname(String surname);

	ArrayList<Employee> findByStatus(Status status);

	ArrayList<Employee> findByPosition(Position position);

	ArrayList<Employee> findByDepartmentDepartmentName(String department);


	ArrayList<Employee> findTopByOrderByEidDesc();

	//ArrayList<Employee> findByPosition(Position positionForDeleting);

}
