package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Attendance;
import lv.venta.model.Employee;

public interface IAttendanceCRUDService {

	public abstract ArrayList<Attendance> selectAllAttendances() throws Exception;
	
	public abstract Attendance selectAttendanceById(long id) throws Exception;
	
	public abstract void deleteAttendanceById(long id) throws Exception;
	
	public abstract void insertNewAttendance(float hoursWorked, Employee employee) throws Exception;
	
	public abstract void updateAttendanceById(long id, float hoursWorked) throws Exception;
}
