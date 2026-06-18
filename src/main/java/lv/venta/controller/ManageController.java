package lv.venta.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.model.Attendance;
import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.Overtime;

import lv.venta.model.Vacation;
import lv.venta.model.enums.DepartmentName;
import lv.venta.model.enums.RequestStatus;
import lv.venta.repo.IAttendanceRepo;
import lv.venta.repo.IDepartmentRepo;
import lv.venta.repo.IOvertimeRepo;
import lv.venta.repo.IVacationRepo;

import lv.venta.model.User;

import lv.venta.repo.IEmployeeRepo;
import lv.venta.repo.IUserRepo;

import lv.venta.service.IAttendanceCRUDService;
import lv.venta.service.IDepartmentCRUDService;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IOvertimeCRUDService;
import lv.venta.service.IVacationCRUDService;
import lv.venta.service.IUserCRUDService;

@Controller
public class ManageController {

	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IOvertimeRepo overtimeRepo;
	
	@Autowired
	private IAttendanceRepo attendanceRepo;
	
	@Autowired
	private IAttendanceCRUDService attendanceCRUDService;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	@Autowired
	private IOvertimeCRUDService overtimeCRUDService;
	
	@Autowired
	private IVacationCRUDService vacationCRUDService;
	
	@Autowired
	private IVacationRepo vacationRepo;
	
	@Autowired
	private IUserCRUDService userCRUDService;
	
	@Autowired
	private IUserRepo userRepo;
	
	@Autowired
	private IEmployeeRepo employeeRepo;
	
	@Autowired
	private IDepartmentCRUDService departmentCRUDService;
	
	@Autowired
	private IDepartmentRepo departmentRepo;
    @GetMapping("/manage/attendance")
    public String getAttendanceByEmployee(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model) {
    	try {  
    		ArrayList<Attendance> attendances;
			if (id != null && date != null ) {
				attendances = generalService.selectAllAttendanceByEmployeeIdAndDateMonth(id, date);
			}
			else if (id != null) {
				attendances = generalService.selectAllAttendancesForEmployeeId(id);
			}
			else if (date != null) {
				attendances = generalService.selectAllAttendanceByEmployeeDateMonth(date);
			}
			else {
				attendances = attendanceCRUDService.selectAllAttendances();
			}
		 
			if ("id".equals(sort) && "asc".equals(order)) {
				 attendances.sort(Comparator.comparing(a -> a.getEmployee().getEid())); 
			}
			else if ("id".equals(sort) && "desc".equals(order)) {
				 attendances.sort(Comparator.comparing(a -> ((Attendance) a).getEmployee().getEid()).reversed());
			}
			else if ("surname".equals(sort) && "asc".equals(order)) {
			    attendances.sort(Comparator.comparing(a -> a.getEmployee().getSurname()));
			}
			else if ("surname".equals(sort) && "desc".equals(order)) {
			    attendances.sort(Comparator.comparing(a -> ((Attendance) a).getEmployee().getSurname()).reversed());
			}
			else if ("date".equals(sort) && "asc".equals(order)) {
			    attendances.sort(Comparator.comparing(Attendance::getWorkDate));
			}
			else if ("date".equals(sort) && "desc".equals(order)) {
			    attendances.sort(Comparator.comparing(Attendance::getWorkDate).reversed());
			}

			if(attendanceRepo.count() != 0) {
    			ArrayList<Attendance> AttendanceLastAid = attendanceRepo.findTopByOrderByAidDesc();
    			model.addAttribute("lastAid", AttendanceLastAid.get(0).getAid() + 1);
    		}
			model.addAttribute("attendances", attendances);
			return "manage-attendance-page";
    	}   
        catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
    
    @PostMapping("/manage/attendance/add")
    public String addAttendanceByEmployee(@RequestParam(required = false) Long eid, @RequestParam float hoursWorked, @RequestParam(required = false) LocalDate date, Model model) {
    	 
    	 try {
    		 Employee employee = employeeCRUDService.selectEmployeeById(eid);
    		 attendanceCRUDService.insertNewAttendanceWithDate(hoursWorked, employee, date);
    		 return "redirect:/manage/attendance";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    @PostMapping("/manage/attendance/update-or-delete")
    public String updateAttendanceByEmployee(@RequestParam(required = false) Long eid, @RequestParam(required = false) Long aid, @RequestParam float hoursWorked, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String action, Model model) {
    	 try {
    		 if ("save".equals(action)) {
    			Employee employee = employeeCRUDService.selectEmployeeById(eid);
    			attendanceCRUDService.updateAttendanceByIdWithDate(aid, hoursWorked, employee, date);
    		 }
    		 else {
    			 attendanceCRUDService.deleteAttendanceById(aid);
    		 }
    		 
    		 return "redirect:/manage/attendance";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @GetMapping("/manage/overtime")
    public String getOvertimeByEmployees(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model) {
    	try {
    		ArrayList<Overtime> overtime;
    		if (id != null && date != null) {
				overtime = generalService.selectAllOvertimeByEmployeeIdAndDateMonth(id, date);
			}
			else if (id != null) {
				overtime = generalService.selectAllOvertimesForEmployeeId(id);
			}
			else if (date != null) {
				overtime = generalService.selectAllOvertimeByEmployeeDateMonth(date);
			}
			else {
				overtime =  overtimeCRUDService.selectAllOvertimes();
			}

    		if ("eid".equals(sort) && "asc".equals(order)) {
				 overtime.sort(Comparator.comparing(a -> a.getEmployee().getEid())); 
			}
			else if ("eid".equals(sort) && "desc".equals(order)) {
				overtime.sort(Comparator.comparing(a -> ( (Overtime) a).getEmployee().getEid()).reversed());
			}
			else if ("date".equals(sort) && "asc".equals(order)) {
				overtime.sort(Comparator.comparing(Overtime::getDate));
			}
			else if ("date".equals(sort) && "desc".equals(order)) {
				overtime.sort(Comparator.comparing(Overtime::getDate).reversed());
			}
			else if ("surname".equals(sort) && "asc".equals(order)) {
				 overtime.sort(Comparator.comparing(a -> a.getEmployee().getSurname())); 
			}
			else if ("surname".equals(sort) && "desc".equals(order)) {
				 overtime.sort(Comparator.comparing(a -> ((Overtime) a).getEmployee().getSurname()).reversed()); 
			}
			else if ("status".equals(sort) && "asc".equals(order)) {
				overtime.sort(Comparator.comparing(Overtime::getStatus,Comparator.nullsLast(Comparator.naturalOrder())));
			}
			else if ("status".equals(sort) && "desc".equals(order)) {
				overtime.sort(Comparator.comparing(Overtime::getStatus,Comparator.nullsLast(Comparator.naturalOrder())).reversed());
			}
    		if(overtimeRepo.count() != 0) {
    			ArrayList<Overtime> overtimeLastOid = overtimeRepo.findTopByOrderByOidDesc();
    			model.addAttribute("lastOid", overtimeLastOid.get(0).getOid() + 1);
    		}

    		
    		model.addAttribute("overtimes", overtime);
    		return "manage-overtime-page";
    	}
    	catch(Exception e) {
   		 model.addAttribute("errorMessage", e.getMessage());
   		 return "error-page";
   	 	}
    }
    
    @PostMapping("/manage/overtime/add")
    public String addOvertimeByEmployees(@RequestParam(required = false) Long eid, @RequestParam float overtimeHours, @RequestParam float overtimeRate, @RequestParam(required = false) String description, @RequestParam(required = false) LocalDate date, @RequestParam RequestStatus status, Model model) { 
    	 try {
    		 Employee employee = employeeCRUDService.selectEmployeeById(eid);
    		 overtimeCRUDService.insertNewOvertimeWithDateAndStatusAndOvertimeRate(overtimeHours, description ,employee, date, status, overtimeRate);
    		 
    		 return "redirect:/manage/overtime";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }

    @PostMapping("/manage/overtime/update-or-delete")
    public String updateOvertimeByEmployees(@RequestParam(required = false) Long eid, @RequestParam(required = false) Long oid, @RequestParam Float overtimeHours, @RequestParam(required = false) Float overtimeRate, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String description, @RequestParam(required = false) String action, @RequestParam(required = false) RequestStatus status, Model model) {
    	 try {
    		 if ("save".equals(action)) {
    			Employee employee = employeeCRUDService.selectEmployeeById(eid);
    			overtimeCRUDService.updateOvertimeById(oid, overtimeHours, overtimeRate, description, employee, status, date);
    		 }
    		 else {
    			 overtimeCRUDService.deleteOvertimeById(oid);
    		 }
    		 
    		 return "redirect:/manage/overtime";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }

    @GetMapping("/manage/vacation")
    public String getVacationByEmpoyees(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model) {
    	 try {
    		ArrayList<Vacation> vacation;
     		if (id != null && date != null) {
     			vacation = vacationRepo.findByVidAndStartDate(id, date);
 			}
 			else if (id != null) {
 				vacation = vacationRepo.findByVid(id);
 			}
 			else if (date != null) {
 				vacation = vacationRepo.findByStartDate(date);
 			}
 			else {
 				vacation =  vacationCRUDService.selectAllVacations();
 			}

     		if ("id".equals(sort) && "asc".equals(order)) {
     			vacation.sort(Comparator.comparing(a -> a.getEmployee().getEid())); 
 			}
 			else if ("id".equals(sort) && "desc".equals(order)) {
 				vacation.sort(Comparator.comparing(a -> ( (Vacation) a).getEmployee().getEid()).reversed());
 			}
 			else if ("date".equals(sort) && "asc".equals(order)) {
 				vacation.sort(Comparator.comparing(Vacation::getStartDate));
 			}
 			else if ("date".equals(sort) && "desc".equals(order)) {
 				vacation.sort(Comparator.comparing(Vacation::getStartDate).reversed());
 			}
 			else if ("active".equals(sort) && "asc".equals(order)) {
 				vacation.sort(Comparator.comparing(Vacation::isActive));
 			}
 			else if ("active".equals(sort) && "desc".equals(order)) {
 				vacation.sort(Comparator.comparing(Vacation::isActive).reversed());
 			}
 			else if ("status".equals(sort) && "asc".equals(order)) {
 				vacation.sort(Comparator.comparing(Vacation::getStatus,Comparator.nullsLast(Comparator.naturalOrder())));
 			}
 			else if ("status".equals(sort) && "desc".equals(order)) {
 				vacation.sort(Comparator.comparing(Vacation::getStatus,Comparator.nullsLast(Comparator.naturalOrder())).reversed());
 			}
     		
     		if(vacationRepo.count() != 0) {
     			ArrayList<Vacation> vacationLastVid = vacationRepo.findTopByOrderByVidDesc();
     			model.addAttribute("lastVid", vacationLastVid.get(0).getVid() + 1);
     		}

     		
     		model.addAttribute("vacations", vacation);
    		 return "manage-vacation-page";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @PostMapping("/manage/vacation/add")
    public String addVacationByEmpoyees(@RequestParam(required = false) Long eid, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate, @RequestParam RequestStatus status, Model model) { 
    	 try {
    		 Employee employee = employeeCRUDService.selectEmployeeById(eid);
    		 vacationCRUDService.insertNewVacation(startDate, endDate ,employee, status);
    		 
    		 return "redirect:/manage/vacation";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @PostMapping("/manage/vacation/update-or-delete")
    public String updateVacationByEmpoyees(@RequestParam(required = false) Long eid, @RequestParam(required = false) Long vid, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate, @RequestParam RequestStatus status, @RequestParam(required = false) String action, Model model) {
    	 try {
    		 if ("save".equals(action)) {
    			Employee employee = employeeCRUDService.selectEmployeeById(eid);
    			//long id, LocalDate newStartDate, LocalDate newEndDate, Employee newEmployee, boolean isActive, RequestStatus status
    			vacationCRUDService.updateVacationById(vid, startDate, endDate, employee, status);
    		 }
    		 else {
    			 vacationCRUDService.deleteVacationById(vid);
    		 }
    	 return "redirect:/manage/vacation";
    	 }
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @GetMapping("/manage/user")
    public String getUsers(@RequestParam(required = false) Long uid, @RequestParam(required = false) String username, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model) {
    	try {
    		ArrayList<User> resultUsers = new ArrayList<User>();
			if (uid != null) {
				resultUsers.add(userCRUDService.selectUserById(uid));
			}
			else if (username != null) {
				resultUsers.add(generalService.selectUserByUsername(username));
			}
			else {
				resultUsers = userCRUDService.selectAllUsers();
			}
			
			if("eid".equals(sort) && "asc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> a.getEmployee().getEid()));
			}
			else if("eid".equals(sort) && "desc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> ((User)a).getEmployee().getEid()).reversed());
			}
			else if("username".equals(sort) && "asc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> a.getUsername())); 
			}
			else if("username".equals(sort) && "desc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> ((User)a).getUsername()).reversed()); 
			}
			else if ("surname".equals(sort) && "asc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> a.getEmployee().getSurname())); 
			}
			else if("surname".equals(sort) && "desc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> ((User)a).getEmployee().getSurname()).reversed());
			}
			else if("authority".equals(sort) && "asc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> a.getAuthority().getTitle()));
			}
			else if("authority".equals(sort) && "desc".equals(order)) {
				resultUsers.sort(Comparator.comparing(a -> ((User)a).getAuthority().getTitle()).reversed());
			}
			if(userRepo.count() != 0) {
     			ArrayList<User> userLastUid = userRepo.findTopByOrderByUidDesc();
     			model.addAttribute("lastUid", userLastUid.get(0).getUid() + 1);
     		}
			model.addAttribute("users", resultUsers);
    		return "manage-user-page";
    	}
    	catch(Exception e) {
   		 	model.addAttribute("errorMessage", e.getMessage());
   		 	return "error-page";
   	 	}
    }
    
    @PostMapping("/manage/user/add")
    public String postUsers(@RequestParam long eid, @RequestParam String username, @RequestParam String authority, Model model) { 
    	 try {
    		 userCRUDService.insertNewUser(username, encoder.encode("parole"), employeeCRUDService.selectEmployeeById(eid), generalService.selectAuthorityByTitle(authority));
    		 return "redirect:/manage/user";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @PostMapping("/manage/user/update-or-delete")
    public String updateUsers(@RequestParam String action, @RequestParam long uid, @RequestParam long eid, @RequestParam String username, @RequestParam String employeeName, @RequestParam String employeeSurname, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String authority, Model model) {
    	 try {
    		if ("save".equals(action)) {
    			User user = userCRUDService.selectUserById(uid);
    			Employee employee = employeeCRUDService.selectEmployeeById(eid);
    			if(user.getEmployee().getEid() == eid) {
    				employeeCRUDService.updateEmployeeById(eid, employeeName, employeeSurname, phoneNumber, email, employee.getHourlyRate(), employee.getDepartment(), employee.getStatus(), employee.getPosition());
    			}
    			userCRUDService.updateUserById(uid, username, user.getPassword(), employee, generalService.selectAuthorityByTitle(authority));
    		 }
    		 else {
     			User user = userCRUDService.selectUserById(uid);
    			Employee employee = user.getEmployee();
    			employee.setUser(null);
    			employeeRepo.save(employee);
    			userCRUDService.deleteUserById(uid);
    		 }
    		 return "redirect:/manage/user";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    @GetMapping("/manage/department")
    public String getDepartment(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model) {
    	 try {
    		ArrayList<Department> department;
    		department = departmentCRUDService.selectAllDepartments();

     		if ("did".equals(sort) && "asc".equals(order)) {
     			department.sort(Comparator.comparing(a -> a.getDid())); 
 			}
 			else if ("did".equals(sort) && "desc".equals(order)) {
 				department.sort(Comparator.comparing(a -> ((Department) a).getDid()).reversed()); 
 			}
 			else if ("departmentName".equals(sort) && "asc".equals(order)) {
 				department.sort(Comparator.comparing(Department::getDepartmentName,Comparator.nullsLast(Comparator.naturalOrder())));
 	 			
 			}
 			else if ("departmentName".equals(sort) && "desc".equals(order)) {
 				department.sort(Comparator.comparing(Department::getDepartmentName,Comparator.nullsLast(Comparator.naturalOrder())).reversed()); 			}

     		
     		if(departmentRepo.count() != 0) {
     			ArrayList<Department> departmentLastDid = departmentRepo.findTopByOrderByDidDesc();
     			model.addAttribute("lastDid", departmentLastDid.get(0).getDid() + 1);
     		}

     		
     		model.addAttribute("departments", department);
     		return "manage-department-page";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    @PostMapping("/manage/department/add")
    public String addDepartment(@RequestParam DepartmentName departmentName, @RequestParam String description, Model model) { 
    	 try {
    		 departmentCRUDService.insertNewDepartment(departmentName, description);
    		 return "redirect:/manage/department";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @PostMapping("/manage/department/update-or-delete")
    public String updateDepartment(@RequestParam String action, @RequestParam Long did, @RequestParam DepartmentName departmentName, @RequestParam String description, Model model) {
    	 try {
    		 if ("save".equals(action)) {
    			departmentCRUDService.updateDepartmentById(did, departmentName, description);
    		 }
    		 else {
    			 departmentCRUDService.deleteDepartmentById(did);
    		 }
    	 return "redirect:/manage/department";
    	 }
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
}
