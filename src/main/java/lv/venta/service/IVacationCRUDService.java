package lv.venta.service;

import java.time.LocalDate;
import java.util.ArrayList;

import lv.venta.model.Employee;
import lv.venta.model.Vacation;
import lv.venta.model.enums.RequestStatus;

public interface IVacationCRUDService {

	public abstract ArrayList<Vacation> selectAllVacations() throws Exception;
	
	public abstract Vacation selectVacationById(long id) throws Exception;
	
	public abstract void deleteVacationById(long id) throws Exception;
	
	public abstract void insertNewVacation(LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee) throws Exception;
	public abstract void insertNewVacation(LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee, RequestStatus status) throws Exception;
	
	public abstract void updateVacationById(long id, LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee, RequestStatus status) throws Exception;
}
