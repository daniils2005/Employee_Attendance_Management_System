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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lv.venta.model.Employee;
import lv.venta.model.Overtime;
import lv.venta.model.enums.RequestStatus;
import lv.venta.repo.IOvertimeRepo;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IOvertimeCRUDService;

@Controller
public class ManageOvertimePageController {

	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IOvertimeRepo overtimeRepo;
	
	@Autowired
	private IOvertimeCRUDService overtimeCRUDService;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	@GetMapping("/manage/overtime")
    public String getOvertimeByEmployees(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model, RedirectAttributes redirectAttributes) {
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
   		 	redirectAttributes.addFlashAttribute("error", e.getMessage());
  			return "redirect:/manage/overtime";
   	 	}
    }
    
    @PostMapping("/manage/overtime/add")
    public String addOvertimeByEmployees(@RequestParam(required = false) Long eid, @RequestParam float overtimeHours, @RequestParam float overtimeRate, @RequestParam(required = false) String description, @RequestParam(required = false) LocalDate date, @RequestParam RequestStatus status, Model model, RedirectAttributes redirectAttributes) { 
    	try {
    		Employee employee = employeeCRUDService.selectEmployeeById(eid);
    		overtimeCRUDService.insertNewOvertimeWithDateAndStatusAndOvertimeRate(overtimeHours, description ,employee, date, status, overtimeRate);
    		return "redirect:/manage/overtime";
    	} 
    	catch(Exception e) {
    		redirectAttributes.addFlashAttribute("error", e.getMessage());
    		return "redirect:/manage/overtime";
    	}
    }

    @PostMapping("/manage/overtime/update-or-delete")
    public String updateOvertimeByEmployees(@RequestParam(required = false) Long eid, @RequestParam(required = false) Long oid, @RequestParam Float overtimeHours, @RequestParam(required = false) Float overtimeRate, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String description, @RequestParam(required = false) String action, @RequestParam(required = false) RequestStatus status, Model model, RedirectAttributes redirectAttributes) {
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
   		 	redirectAttributes.addFlashAttribute("error", e.getMessage());
  			return "redirect:/manage/overtime";
    	}
    }
}
