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

				Department defaultDepartment = new Department("Nav_minets", "Departaments nav bijis uzlikts vai tika nomainīts vai dzēsts");
				Department dep1 = new Department("IT", "It nodaļa priekš uzņemuma uzturēšanas");
				Department dep2 = new Department("Gramatvediba", "Grāmatvedības nodaļa");
				Department dep3 = new Department("Personala_nodala", "Personāla nodaļa");
				departRepo.saveAll(Arrays.asList(dep1, dep2, dep3, defaultDepartment));
				departRepo.save(dep1);

				Employee emp1 = new Employee("Janis", "Berzins", "123456-12345", "12345678","janis@gmail.com", 10, dep1, Status.Aktivs, Position.Programmetajs);
				Employee emp2 = new Employee("Ugis", "Andrums", "223456-12345", "12345679", "ugis@gmail.com", 12, dep1, Status.Aktivs, Position.DevOps_inzenieris);
				Employee emp3 = new Employee("Anna", "Kalnina", "323456-12345", "12345680", "anna@gmail.com", 11, dep2, Status.Aktivs, Position.Testetajs);
				Employee emp4 = new Employee("Peteris", "Ozols", "423456-12345", "12345681", "peteris@gmail.com", 14, dep1, Status.Aktivs, Position.Programmetajs);
				Employee emp5 = new Employee("Liga", "Liepa", "523456-12345", "12345682", "liga@gmail.com", 13, dep3, Status.Aktivs, Position.DevOps_inzenieris);
				Employee emp6 = new Employee("Roberts", "Krasts", "623456-12345", "12345683", "roberts@gmail.com", 15, dep1, Status.Aktivs, Position.DevOps_inzenieris);
				Employee emp7 = new Employee("Elina", "Briede", "723456-12345", "12345684", "elina@gmail.com", 9, dep2, Status.Aktivs, Position.Sistemu_arhitekts);
				Employee emp8 = new Employee("Martins", "Egle", "823456-12345", "12345685", "martins@gmail.com", 10, dep3, Status.Aktivs, Position.Datu_analitikis);
				Employee emp9 = new Employee("Kristaps", "Vilks", "923456-12345", "12345686", "kristaps@gmail.com", 16, dep1, Status.Neaktivs, Position.Programmetajs);
				Employee emp10 = new Employee("Laura", "Ziedina", "023456-12345", "12345687", "laura@gmail.com", 11, dep2, Status.Aktivs, Position.Programmetajs);
				empRepo.saveAll(Arrays.asList(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10));
				
				Authority auth1 = new Authority("ADMIN");
				Authority auth2 = new Authority("USER");
				authorityRepo.saveAll(Arrays.asList(auth1, auth2));
				PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
				
				userRepo.save(new User("Janis", encoder.encode("parole"), emp1, auth1));
				userRepo.save(new User("Ugis", encoder.encode("parole"), emp2, auth2));
				userRepo.save(new User("Anna", encoder.encode("parole"), emp3, auth2));
				userRepo.save(new User("Peteris", encoder.encode("parole"), emp4, auth2));
				userRepo.save(new User("Liga", encoder.encode("parole"), emp5, auth2));
				userRepo.save(new User("Roberts", encoder.encode("parole"), emp6, auth2));
				userRepo.save(new User("Elina", encoder.encode("parole"), emp7, auth2));
				userRepo.save(new User("Martins", encoder.encode("parole"), emp8, auth2));
				userRepo.save(new User("Kristaps", encoder.encode("parole"), emp9, auth2));
				userRepo.save(new User("Laura", encoder.encode("parole"), emp10, auth2));
				
				attendRepo.saveAll(Arrays.asList(
					    new Attendance(8, emp1, LocalDate.of(2025, 12, 1)),
					    new Attendance(7.5f, emp1, LocalDate.of(2025, 12, 2)),
					    new Attendance(9, emp1, LocalDate.of(2025, 12, 3)),
					    new Attendance(8, emp2, LocalDate.of(2025, 12, 1)),
					    new Attendance(10, emp2, LocalDate.of(2025, 12, 2)),
					    new Attendance(8.5f, emp2, LocalDate.of(2025, 12, 3)),
					    new Attendance(8, emp3, LocalDate.of(2025, 12, 1)),
					    new Attendance(6.5f, emp3, LocalDate.of(2025, 12, 2)),
					    new Attendance(8, emp3, LocalDate.of(2025, 12, 3)),
					    new Attendance(9, emp4, LocalDate.of(2025, 12, 1)),
					    new Attendance(8, emp4, LocalDate.of(2025, 12, 2)),
					    new Attendance(7, emp5, LocalDate.of(2025, 12, 1)),
					    new Attendance(8, emp5, LocalDate.of(2025, 12, 2)),
					    new Attendance(10, emp6, LocalDate.of(2025, 12, 1)),
					    new Attendance(9, emp6, LocalDate.of(2025, 12, 2)),
					    new Attendance(8, emp7, LocalDate.of(2025, 12, 1)),
					    new Attendance(8, emp8, LocalDate.of(2025, 12, 1)),
					    new Attendance(7.5f, emp9, LocalDate.of(2025, 12, 1)),
					    new Attendance(9, emp10, LocalDate.of(2025, 12, 1))
					));
				
				overRepo.saveAll(Arrays.asList(
				        new Overtime(2, "Server maintenance", emp2),
				        new Overtime(3, "Database migration", emp4),
				        new Overtime(1.5f, "Monthly report", emp3),
				        new Overtime(4, "Urgent recruitment", emp5),
				        new Overtime(2.5f, "System update", emp6),
				        new Overtime(1, "Documentation", emp10)
				));
				
				vacationRepo.saveAll(Arrays.asList(
				        new Vacation(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 14), emp1),
				        new Vacation(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 14), emp3),
				        new Vacation(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7), emp5),
				        new Vacation(LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 14), emp8)
				));
			}
		};
	}

}
