package lv.venta.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;

import lv.venta.model.Employee;
import lv.venta.model.Vacation;
import lv.venta.service.IVacationCRUDService;

public class VacationCRUDServiceImpl implements IVacationCRUDService {

	@Override
	public ArrayList<Vacation> selectAllVacations() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vacation selectVacationById(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteVacationById(long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertNewVacation(LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateVacationById(long id, LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
