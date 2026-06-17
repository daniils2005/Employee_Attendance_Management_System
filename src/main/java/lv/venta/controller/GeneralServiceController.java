package lv.venta.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.helper.MyUserDetails;
import lv.venta.model.Attendance;
import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.Overtime;
import lv.venta.model.User;
import lv.venta.model.enums.Position;
import lv.venta.model.enums.RequestStatus;
import lv.venta.model.enums.Status;
import lv.venta.service.IAttendanceCRUDService;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IOvertimeCRUDService;
import lv.venta.service.IUserCRUDService;
import lv.venta.service.IVacationCRUDService;

@Controller
public class GeneralServiceController {

	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IAttendanceCRUDService attendanceCRUDService;
	
	@Autowired
	private IOvertimeCRUDService overtimeCRUDService;
	
	@Autowired
	private IVacationCRUDService vacationCRUDService;
	
	@Autowired
	private IUserCRUDService userCRUDService;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	
	private User getCurrentUser() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    MyUserDetails details = (MyUserDetails) auth.getPrincipal();
	    return details.getUser();
	}
	
    @GetMapping("/attendance")
    public String getMyAttendancePage(Model model) {
        User currentUser = getCurrentUser();
        try {
        	model.addAttribute("attendances", generalService.selectAllAttendancesThisMonthForEmployeeId(currentUser.getEmployee().getEid()));
        	model.addAttribute("dateToday", LocalDate.now());
        	model.addAttribute("salary", generalService.calculateSalaryThisMonthForEmployeeId(currentUser.getEmployee().getEid()));
        	return "my-attendance-page";
        } catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
        }
    }
    
    @PostMapping("/attendance")
    public String postMyAttendancePage(@RequestParam float hoursWorked, Model model) {
    	 User currentUser = getCurrentUser();
    	 try {
    		 attendanceCRUDService.insertNewAttendance(hoursWorked, currentUser.getEmployee());
    		 return "redirect:/attendance";
    	 } catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @GetMapping("/overtime")
    public String getMyOvertimePage(Model model) {
        User currentUser = getCurrentUser();
        try {
        	model.addAttribute("overtimes", generalService.selectAllOvertimesThisMonthForEmployeeId(currentUser.getEmployee().getEid()));
        	model.addAttribute("dateToday", LocalDate.now());
        	return "my-overtime-page";
        } catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
        }
    }
    
    @PostMapping("/overtime")
    public String postMyOvertimePage(@RequestParam float overtimeHours, @RequestParam String description, Model model) {
    	 User currentUser = getCurrentUser();
    	 try {
    		 overtimeCRUDService.insertNewOvertime(overtimeHours, description, currentUser.getEmployee());
    		 return "redirect:/overtime";
    	 } catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @GetMapping("/vacation")
    public String getMyVacationPage(Model model) {
        User currentUser = getCurrentUser();
        try {
        	model.addAttribute("vacations", generalService.selectAllVacationsForEmployeeId(currentUser.getEmployee().getEid()));
        	model.addAttribute("dateToday", LocalDate.now());
        	return "my-vacation-page";
        } catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
        }
    }
    
    @PostMapping("/vacation")
    public String postMyVacationPage(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Model model) {
    	 User currentUser = getCurrentUser();
    	 try {
    		 vacationCRUDService.insertNewVacation(startDate, endDate, currentUser.getEmployee());
    		 return "redirect:/vacation";
    	 } catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @GetMapping("/account")
    public String getMyUserAccountPage(Model model) {
        try {
        	User currentUser = userCRUDService.selectUserById(getCurrentUser().getUid());
        	model.addAttribute("user", currentUser);
        	return "my-account-page";
        } catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
        }
    }
    
    @PostMapping("/account/change-username")
    public String postAccountChangeUsername(@RequestParam String newUsername, Model model) {
    	try {
    		User currentUser = userCRUDService.selectUserById(getCurrentUser().getUid());
    		userCRUDService.updateUserById(currentUser.getUid(), newUsername, currentUser.getPassword(), currentUser.getEmployee(), currentUser.getAuthority());
    		return "redirect:/account";
    	} catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
    	}
    }
    
    @PostMapping("/account/change-name")
    public String postAccountChangeName(@RequestParam String newName, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), newName, currentEmployee.getSurname(), currentEmployee.getNumber(), currentEmployee.getEmail(), currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return "redirect:/account";
    	} catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
    	}
    }
    
    @PostMapping("/account/change-surname")
    public String postAccountChangeSurname(@RequestParam String newSurname, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), currentEmployee.getName(), newSurname, currentEmployee.getNumber(), currentEmployee.getEmail(), currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return "redirect:/account";
    	} catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
    	}
    }
    
    @PostMapping("/account/change-number")
    public String postAccountChangeNumber(@RequestParam String newNumber, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), currentEmployee.getName(), currentEmployee.getSurname(), newNumber, currentEmployee.getEmail(), currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return "redirect:/account";
    	} catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
    	}
    }
    
    @PostMapping("/account/change-email")
    public String postAccountChangeEmail(@RequestParam String newEmail, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), currentEmployee.getName(), currentEmployee.getSurname(), currentEmployee.getNumber(), newEmail, currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return "redirect:/account";
    	} catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
    	}
    }
    
    @PostMapping("/account/change-password")
    public String postAccountChangePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, Model model) {
    	try { 
    		User currentUser = userCRUDService.selectUserById(getCurrentUser().getUid());
	    	if(!encoder.matches(currentPassword, currentUser.getPassword())) {
	    		model.addAttribute("errorMessage", "Current password doesn't match");
	    		return "error-page";
	    	}
	    	if(!newPassword.equals(confirmPassword)) {
	    		model.addAttribute("errorMessage", "Confirm password doesn't match new password");
	    		return "error-page";
	    	}
	    	if(encoder.matches(newPassword, currentUser.getPassword())) {
	    	    model.addAttribute("errorMessage", "New password must be different from current password");
	    	    return "error-page";
	    	}
	    	userCRUDService.updateUserById(currentUser.getUid(), currentUser.getUsername(), encoder.encode(newPassword), currentUser.getEmployee(), currentUser.getAuthority());
	    	return "redirect:/logout";
    	} catch(Exception e) {
        	model.addAttribute("errorMessage", e.getMessage());
        	return "error-page";
    	}
    }
    
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
	
			model.addAttribute("attendances", attendances);

			return "manage-attendance-page";
    	}   
        catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
    @PostMapping("/manage/attendance")
    public String postAttendanceByEmployee(@RequestParam(required = false) Long eid, @RequestParam float hoursWorked, @RequestParam(required = false) LocalDate date, Model model) {
    	 
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
    
    
    
    
    @GetMapping("/manage/overtime")
    public String getOvertimeByEmployees(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, Model model) {
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

		 
			if ("nameAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(a -> a.getEmployee().getName()));
			}
			else if ("nameDesc".equals(sort)) {
				overtime.sort(Comparator.comparing((Overtime a) -> a.getEmployee().getName()).reversed());
			}
			else if ("idAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(a -> a.getEmployee().getEid()));
			}
			else if ("idDesc".equals(sort)) {
				overtime.sort(Comparator.comparing((Overtime a) -> a.getEmployee().getEid()).reversed());
			}
			else if ("dateAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getDate));
			}
			else if ("dateDesc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getDate).reversed());
			}
			else if ("overHoursAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeHours));
			}
			else if ("overHoursDesc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeHours).reversed());
			}
			else if ("overHoursRateAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeRate,Comparator.nullsLast(Float::compareTo)));
			}
			else if ("overHoursRateDesc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeRate,Comparator.nullsLast(Float::compareTo)).reversed());
			}
			else if ("statusSort".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getStatus,Comparator.nullsLast(Comparator.naturalOrder())).reversed());
			}

    		model.addAttribute("overtimes", overtime);
    		return "manage-overtime-page";
    	}
    	catch(Exception e) {
   		 model.addAttribute("errorMessage", e.getMessage());
   		 return "error-page";
   	 	}
    }
    @PostMapping("/manage/overtime")
    public String postOvertimeByEmployees(@RequestParam(required = false) Long id, @RequestParam float overtimeHours, @RequestParam float overtimeRate, @RequestParam(required = false) String description, @RequestParam(required = false) LocalDate date, @RequestParam RequestStatus status, Model model) {
    	 
    	 try {
    		 Employee employee = employeeCRUDService.selectEmployeeById(id);
    		 overtimeCRUDService.insertNewOvertimeWithDateAndStatusAndOvertimeRate(overtimeHours, description ,employee, date, status, overtimeRate);
    		 return "redirect:/manage/overtime";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
 
   
}
