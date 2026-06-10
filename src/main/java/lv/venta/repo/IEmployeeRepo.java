package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;


import lv.venta.model.Employee;

public interface IEmployeeRepo extends CrudRepository<Employee, Long>{

}
