package lv.venta.repo;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import lv.venta.model.Attendance;
import lv.venta.model.Employee;
import lv.venta.model.Overtime;

public interface IOvertimeRepo extends CrudRepository<Overtime, Long>{

	boolean existsByEmployeeAndDate(Employee newEmployee, LocalDate now);

	ArrayList<Overtime> findByEmployeeEid(long eid);

	@Query(value = "SELECT * FROM overtime_table WHERE eid = :eid AND date between :min AND :max", nativeQuery = true)
	ArrayList<Overtime> findByEmployeeIdThisMonth(@Param("eid") long eid, @Param("min") LocalDate min, @Param("max") LocalDate max);
}
