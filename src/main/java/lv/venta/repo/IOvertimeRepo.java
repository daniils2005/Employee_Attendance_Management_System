package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Overtime;

public interface IOvertimeRepo extends CrudRepository<Overtime, Long>{

}
