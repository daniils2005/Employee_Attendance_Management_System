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

import lv.venta.model.Attendance;
import lv.venta.model.Employee;
import lv.venta.repo.IAttendanceRepo;
import lv.venta.service.IAttendanceCRUDService;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;

@Controller
public class ManageAttendancePageController {
	
	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IAttendanceRepo attendanceRepo;
	
	@Autowired
	private IAttendanceCRUDService attendanceCRUDService;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	private String surnameGlobal = "surname";
	
    @GetMapping("/manage/attendance")
    public String getAttendanceByEmployee(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model, RedirectAttributes redirectAttributes) {
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
			else if (surnameGlobal.equals(sort) && "asc".equals(order)) {
			    attendances.sort(Comparator.comparing(a -> a.getEmployee().getSurname()));
			}
			else if (surnameGlobal.equals(sort) && "desc".equals(order)) {
			    attendances.sort(Comparator.comparing(a -> ((Attendance) a).getEmployee().getSurname()).reversed());
			}
			else if ("date".equals(sort) && "asc".equals(order)) {
			    attendances.sort(Comparator.comparing(Attendance::getWorkDate));
			}
			else if ("date".equals(sort) && "desc".equals(order)) {
			    attendances.sort(Comparator.comparing(Attendance::getWorkDate).reversed());
			}

			if(attendanceRepo.count() != 0) {
    			ArrayList<Attendance> AttendanceLastAid = attendanceRepo.findTopByOrderByAidDesc();
    			model.addAttribute("lastAid", AttendanceLastAid.get(0).getAid() + 1);
    		}
			model.addAttribute("attendances", attendances);
			return "manage-attendance-page";
    	}   
        catch (Exception e) {
   		 	redirectAttributes.addFlashAttribute("error", e.getMessage());
  			return "redirect:/manage/attendance";
        }
    }
    
    @PostMapping("/manage/attendance/add")
    public String addAttendanceByEmployee(@RequestParam(required = false) Long eid, @RequestParam float hoursWorked, @RequestParam(required = false) LocalDate date, Model model, RedirectAttributes redirectAttributes) {
    	try {
    		Employee employee = employeeCRUDService.selectEmployeeById(eid);
    		attendanceCRUDService.insertNewAttendanceWithDate(hoursWorked, employee, date);
    		return "redirect:/manage/attendance";
    	} 
    	catch(Exception e) {
   		 	redirectAttributes.addFlashAttribute("error", e.getMessage());
  			return "redirect:/manage/attendance";
    	}
    }
    
    @PostMapping("/manage/attendance/update-or-delete")
    public String updateAttendanceByEmployee(@RequestParam(required = false) Long eid, @RequestParam(required = false) Long aid, @RequestParam float hoursWorked, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String action, Model model, RedirectAttributes redirectAttributes) {
    	try {
    		if ("save".equals(action)) {
    			Employee employee = employeeCRUDService.selectEmployeeById(eid);
    			attendanceCRUDService.updateAttendanceByIdWithDate(aid, hoursWorked, employee, date);
    		}
    		else {
    			attendanceCRUDService.deleteAttendanceById(aid);
    		}
    		 
    		return "redirect:/manage/attendance";
    	} 
    	catch(Exception e) {
    		redirectAttributes.addFlashAttribute("error", e.getMessage());
      		return "redirect:/manage/attendance";
    	}
    }
}
