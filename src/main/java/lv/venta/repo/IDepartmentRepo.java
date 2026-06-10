package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Department;

public interface IDepartmentRepo extends CrudRepository<Department, Long>{

}
