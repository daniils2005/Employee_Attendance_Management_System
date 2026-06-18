package lv.venta.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lv.venta.helper.MyUserDetails;
import lv.venta.model.Employee;
import lv.venta.model.User;
import lv.venta.service.IAttendanceCRUDService;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IOvertimeCRUDService;
import lv.venta.service.IUserCRUDService;
import lv.venta.service.IVacationCRUDService;

@Controller
public class MyController {

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
	
	private String dateToday = "dateToday";
	private String errorPage = "error-page";
	private String errorMessage = "errorMessage";
	private String redirectAccount = "redirect:/account";
	
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
        	model.addAttribute(dateToday, LocalDate.now());
        	model.addAttribute("salary", generalService.calculateSalaryThisMonthForEmployeeId(currentUser.getEmployee().getEid()));
        	return "my-attendance-page";
        } catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
        }
    }
    
    @PostMapping("/attendance")
    public String postMyAttendancePage(@RequestParam float hoursWorked, Model model) {
    	 User currentUser = getCurrentUser();
    	 try {
    		 attendanceCRUDService.insertNewAttendance(hoursWorked, currentUser.getEmployee());
    		 return "redirect:/attendance";
    	 } catch(Exception e) {
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
    
    @GetMapping("/overtime")
    public String getMyOvertimePage(Model model) {
        User currentUser = getCurrentUser();
        try {
        	model.addAttribute("overtimes", generalService.selectAllOvertimesThisMonthForEmployeeId(currentUser.getEmployee().getEid()));
        	model.addAttribute(dateToday, LocalDate.now());
        	return "my-overtime-page";
        } catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
        }
    }
    
    @PostMapping("/overtime")
    public String postMyOvertimePage(@RequestParam float overtimeHours, @RequestParam String description, Model model) {
    	 User currentUser = getCurrentUser();
    	 try {
    		 overtimeCRUDService.insertNewOvertime(overtimeHours, description, currentUser.getEmployee());
    		 return "redirect:/overtime";
    	 } catch(Exception e) {
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
    
    @GetMapping("/vacation")
    public String getMyVacationPage(Model model) {
        User currentUser = getCurrentUser();
        try {
        	model.addAttribute("vacations", generalService.selectAllVacationsForEmployeeId(currentUser.getEmployee().getEid()));
        	model.addAttribute(dateToday, LocalDate.now());
        	return "my-vacation-page";
        } catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
        }
    }
    
    @PostMapping("/vacation")
    public String postMyVacationPage(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Model model) {
    	 User currentUser = getCurrentUser();
    	 try {
    		 vacationCRUDService.insertNewVacation(startDate, endDate, currentUser.getEmployee());
    		 return "redirect:/vacation";
    	 } catch(Exception e) {
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
    
    @GetMapping("/account")
    public String getMyUserAccountPage(Model model) {
        try {
        	User currentUser = userCRUDService.selectUserById(getCurrentUser().getUid());
        	model.addAttribute("user", currentUser);
        	return "my-account-page";
        } catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
        }
    }
    
    @PostMapping("/account/change-username")
    public String postAccountChangeUsername(@RequestParam String newUsername, Model model) {
    	try {
    		User currentUser = userCRUDService.selectUserById(getCurrentUser().getUid());
    		userCRUDService.updateUserById(currentUser.getUid(), newUsername, currentUser.getPassword(), currentUser.getEmployee(), currentUser.getAuthority());
    		return "redirect:/account";
    	} catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
    	}
    }
    
    @PostMapping("/account/change-name")
    public String postAccountChangeName(@RequestParam String newName, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), newName, currentEmployee.getSurname(), currentEmployee.getNumber(), currentEmployee.getEmail(), currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return redirectAccount;
    	} catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
    	}
    }
    
    @PostMapping("/account/change-surname")
    public String postAccountChangeSurname(@RequestParam String newSurname, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), currentEmployee.getName(), newSurname, currentEmployee.getNumber(), currentEmployee.getEmail(), currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return redirectAccount;
    	} catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
    	}
    }
    
    @PostMapping("/account/change-number")
    public String postAccountChangeNumber(@RequestParam String newNumber, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), currentEmployee.getName(), currentEmployee.getSurname(), newNumber, currentEmployee.getEmail(), currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return redirectAccount;
    	} catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
    	}
    }
    
    @PostMapping("/account/change-email")
    public String postAccountChangeEmail(@RequestParam String newEmail, Model model) {
    	try {
    		Employee currentEmployee = employeeCRUDService.selectEmployeeById(getCurrentUser().getEmployee().getEid());
    		employeeCRUDService.updateEmployeeById(currentEmployee.getEid(), currentEmployee.getName(), currentEmployee.getSurname(), currentEmployee.getNumber(), newEmail, currentEmployee.getHourlyRate(), currentEmployee.getDepartment(), currentEmployee.getStatus(), currentEmployee.getPosition());
    		return redirectAccount;
    	} catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
    	}
    }
    
    @PostMapping("/account/change-password")
    public String postAccountChangePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, HttpServletRequest request, HttpServletResponse response, Model model) {
    	try { 
    		User currentUser = userCRUDService.selectUserById(getCurrentUser().getUid());
	    	if(!encoder.matches(currentPassword, currentUser.getPassword())) {
	    		model.addAttribute(errorMessage, "Current password doesn't match");
	    		return errorPage;
	    	}
	    	if(!newPassword.equals(confirmPassword)) {
	    		model.addAttribute(errorMessage, "Confirm password doesn't match new password");
	    		return errorPage;
	    	}
	    	if(encoder.matches(newPassword, currentUser.getPassword())) {
	    	    model.addAttribute(errorMessage, "New password must be different from current password");
	    	    return errorPage;
	    	}
	    	userCRUDService.updateUserById(currentUser.getUid(), currentUser.getUsername(), encoder.encode(newPassword), currentUser.getEmployee(), currentUser.getAuthority());
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    	new SecurityContextLogoutHandler().logout(
	    	        request,
	    	        response,
	    	        auth
	    	);
	    	return "redirect:/login?logout";
    	} catch(Exception e) {
        	model.addAttribute(errorMessage, e.getMessage());
        	return errorPage;
    	}
    }
}
