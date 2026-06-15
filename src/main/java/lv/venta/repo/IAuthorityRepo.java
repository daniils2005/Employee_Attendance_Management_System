package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Authority;

public interface IAuthorityRepo extends CrudRepository<Authority, Long>{

}
