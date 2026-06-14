package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Department;
import lv.venta.model.enums.DepartmentName;

public interface IDepartmentRepo extends CrudRepository<Department, Long>{

	boolean existsByDepartmentName(DepartmentName departmentName);

	Department findByDepartmentName(DepartmentName navMinēts);

}
