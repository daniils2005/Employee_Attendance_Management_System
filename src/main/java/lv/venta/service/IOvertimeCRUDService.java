package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Employee;
import lv.venta.model.Overtime;

public interface IOvertimeCRUDService {
	
	public abstract ArrayList<Overtime> selectAllOvertimes() throws Exception;
	
	public abstract Overtime selectOvertimeById(long id) throws Exception;
	
	public abstract void deleteOvertimeById(long id) throws Exception;
	
	public abstract void insertNewOvertime(float newOvertimeHours, String newDescription, Employee newEmployee) throws Exception;
	
	public abstract void updateOvertimeById(long id, float newOvertimeHours, float newOvertimeRate, String newDescription, Employee newEmployee) throws Exception;
}
