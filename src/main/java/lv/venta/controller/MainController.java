package lv.venta.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
	
    @GetMapping("/home")
    public String getHomePage(Model model, Principal principal) {
    	model.addAttribute("username", principal.getName());
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
    		return "admin-home-page";
    	}
    	return "home-page";
    }
    
    
}