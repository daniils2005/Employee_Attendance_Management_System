package lv.venta.repo;

import java.io.ObjectInputFilter.Status;
import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.enums.DepartmentName;
import lv.venta.model.enums.Position;

public interface IEmployeeRepo extends CrudRepository<Employee, Long>{

	boolean existsByPersonCode(String personCode);

	ArrayList<Employee> findByDepartment(Department departmentForDeleting);

	ArrayList<Employee> findBySurname(String surname);

	ArrayList<Employee> findByStatus(Status status);

	ArrayList<Employee> findByPosition(Position position);

	ArrayList<Employee> findByDepartmentDepartmentName(DepartmentName department);

}
