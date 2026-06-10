package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Attendance;

public interface IAttendanceRepo extends CrudRepository<Attendance, Long> {

}
