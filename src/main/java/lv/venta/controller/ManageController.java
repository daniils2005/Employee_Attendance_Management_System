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

import lv.venta.model.Attendance;
import lv.venta.model.Employee;
import lv.venta.model.Overtime;
import lv.venta.model.enums.RequestStatus;
import lv.venta.service.IAttendanceCRUDService;
import lv.venta.service.IEmployeeCRUDService;
import lv.venta.service.IGeneralService;
import lv.venta.service.IOvertimeCRUDService;

@Controller
public class ManageController {

	@Autowired
	private IGeneralService generalService;
	
	@Autowired
	private IAttendanceCRUDService attendanceCRUDService;
	
	@Autowired
	private IEmployeeCRUDService employeeCRUDService;
	
	@Autowired
	private IOvertimeCRUDService overtimeCRUDService;
	
    @GetMapping("/manage/attendance")
    public String getAttendanceByEmployee(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model) {
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
			else if ("surname".equals(sort) && "asc".equals(order)) {
			    attendances.sort(Comparator.comparing(a -> a.getEmployee().getSurname()));
			}
			else if ("surname".equals(sort) && "desc".equals(order)) {
			    attendances.sort(Comparator.comparing(a -> ((Attendance) a).getEmployee().getSurname()).reversed());
			}
			else if ("date".equals(sort) && "asc".equals(order)) {
			    attendances.sort(Comparator.comparing(Attendance::getWorkDate));
			}
			else if ("date".equals(sort) && "desc".equals(order)) {
			    attendances.sort(Comparator.comparing(Attendance::getWorkDate).reversed());
			}
	
			model.addAttribute("attendances", attendances);
			return "manage-attendance-page";
    	}   
        catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
    
    @PostMapping("/manage/attendance")
    public String postAttendanceByEmployee(@RequestParam(required = false) Long eid, @RequestParam float hoursWorked, @RequestParam(required = false) LocalDate date, Model model) {
    	 
    	 try {
    		 Employee employee = employeeCRUDService.selectEmployeeById(eid);
    		  attendanceCRUDService.insertNewAttendanceWithDate(hoursWorked, employee, date);
    		 return "redirect:/manage/attendance";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
    
    @GetMapping("/manage/overtime")
    public String getOvertimeByEmployees(@RequestParam(required = false) Long id, @RequestParam(required = false) LocalDate date, @RequestParam(required = false) String sort, Model model) {
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

			if ("nameAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(a -> a.getEmployee().getName()));
			}
			else if ("nameDesc".equals(sort)) {
				overtime.sort(Comparator.comparing((Overtime a) -> a.getEmployee().getName()).reversed());
			}
			else if ("idAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(a -> a.getEmployee().getEid()));
			}
			else if ("idDesc".equals(sort)) {
				overtime.sort(Comparator.comparing((Overtime a) -> a.getEmployee().getEid()).reversed());
			}
			else if ("dateAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getDate));
			}
			else if ("dateDesc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getDate).reversed());
			}
			else if ("overHoursAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeHours));
			}
			else if ("overHoursDesc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeHours).reversed());
			}
			else if ("overHoursRateAsc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeRate,Comparator.nullsLast(Float::compareTo)));
			}
			else if ("overHoursRateDesc".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getOvertimeRate,Comparator.nullsLast(Float::compareTo)).reversed());
			}
			else if ("statusSort".equals(sort)) {
				overtime.sort(Comparator.comparing(Overtime::getStatus,Comparator.nullsLast(Comparator.naturalOrder())).reversed());
			}

    		model.addAttribute("overtimes", overtime);
    		return "manage-overtime-page";
    	}
    	catch(Exception e) {
   		 model.addAttribute("errorMessage", e.getMessage());
   		 return "error-page";
   	 	}
    }
    
    @PostMapping("/manage/overtime")
    public String postOvertimeByEmployees(@RequestParam(required = false) Long id, @RequestParam float overtimeHours, @RequestParam float overtimeRate, @RequestParam(required = false) String description, @RequestParam(required = false) LocalDate date, @RequestParam RequestStatus status, Model model) { 
    	 try {
    		 Employee employee = employeeCRUDService.selectEmployeeById(id);
    		 overtimeCRUDService.insertNewOvertimeWithDateAndStatusAndOvertimeRate(overtimeHours, description ,employee, date, status, overtimeRate);
    		 return "redirect:/manage/overtime";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute("errorMessage", e.getMessage());
    		 return "error-page";
    	 }
    }
}
