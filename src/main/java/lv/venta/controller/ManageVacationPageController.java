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
import lv.venta.model.Vacation;
import lv.venta.model.enums.RequestStatus;
import lv.venta.repo.IVacationRepo;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IVacationCRUDService;

@Controller
public class ManageVacationPageController {

	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IVacationCRUDService vacationCRUDService;
	
	@Autowired
	private IVacationRepo vacationRepo;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	@GetMapping("/manage/vacation")
    public String getVacationByEmpoyees(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model, RedirectAttributes redirectAttributes) {
    	try {
    		ArrayList<Vacation> vacation;
     		if (id != null && date != null) {
     			vacation = generalService.findByEmployeeEidAndStartDate(id, date);
 			}
     		else if (id != null) {
 				vacation = generalService.selectAllVacationsForEmployeeId(id);
     		}
 			else if (date != null) {
 				vacation = vacationRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);
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
     
     		if (vacationRepo.count() != 0) {
     			ArrayList<Vacation> vacationLastVid = vacationRepo.findTopByOrderByVidDesc();
     			model.addAttribute("lastVid", vacationLastVid.get(0).getVid() + 1);
     		}
     		model.addAttribute("vacations", vacation);
     		return "manage-vacation-page";
    	} 
    	catch(Exception e) {
     		redirectAttributes.addFlashAttribute("error", e.getMessage());
     		return "redirect:/manage/vacation";
    	}
   }
    
    @PostMapping("/manage/vacation/add")
    public String addVacationByEmpoyees(@RequestParam(required = false) Long eid, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate, @RequestParam RequestStatus status, Model model, RedirectAttributes redirectAttributes) { 
    	try {
    		Employee employee = employeeCRUDService.selectEmployeeById(eid);
    		vacationCRUDService.insertNewVacation(startDate, endDate ,employee, status);
    		return "redirect:/manage/vacation";
    	} 
    	catch(Exception e) {
    		redirectAttributes.addFlashAttribute("error", e.getMessage());
      		return "redirect:/manage/vacation";
    	}
   }
    
   @PostMapping("/manage/vacation/update-or-delete")
   public String updateVacationByEmpoyees(@RequestParam(required = false) Long eid, @RequestParam(required = false) Long vid, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate, @RequestParam RequestStatus status, @RequestParam(required = false) String action, Model model, RedirectAttributes redirectAttributes) {
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
    		redirectAttributes.addFlashAttribute("error", e.getMessage());
      		return "redirect:/manage/vacation";
    	}
    }
}
