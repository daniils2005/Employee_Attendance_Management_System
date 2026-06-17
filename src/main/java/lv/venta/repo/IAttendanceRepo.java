package lv.venta.repo;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import lv.venta.model.Attendance;

public interface IAttendanceRepo extends CrudRepository<Attendance, Long> {

	ArrayList<Attendance> findByEmployeeEid(long eid);

	ArrayList<Attendance> findByWorkDate(LocalDate now);

	@Query(value = "SELECT * FROM attendance_table WHERE eid = :eid AND work_date between :min AND :max", nativeQuery = true)
	ArrayList<Attendance> findByEmployeeIdThisMonth(@Param("eid") long eid, @Param("min") LocalDate min, @Param("max") LocalDate max);

	ArrayList<Attendance> findAllByOrderByEmployee_NameDesc();

	ArrayList<Attendance> findAllByOrderByEmployee_NameAsc();
}
