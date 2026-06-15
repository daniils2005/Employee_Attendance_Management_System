package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lv.venta.model.Attendance;
import lv.venta.service.IAttendanceCRUDService;

@Controller
@RequestMapping("/attendance/crud")
public class AttendanceCRUDController {
	
	@Autowired
	private IAttendanceCRUDService attendanceService;
	
	@GetMapping("/show/all")
	public String getAllAttendances(Model model) {
		try {
			model.addAttribute("attendances", attendanceService.selectAllAttendances());
			return "attendance-show-all-page";
		} catch(Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/show/one")
	public String getOneAttendanceById(@RequestParam(name = "id") long id, Model model) {
		try {
			model.addAttribute("attendance", attendanceService.selectAttendanceById(id));
			return "attendance-show-one-page";
		} catch(Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/remove")
	public String getDeleteAttendanceById(@RequestParam(name = "id") long id, Model model) {
		try {
			attendanceService.deleteAttendanceById(id);
			return "redirect:/attendance/crud/show/all";
		} catch(Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/add")
	public String getAddNewAttendance(Model model) {
		model.addAttribute("attendance", new Attendance());
		return "attendance-add-page";
	}
	
	@PostMapping("/add")
	public String postAddNewAttendance(@Valid Attendance attendance, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "attendance-add-page";
		}
		try {
			attendanceService.insertNewAttendance(attendance.getHoursWorked(), attendance.getEmployee());
			return "redirect:/attendance/crud/show/all";
		} catch(Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error-page";
		}
	}
	
	@GetMapping("/update")
	public String getUpdateAttendanceById(@RequestParam(name = "id") long id, Model model) {
		try {
			model.addAttribute("attendance", attendanceService.selectAttendanceById(id));
			model.addAttribute("id", id);
			return "attendance-update-page";
		} catch(Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error-page";
		}
	}
	
	@PostMapping("/update")
	public String postUpdateAttendanceById(@RequestParam(name = "id") long id, @Valid Attendance attendance, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("id", id);
			return "attendance-update-page";
		}
		try {
			attendanceService.updateAttendanceById(id, attendance.getHoursWorked(), attendance.getEmployee());
			return "redirect:/attendance/crud/show/all";
		} catch(Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error-page";
		}
	}
	
}

