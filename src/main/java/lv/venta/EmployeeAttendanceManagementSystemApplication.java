package lv.venta;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lv.venta.model.Attendance;
import lv.venta.model.Authority;
import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.Overtime;
import lv.venta.model.User;
import lv.venta.model.Vacation;
import lv.venta.model.enums.DepartmentName;
import lv.venta.model.enums.Position;
import lv.venta.model.enums.Status;
import lv.venta.repo.IAttendanceRepo;
import lv.venta.repo.IAuthorityRepo;
import lv.venta.repo.IDepartmentRepo;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.repo.IOvertimeRepo;
import lv.venta.repo.IUserRepo;
import lv.venta.repo.IVacationRepo;

@SpringBootApplication
public class EmployeeAttendanceManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeAttendanceManagementSystemApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner testRepo(IAttendanceRepo attendRepo, IDepartmentRepo departRepo, IEmployeeRepo empRepo, IOvertimeRepo overRepo, IUserRepo userRepo, IVacationRepo vacationRepo, IAuthorityRepo authorityRepo) {
		
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				Department dep1 = new Department(DepartmentName.IT, "It nodaļa priekš uzņemuma uzturēšanas");
				Department dep2 = new Department(DepartmentName.Gramatvediba, "Grāmatvedības nodaļa");
				Department dep3 = new Department(DepartmentName.Personala_nodala, "Personāla nodaļa");

				Department defaultDepartment = new Department(DepartmentName.Nav_minets, "Departaments nav bijis uzlikts vai tika nomainīts vai dzēsts");

				departRepo.saveAll(Arrays.asList(dep1, dep2, dep3, defaultDepartment));
				
				Employee emp1 = new Employee("Janis", "Berzins", "123456-12345", "12345678", "janis@gmail.com", 10, dep1, Status.Aktivs, Position.Programmetajs);
				Employee emp2 = new Employee("Ugis", "Andrums", "123456-12345", "12345679", "Ugis@gmail.com", 10, dep1, Status.Aktivs, Position.DevOps_inzenieris);
				empRepo.save(emp1);
				empRepo.save(emp2);
				
				Authority auth1 = new Authority("ADMIN");
				Authority auth2 = new Authority("USER");
				authorityRepo.saveAll(Arrays.asList(auth1, auth2));
				
				PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
				
				User user1 = new User("Janis", encoder.encode("parole"), emp1, auth1);
				User user2 = new User("Ugis", encoder.encode("parole"), emp2, auth2);
				userRepo.save(user1);
				userRepo.save(user2);
				
				Attendance att1 = new Attendance(8, emp1);
				Attendance att2 = new Attendance(9, emp1);
				Attendance att3 = new Attendance(8, emp1);
				Attendance att4 = new Attendance(10, emp2);
				Attendance att5 = new Attendance(11, emp2);
				att4.setWorkDate(LocalDate.of(2025, 12, 5));
				attendRepo.saveAll(Arrays.asList(att1, att2, att3, att4, att5));
				
				Overtime ov2 = new Overtime(2, "Overtime", emp2);
				Overtime ov3 = new Overtime(3, "Overtime", emp2);
				overRepo.saveAll(Arrays.asList(ov2, ov3));
				
				Vacation v1 = new Vacation(LocalDate.now(), LocalDate.now().plusWeeks(2), emp1);
				Vacation v2 = new Vacation(LocalDate.now(), LocalDate.now().plusWeeks(2), emp2);
				vacationRepo.save(v1);
				vacationRepo.save(v2);
			}
		};
	}

}
