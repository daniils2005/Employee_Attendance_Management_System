package lv.venta.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.helper.MyUserDetails;
import lv.venta.model.User;
import lv.venta.service.IAttendanceCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IOvertimeCRUDService;
import lv.venta.service.IVacationCRUDService;

@Controller
public class GeneralServiceController {

	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IAttendanceCRUDService attendanceCRUDService;
	
	@Autowired
	private IOvertimeCRUDService overtimeCRUDService;
	
	@Autowired
	private IVacationCRUDService vacationCRUDService;
	
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
    @GetMapping("/manage/attendance")
    public String attendanceByEmployee(@RequestParam(required = false) Long id, Model model) {
        try {

            if (id != null) {
                model.addAttribute("attendances",
                        generalService.selectAllAttendanceByEmployeeId(id));
            } else {
                model.addAttribute("attendances",
                        attendanceCRUDService.selectAllAttendances());
            }

            return "manage-attendance-page";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
    
    
    
}
