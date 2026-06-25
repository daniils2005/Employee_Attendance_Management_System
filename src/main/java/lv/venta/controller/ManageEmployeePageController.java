package lv.venta.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lv.venta.model.Department;
import lv.venta.model.Employee;
import lv.venta.model.Position;
import lv.venta.model.enums.Status;
import lv.venta.repo.IDepartmentRepo;
import lv.venta.repo.IEmployeeRepo;
import lv.venta.repo.IPositionRepo;
import lv.venta.service.IDepartmentCRUDService;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IPositionCRUDService;

@Controller
public class ManageEmployeePageController {
	
	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IDepartmentCRUDService departmentCRUDService;
	
	@Autowired
	private IPositionCRUDService positionCRUDService;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	@Autowired
	private IEmployeeRepo employeeRepo;
	
	@Autowired
	private IDepartmentRepo departmentRepo;
	
	@Autowired
	private IPositionRepo positionRepo;
	
	@GetMapping("/manage/employee")
    public String getEmployees(@RequestParam(required = false) Long eid, @RequestParam(required = false) String surname, @RequestParam(required = false) Status status, @RequestParam(required = false) String position, @RequestParam(required = false) String department, Model model, RedirectAttributes redirectAttributes) {
    	try {
    		ArrayList<Employee> resultEmployees = new ArrayList<Employee>();
    		ArrayList<Department> resultDepartment = departmentCRUDService.selectAllDepartments();
    		ArrayList<Position> resultPosition = positionCRUDService.selectAllPositions();
			if(eid != null) {
				resultEmployees.add(employeeCRUDService.selectEmployeeById(eid));
			}
			else if(surname != null) {
				resultEmployees = generalService.findEmployeesBySurname(surname);
			}
			else if(status != null) {
				resultEmployees = employeeRepo.findByStatus(status);
			}
			else if(position != null) {
				resultEmployees = employeeRepo.findByPositionName(position);
			}
			else if(department != null) {
				resultEmployees = employeeRepo.findByDepartmentDepartmentName(department);
			} else {
				resultEmployees = employeeCRUDService.selectAllEmployees();
			}
			if(employeeRepo.count() != 0) {
     			ArrayList<Employee> employeeLastEid = employeeRepo.findTopByOrderByEidDesc();
     			model.addAttribute("lastEid", employeeLastEid.get(0).getEid() + 1);
     		}
			model.addAttribute("employees", resultEmployees);
			model.addAttribute("departments", resultDepartment);
			model.addAttribute("positions", resultPosition);
    		return "manage-employee-page";
    	}
    	catch(Exception e) {
   		 	redirectAttributes.addFlashAttribute("error", e.getMessage());
  			return "redirect:/manage/employee";
   	 	}
    }
    
    @PostMapping("/manage/employee/add")
    public String postEmployees(@RequestParam String name, @RequestParam String surname, @RequestParam String personCode, @RequestParam float hourlyRate, @RequestParam String position, @RequestParam String department, @RequestParam Status status, @RequestParam String email, @RequestParam String number, Model model, RedirectAttributes redirectAttributes) { 
    	try {
    		employeeCRUDService.insertNewEmployee(name, surname, personCode, number, email, hourlyRate, departmentRepo.findByDepartmentName(department), status, positionRepo.findByName(position));
    		return "redirect:/manage/employee";
    	} 
    	catch(Exception e) {
    		redirectAttributes.addFlashAttribute("error", e.getMessage());
      		return "redirect:/manage/employee";
    	}
    }
    
    @PostMapping("/manage/employee/update-or-delete")
    public String updateEmployees(@RequestParam long eid, @RequestParam String name, @RequestParam String surname, @RequestParam float hourlyRate, @RequestParam String position, @RequestParam String department, @RequestParam Status status, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String action, Model model, RedirectAttributes redirectAttributes) {
    	try {
    		if("save".equals(action)) {
    			employeeCRUDService.updateEmployeeById(eid, name, surname, phoneNumber, email, hourlyRate, departmentRepo.findByDepartmentName(department), status, positionRepo.findByName(position));
    		}
    	return "redirect:/manage/employee";
    	} 
    	catch(Exception e) {
    		redirectAttributes.addFlashAttribute("error", e.getMessage());
      		return "redirect:/manage/employee";
    	}
    }
}
