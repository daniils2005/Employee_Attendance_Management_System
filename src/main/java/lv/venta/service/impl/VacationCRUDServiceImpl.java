package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Employee;
import lv.venta.model.Vacation;
import lv.venta.repo.IVacationRepo;
import lv.venta.service.IVacationCRUDService;

@Service
public class VacationCRUDServiceImpl implements IVacationCRUDService {

	@Autowired
	private IVacationRepo vacationRepo;
	
	@Override
	public ArrayList<Vacation> selectAllVacations() throws Exception {
		if(vacationRepo.count() == 0) {
			throw new Exception("Vacation table is empty");
		}
		ArrayList<Vacation> result = (ArrayList<Vacation>)vacationRepo.findAll();
		return result;
	}

	@Override
	public Vacation selectVacationById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!vacationRepo.existsById(id)) {
			throw new Exception("Vacation with id = " + id + " doesn't exist");
		}
		return vacationRepo.findById(id).get();
	}

	@Override
	public void deleteVacationById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!vacationRepo.existsById(id)) {
			throw new Exception("Vacation with id = " + id + " doesn't exist");
		}
		Vacation vacationForDeleting = vacationRepo.findById(id).get();
		vacationRepo.delete(vacationForDeleting);
	}

	@Override
	public void insertNewVacation(LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee) throws Exception {
		if(newStartDate == null || newEndDate == null || newEmployee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(newStartDate.isAfter(newEndDate)) {
			throw new Exception("Vacation start date can't be after the end date");
		}
		if(vacationRepo.existsOverlappingVacation(newEmployee, newStartDate, newEndDate)) {
			throw new Exception("Vacation for employee id=" + newEmployee.getEid() + " for " + newStartDate + "-" + newEndDate + " has already been registered");
		}
		Vacation newVacation = new Vacation(newStartDate, newEndDate, newEmployee);
		vacationRepo.save(newVacation);
	}

	@Override
	public void updateVacationById(long id, LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee) throws Exception {
		if(id <= 0 || newStartDate == null || newEndDate == null || newEmployee == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!vacationRepo.existsById(id)) {
			throw new Exception("Vacation with id = " + id + " doesn't exist");
		}
		if(newStartDate.isAfter(newEndDate)) {
			throw new Exception("Vacation start date can't be after the end date");
		}
		if(vacationRepo.existsOverlappingVacationForUpdate(id, newEmployee, newStartDate, newEndDate)) {
			throw new Exception("Another vacation in time period of " + newStartDate + "-" + newEndDate + " already exists");
		}
		Vacation vacationForUpdating = vacationRepo.findById(id).get();
		vacationForUpdating.setStartDate(newStartDate);
		vacationForUpdating.setEndDate(newEndDate);
		vacationForUpdating.setEmployee(newEmployee);
		vacationRepo.save(vacationForUpdating);
	}

}
