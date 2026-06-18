package lv.venta.service;

import java.time.LocalDate;
import java.util.ArrayList;

import lv.venta.model.Employee;
import lv.venta.model.Overtime;
import lv.venta.model.enums.RequestStatus;

public interface IOvertimeCRUDService {
	
	public abstract ArrayList<Overtime> selectAllOvertimes() throws Exception;
	
	public abstract Overtime selectOvertimeById(long id) throws Exception;
	
	public abstract void deleteOvertimeById(long id) throws Exception;
	
	public abstract void insertNewOvertime(float newOvertimeHours, String newDescription, Employee newEmployee) throws Exception;
	
	public abstract void insertNewOvertimeWithDateAndStatusAndOvertimeRate(float newOvertimeHours, String newDescription, Employee newEmployee, LocalDate date, RequestStatus status, float overtimeRate) throws Exception;
	
	public abstract void updateOvertimeById(long id, float newOvertimeHours, float newOvertimeRate, String newDescription, Employee newEmployee) throws Exception;
	public abstract void updateOvertimeById(long id, float newOvertimeHours, float newOvertimeRate, String newDescription, Employee newEmployee, RequestStatus status, LocalDate date) throws Exception;
}
