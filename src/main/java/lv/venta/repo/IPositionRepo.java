package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Position;

public interface IPositionRepo extends CrudRepository<Position, Long>{

	Position findByName(String string);

	boolean existsByName(String positionName);

}
