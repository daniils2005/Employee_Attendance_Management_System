package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Department;
import lv.venta.model.Vacation;
import lv.venta.model.enums.DepartmentName;

public interface IDepartmentRepo extends CrudRepository<Department, Long>{

	boolean existsByDepartmentName(DepartmentName departmentName);

	Department findByDepartmentName(DepartmentName navMinēts);

	ArrayList<Department> findTopByOrderByDidDesc();

}
