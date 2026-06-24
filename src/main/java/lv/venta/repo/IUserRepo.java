package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Employee;
import lv.venta.model.User;

public interface IUserRepo extends CrudRepository<User, Long>{

	User findByEmployee(Employee employeeForDeleting);

	boolean existsByUsername(String newUsername);

	User findByUsername(String username);

	ArrayList<User> findTopByOrderByUidDesc();

	int countByAuthorityTitle(String authority);

	boolean existsByEmployeeEid(long eid);

}
