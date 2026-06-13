package lv.venta.repo;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Employee;
import lv.venta.model.Overtime;

public interface IOvertimeRepo extends CrudRepository<Overtime, Long>{

	boolean existsByEmployeeAndDate(Employee newEmployee, LocalDate now);

}
