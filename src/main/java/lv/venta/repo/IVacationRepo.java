package lv.venta.repo;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import lv.venta.model.Employee;
import lv.venta.model.Vacation;

public interface IVacationRepo extends CrudRepository<Vacation, Long>{

    @Query("SELECT COUNT(v) > 0 FROM Vacation v WHERE v.employee = :employee " + "AND (:startDate <= v.endDate AND :endDate >= v.startDate)")
    boolean existsOverlappingVacation(@Param("employee") Employee employee,  @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(v) > 0 FROM Vacation v WHERE v.employee = :employee " + "AND v.id != :id AND (:startDate <= v.endDate AND :endDate >= v.startDate)")
    boolean existsOverlappingVacationForUpdate(@Param("id") long id, @Param("employee") Employee employee, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	ArrayList<Vacation> findByEmployeeEid(long eid);

	ArrayList<Vacation> findTopByOrderByVidDesc();

	ArrayList<Vacation> findByStartDate(LocalDate date);

	ArrayList<Vacation> findByVid(Long id);

	ArrayList<Vacation> findByVidAndStartDate(Long id, LocalDate date);

	ArrayList<Vacation> findByEmployeeEidAndStartDate(Long id, LocalDate date);

	ArrayList<Vacation> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date, LocalDate date2);

}
