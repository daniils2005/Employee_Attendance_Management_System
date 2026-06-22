package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Position;

public interface IPositionCRUDService {
	
	public abstract ArrayList<Position> selectAllPositions() throws Exception;
	
	public abstract Position selectPositionById(long id) throws Exception;
	
	public abstract void insertNewPosition(String newName, String newDepartment) throws Exception;
	
	public abstract void updatePositionById(long id, String newName, String newDescription) throws Exception;
	
	public abstract void deletePositionById(long id) throws Exception;
}
