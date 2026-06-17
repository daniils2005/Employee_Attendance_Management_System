package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Employee;
import lv.venta.model.Overtime;
import lv.venta.model.enums.RequestStatus;
import lv.venta.repo.IOvertimeRepo;
import lv.venta.service.IOvertimeCRUDService;

@Service
public class OvertimeCRUDServiceImpl implements IOvertimeCRUDService {

	@Autowired
	private IOvertimeRepo overtimeRepo;
	
	@Override
	public ArrayList<Overtime> selectAllOvertimes() throws Exception {
		if(overtimeRepo.count() == 0) {
			throw new Exception("Overtime table is empty");
		}
		ArrayList<Overtime> result = (ArrayList<Overtime>)overtimeRepo.findAll();
		return result;
	}

	@Override
	public Overtime selectOvertimeById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!overtimeRepo.existsById(id)) {
			throw new Exception("Overtime with id = " + id + " doesn't exist");
		}
		return overtimeRepo.findById(id).get();
	}

	@Override
	public void deleteOvertimeById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!overtimeRepo.existsById(id)) {
			throw new Exception("Overtime with id = " + id + " doesn't exist");
		}
		Overtime overtimeForDeleting = overtimeRepo.findById(id).get();
		overtimeRepo.delete(overtimeForDeleting);
	}

	@Override
	public void insertNewOvertime(float newOvertimeHours, String newDescription, Employee newEmployee) throws Exception {
		if(newOvertimeHours < 0 || newDescription == null || newEmployee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		LocalDate date = LocalDate.now();
		if(overtimeRepo.existsByEmployeeAndDate(newEmployee, date)) {
			throw new Exception("Overtime for employee with id=" + newEmployee.getEid() + " has already been registered for today(" + date + ")");
		}
		Overtime newOvertime = new Overtime(newOvertimeHours, newDescription, newEmployee);
		overtimeRepo.save(newOvertime);
	}

	@Override
	public void updateOvertimeById(long id, float newOvertimeHours, float newOvertimeRate, String newDescription, Employee newEmployee) throws Exception {
		if(id <= 0 || newOvertimeHours < 0 || newOvertimeRate < 0 || newDescription == null || newEmployee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!overtimeRepo.existsById(id)) {
			throw new Exception("Overtime with id = " + id + " doesn't exist");
		}
		Overtime overtimeForUpdating = overtimeRepo.findById(id).get();
		overtimeForUpdating.setOvertimeHours(newOvertimeHours);
		overtimeForUpdating.setOvertimeRate(newOvertimeRate);
		overtimeForUpdating.setDescription(newDescription);
		overtimeForUpdating.setEmployee(newEmployee);
		overtimeRepo.save(overtimeForUpdating);
	}
	public void insertNewOvertimeWithDateAndStatusAndOvertimeRate(float newOvertimeHours, String newDescription, Employee newEmployee, LocalDate date, RequestStatus status, float overtimeRate) throws Exception{
		if(newOvertimeHours < 0 || newDescription == null || newEmployee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		

		Overtime newOvertime = new Overtime(newOvertimeHours, newDescription, newEmployee);
		if(date == null) {
			newOvertime.setDate(LocalDate.now());
		}
		else {
			newOvertime.setDate(date);
		}
		newOvertime.setStatus(status);
		newOvertime.setOvertimeRate(overtimeRate);
		overtimeRepo.save(newOvertime);
		
	}
	
}
