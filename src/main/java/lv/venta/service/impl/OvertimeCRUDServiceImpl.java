package lv.venta.service.impl;

import java.util.ArrayList;

import lv.venta.model.Employee;
import lv.venta.model.Overtime;
import lv.venta.service.IOvertimeCRUDService;

public class OvertimeCRUDServiceImpl implements IOvertimeCRUDService {

	@Override
	public ArrayList<Overtime> selectAllOvertimes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Overtime selectOvertimeById(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteOvertimeById(long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertNewOvertime(float newOvertimeHours, float newOvertimeRate, String newDescription,
			Employee newEmployee) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOvertimeById(long id, float newOvertimeHours, float newOvertimeRate, String newDescription,
			Employee newEmployee) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
