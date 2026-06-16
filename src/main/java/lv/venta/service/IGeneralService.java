package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Attendance;
import lv.venta.model.Overtime;
import lv.venta.model.Vacation;

public interface IGeneralService {
	
	public abstract ArrayList<Attendance> selectAllAttendancesForEmployeeId(long eid) throws Exception;
	
	public abstract ArrayList<Attendance> selectAllAttendancesForToday() throws Exception;
	
	public abstract ArrayList<Overtime> selectAllOvertimesForEmployeeId(long eid) throws Exception;
	
	public abstract ArrayList<Vacation> selectAllVacationsForEmployeeId(long eid) throws Exception;
	
	public abstract ArrayList<Attendance> selectAllAttendancesThisMonthForEmployeeId(long eid) throws Exception;
	
	public abstract ArrayList<Overtime> selectAllOvertimesThisMonthForEmployeeId(long eid) throws Exception;
	
	public abstract float calculateSalaryThisMonthForEmployeeId(long eid) throws Exception;
	
	public ArrayList<Attendance> selectAllAttendanceByEmployeeId(long id) throws Exception;
}
