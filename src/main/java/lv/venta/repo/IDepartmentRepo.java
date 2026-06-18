package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Department;

public interface IDepartmentRepo extends CrudRepository<Department, Long>{

	boolean existsByDepartmentName(String departmentName);

	Department findByDepartmentName(String string);

	ArrayList<Department> findTopByOrderByDidDesc();

}
