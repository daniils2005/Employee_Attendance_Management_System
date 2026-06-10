package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Vacation;

public interface IVacationRepo extends CrudRepository<Vacation, Long>{

}
