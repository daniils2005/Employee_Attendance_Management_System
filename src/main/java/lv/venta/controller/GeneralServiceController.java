package lv.venta.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.helper.MyUserDetails;
import lv.venta.model.User;
import lv.venta.service.IAttendanceCRUDService;
import lv.venta.service.IGeneralService;

@Controller
public class GeneralServiceController {

	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IAttendanceCRUDService attendanceCRUDService;
	
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
}
