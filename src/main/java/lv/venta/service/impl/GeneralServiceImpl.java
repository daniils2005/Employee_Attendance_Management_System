package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Attendance;
import lv.venta.model.Overtime;
import lv.venta.model.Vacation;
import lv.venta.repo.IAttendanceRepo;
import lv.venta.repo.IOvertimeRepo;
import lv.venta.repo.IVacationRepo;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;

@Service
public class GeneralServiceImpl implements IGeneralService {

	@Autowired
	private IAttendanceRepo attendanceRepo;
	
	@Autowired
	private IOvertimeRepo overtimeRepo;
	
	@Autowired
	private IVacationRepo vacationRepo;
	
	@Autowired
	private IEmployeeCRUDService employeeService;
	
	@Override
	public ArrayList<Attendance> selectAllAttendancesForEmployeeId(long eid) throws Exception {
		if(attendanceRepo.count() == 0) {
			throw new Exception("Attendance table is empty");
		}
		if(eid <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		ArrayList<Attendance> result = attendanceRepo.findByEmployeeEid(eid);
		if(result.isEmpty()) {
			throw new Exception("There are no records of attendance for employee id=" + eid);
		}
		return result;
	}

	@Override
	public ArrayList<Attendance> selectAllAttendancesForToday() throws Exception {
		if(attendanceRepo.count() == 0) {
			throw new Exception("Attendance table is empty");
		}
		ArrayList<Attendance> result = attendanceRepo.findByWorkDate(LocalDate.now());
		if(result.isEmpty()) {
			throw new Exception("There are no records of attendance for today");
		}
		return result;
	}

	@Override
	public ArrayList<Overtime> selectAllOvertimesForEmployeeId(long eid) throws Exception {
		if(overtimeRepo.count() == 0) {
			throw new Exception("Overtime table is empty");
		}
		if(eid <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		ArrayList<Overtime> result = overtimeRepo.findByEmployeeEid(eid);
		return result;
	}

	@Override
	public ArrayList<Vacation> selectAllVacationsForEmployeeId(long eid) throws Exception {
		if(vacationRepo.count() == 0) {
			throw new Exception("Vacation table is empty");
		}
		if(eid <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		ArrayList<Vacation> result = vacationRepo.findByEmployeeEid(eid);
		return result;
	}

	@Override
	public ArrayList<Attendance> selectAllAttendancesThisMonthForEmployeeId(long eid) throws Exception {
		if(attendanceRepo.count() == 0) {
			throw new Exception("Attendance table is empty");
		}
		if(eid <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		LocalDate today = LocalDate.now();
		ArrayList<Attendance> result = attendanceRepo.findByEmployeeIdThisMonth(eid, today.withDayOfMonth(1), today.withDayOfMonth(today.lengthOfMonth()));
		return result;
	}

	@Override
	public ArrayList<Overtime> selectAllOvertimesThisMonthForEmployeeId(long eid) throws Exception {
		if(overtimeRepo.count() == 0) {
			throw new Exception("Overtime table is empty");
		}
		if(eid <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		LocalDate today = LocalDate.now();
		ArrayList<Overtime> result = overtimeRepo.findByEmployeeIdThisMonth(eid, today.withDayOfMonth(1), today.withDayOfMonth(today.lengthOfMonth()));
		return result;
	}

	@Override
	public float calculateSalaryThisMonthForEmployeeId(long eid) throws Exception {
		if(attendanceRepo.count() == 0) {
			throw new Exception("Unable to calculate - attendance table is empty");
		}
		float result = 0;
		double employeeHourlyRate = employeeService.selectEmployeeById(eid).getHourlyRate();
		ArrayList<Overtime> overtimeList = new ArrayList<Overtime>();
		try {
			overtimeList = selectAllOvertimesThisMonthForEmployeeId(eid);
		} catch(Exception e) {
			
		}
		if(!overtimeList.isEmpty()) {
			for(var overtime : overtimeList) {
				if(overtime.getOvertimeRate() != null) {
					result += employeeHourlyRate * overtime.getOvertimeRate() * overtime.getOvertimeHours();
				}
			}
		}
		ArrayList<Attendance> attendanceList = selectAllAttendancesThisMonthForEmployeeId(eid);
		for(var attendance : attendanceList) {
			if(attendance.getHoursWorked() > 8) {
				result += employeeHourlyRate * 8;
			} else {
				result += employeeHourlyRate * attendance.getHoursWorked();
			}
		}
		return result;
	}

}
