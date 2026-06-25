package lv.venta.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.model.Department;
import lv.venta.repo.IDepartmentRepo;
import lv.venta.service.IDepartmentCRUDService;

@Controller
public class ManageDepartmentPageController {
	
	@Autowired
	private IDepartmentRepo departmentRepo;
	
	@Autowired
	private IDepartmentCRUDService departmentCRUDService;
	
	private String errorPage = "error-page";
	private String errorMessage = "errorMessage";
	
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
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
	
    @PostMapping("/manage/department/add")
    public String addDepartment(@RequestParam String addDepartmentName, @RequestParam String description, Model model) { 
    	 try {
    		 departmentCRUDService.insertNewDepartment(addDepartmentName, description);
    		 return "redirect:/manage/department";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
 
    @PostMapping("/manage/department/update-or-delete")
    public String updateDepartment(@RequestParam String action, @RequestParam Long did, @RequestParam String departmentName, @RequestParam String description, Model model) {
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
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
}
