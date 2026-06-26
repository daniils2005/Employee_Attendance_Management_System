package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import lv.venta.model.Attendance;
import lv.venta.model.Employee;
import lv.venta.repo.IAttendanceRepo;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.service.IAttendanceCRUDService;

@Service
public class AttendanceCRUDServiceImpl implements IAttendanceCRUDService {

	@Autowired
	private IAttendanceRepo attendanceRepo;
	
	@Autowired
	private IEmployeeRepo employeeRepo;
	
	@Override
	public ArrayList<Attendance> selectAllAttendances() throws Exception {
		ArrayList<Attendance> result = (ArrayList<Attendance>)attendanceRepo.findAll();
		return result;
	}

	@Override
	public Attendance selectAttendanceById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!attendanceRepo.existsById(id)) {
			throw new Exception("Attendance with id=" + id + " doesn't exist");
		}
		return attendanceRepo.findById(id).get();
	}

	@Override
	public void deleteAttendanceById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!attendanceRepo.existsById(id)) {
			throw new Exception("Attendance with id=" + id + " doesn't exist");
		}
		Attendance attendanceForDeleting = attendanceRepo.findById(id).get();
		attendanceRepo.delete(attendanceForDeleting);
	}

	@Override
	public void insertNewAttendance(float hoursWorked, Employee employee) throws Exception {
		if(hoursWorked < 0 || employee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!employeeRepo.existsByPersonCode(employee.getPersonCode())) {
			throw new Exception("Can't register an attendance for employee that doesn't exist");
		}
		float sum = hoursWorked;
		ArrayList<Attendance> attendancesTodayForEmployee = attendanceRepo.findAllByEmployeeEidAndWorkDate(employee.getEid(), LocalDate.now());
		for(var attendance : attendancesTodayForEmployee) {
			sum += attendance.getHoursWorked();
			if(sum > 8) {
				throw new Exception("Sum of hours for attendances today(" + LocalDate.now() + ") cannot exceed 8 hours");
			}
		}
		Attendance newAttendance = new Attendance(hoursWorked, employee);
		attendanceRepo.save(newAttendance);
	}
	
	@Override
	public void insertNewAttendanceWithDate(float hoursWorked, Employee employee, LocalDate date) throws Exception{
		if(hoursWorked < 0 || employee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!employeeRepo.existsByPersonCode(employee.getPersonCode())) {
			throw new Exception("Can't register an attendance for employee that doesn't exist");
		}
		float sum = hoursWorked;
		ArrayList<Attendance> attendancesTodayForEmployee = attendanceRepo.findAllByEmployeeEidAndWorkDate(employee.getEid(), LocalDate.now());
		for(var attendance : attendancesTodayForEmployee) {
			sum += attendance.getHoursWorked();
			if(sum > 8) {
				throw new Exception("Sum of hours for attendances today(" + LocalDate.now() + ") cannot exceed 8 hours");
			}
		}
		Attendance newAttendance = new Attendance(hoursWorked, employee);
		if(date == null) {
			newAttendance.setWorkDate(LocalDate.now());
		}
		else {
			newAttendance.setWorkDate(date);
		}
		attendanceRepo.save(newAttendance);
	}

	@Override
	public void updateAttendanceById(long id, float hoursWorked, Employee employee) throws Exception {
		if(id <= 0 || hoursWorked < 0 || employee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!attendanceRepo.existsById(id)) {
			throw new Exception("Attendance with id=" + id + " doesn't exist");
		}
		Attendance attendanceForUpdating = attendanceRepo.findById(id).get();
		attendanceForUpdating.setHoursWorked(hoursWorked);
		attendanceForUpdating.setEmployee(employee);
		attendanceRepo.save(attendanceForUpdating);
	}
	
	public void updateAttendanceByIdWithDate(long id, float hoursWorked, Employee employee, LocalDate date) throws Exception{
		if(id <= 0 || hoursWorked < 0 || employee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!attendanceRepo.existsById(id)) {
			throw new Exception("Attendance with id=" + id + " doesn't exist");
		}
		
		Attendance attendanceForUpdating = attendanceRepo.findById(id).get();
		attendanceForUpdating.setHoursWorked(hoursWorked);
		attendanceForUpdating.setEmployee(employee);
		attendanceForUpdating.setWorkDate(date);
		attendanceRepo.save(attendanceForUpdating);
		
	}

}
