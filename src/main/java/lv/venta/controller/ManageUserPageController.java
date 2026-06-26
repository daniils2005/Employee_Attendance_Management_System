package lv.venta.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lv.venta.helper.MyUserDetails;
import lv.venta.model.Employee;
import lv.venta.model.User;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.repo.IUserRepo;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IUserCRUDService;

@Controller
public class ManageUserPageController {
	
	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IUserCRUDService userCRUDService;
	
	@Autowired
	private IUserRepo userRepo;
	
	@Autowired
	private IEmployeeRepo employeeRepo;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	private User getCurrentUser() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    MyUserDetails details = (MyUserDetails) auth.getPrincipal();
	    return details.getUser();
	}
	
	@GetMapping("/manage/user")
    public String getUsers(@RequestParam(required = false) Long uid, @RequestParam(required = false) String username, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model, RedirectAttributes redirectAttributes) {
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
   		 	redirectAttributes.addFlashAttribute("error", e.getMessage());
  			return "redirect:/manage/user";
   	 	}
    }
    
    @PostMapping("/manage/user/add")
    public String postUsers(@RequestParam long eid, @RequestParam String username, @RequestParam String authority, Model model, RedirectAttributes redirectAttributes) { 
    	 try {
    		 if(authority.equals("ADMIN") && userRepo.countByAuthorityTitle(authority) >= 3) {
    			 redirectAttributes.addFlashAttribute("error", "There can't be more than 3 ADMIN users.");
    			 return "redirect:/manage/user";
    		 }
    		 userCRUDService.insertNewUser(username, encoder.encode("parole"), employeeCRUDService.selectEmployeeById(eid), generalService.selectAuthorityByTitle(authority));
    		 return "redirect:/manage/user";
    	 } 
    	 catch(Exception e) {
    		 redirectAttributes.addFlashAttribute("error", e.getMessage());
   			 return "redirect:/manage/user";
    	 }
    }
    
    @PostMapping("/manage/user/update-or-delete")
    public String updateUsers(@RequestParam String action, @RequestParam long uid, @RequestParam long eid, @RequestParam String username, @RequestParam String employeeName, @RequestParam String employeeSurname, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String authority, Model model, RedirectAttributes redirectAttributes) {
    	 try {
    		if ("save".equals(action)) {
    			if(authority.equals("ADMIN") && userRepo.countByAuthorityTitle(authority) >= 3) {
       			 redirectAttributes.addFlashAttribute("error", "There can't be more than 3 ADMIN users.");
       			 return "redirect:/manage/user";
    			}
    			User user = userCRUDService.selectUserById(uid);
    			Employee employee = employeeCRUDService.selectEmployeeById(eid);
    			if(user.getEmployee().getEid() == eid) {
    				employeeCRUDService.updateEmployeeById(eid, employeeName, employeeSurname, phoneNumber, email, employee.getHourlyRate(), employee.getDepartment(), employee.getStatus(), employee.getPosition());
    			}
    			userCRUDService.updateUserById(uid, username, user.getPassword(), employee, generalService.selectAuthorityByTitle(authority));
    		 }
    		 else {
    			if(getCurrentUser().getUid() == uid) {
    				redirectAttributes.addFlashAttribute("error", "Can't delete your own account");
    	    		return "redirect:/manage/user";
    			}
     			User user = userCRUDService.selectUserById(uid);
    			Employee employee = user.getEmployee();
    			employee.setUser(null);
    			employeeRepo.save(employee);
    			userCRUDService.deleteUserById(uid);
    		 }
    		 return "redirect:/manage/user";
    	 } 
    	 catch(Exception e) {
    		 redirectAttributes.addFlashAttribute("error", e.getMessage());
   			 return "redirect:/manage/user";
    	 }
    }
    
    @PostMapping("/manage/user/reset-password")
    public String postUserResetPassword(@RequestParam long uid, @RequestParam String newPassword, Model model, RedirectAttributes redirectAttributes) {
    	try {
    		User userForUpdating = userCRUDService.selectUserById(uid);
	    	userCRUDService.updateUserById(userForUpdating.getUid(), userForUpdating.getUsername(), encoder.encode(newPassword), userForUpdating.getEmployee(), userForUpdating.getAuthority());
	    	return "redirect:/manage/user";
    	} catch(Exception e) {
   		 	redirectAttributes.addFlashAttribute("error", e.getMessage());
  			return "redirect:/manage/user";
    	}
    }
}
