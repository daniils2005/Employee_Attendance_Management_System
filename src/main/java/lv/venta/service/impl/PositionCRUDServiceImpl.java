package lv.venta.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.Position;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.repo.IPositionRepo;
import lv.venta.service.IPositionCRUDService;

@Service
public class PositionCRUDServiceImpl implements IPositionCRUDService {
	
	@Autowired 
	private IPositionRepo positionRepo;
	
	@Autowired
	private IEmployeeRepo employeeRepo;

	
	
	@Override
	public ArrayList<Position> selectAllPositions() throws Exception {
		return(ArrayList<Position>)positionRepo.findAll();
	}
	
	@Override
	public Position selectPositionById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!positionRepo.existsById(id)) {
			throw new Exception("Position with id = " + id + " doesnt exist");
		}
		return positionRepo.findById(id).get();
	}
	
	@Override
	@Transactional
	public void deletePositionById(long id) throws Exception {
		if(id <= 0) {
			throw new Exception("id cant be negative or equal to 0");
		}
		if(!positionRepo.existsById(id)) {
			throw new Exception("Position with id = " + id + " doesnt exist");
		}
		Position positionForDeleting = positionRepo.findById(id).get();
		ArrayList<Employee> employeesFromPosition = employeeRepo.findByPosition(positionForDeleting);
		Position defaultPosition = positionRepo.findByName("Nav_minets");
		for(var employee : employeesFromPosition) {
			employee.setPosition(defaultPosition);
		}
		employeeRepo.saveAll(employeesFromPosition);
		positionRepo.delete(positionForDeleting);
	}
	
	@Override
	public void insertNewPosition(String positionName, String description) throws Exception {
		if(positionName == null || description == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(positionRepo.existsByName(positionName)) {
			throw new Exception(positionName + "  already exists");
		}
		Position newPosition = new Position(positionName, description);
		positionRepo.save(newPosition);
	}
	
	@Override
	public void updatePositionById(long id, String positionName, String description) throws Exception {
		if(id <= 0 || positionName == null || description == null) {
			throw new Exception("One or more input arguments are invalid");
		}
		if(!positionRepo.existsById(id)) {
			throw new Exception("Department with id = " + id + " doesn't exist");
		}

		Position positionForUpdating = positionRepo.findById(id).get();
		positionForUpdating.setName(positionName);
		positionForUpdating.setDescription(description);
		positionRepo.save(positionForUpdating);
	}
}
