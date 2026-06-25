package lv.venta.controller;

import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lv.venta.model.Department;
import lv.venta.model.Position;
import lv.venta.repo.IDepartmentRepo;
import lv.venta.service.IPositionCRUDService;

@Controller
public class ManagePositionPageController {

	@Autowired
	private IPositionCRUDService positionCRUDService;
	
	@Autowired
	private IDepartmentRepo departmentRepo;
	
	private String errorPage = "error-page";
	private String errorMessage = "errorMessage";
	
	@GetMapping("/manage/position")
	public String getPosition(@RequestParam(required = false) Long id, @RequestParam(required = false) String sort, @RequestParam(required = false) String order, Model model) {
		 try {
			ArrayList<Position> position;
			position = positionCRUDService.selectAllPositions();
     		if ("pid".equals(sort) && "asc".equals(order)) {
     			position.sort(Comparator.comparing(a -> a.getPid())); 
 			}
 			else if ("pid".equals(sort) && "desc".equals(order)) {
 				position.sort(Comparator.comparing(a -> ((Position) a).getPid()).reversed()); 
 			}
 			else if ("positionName".equals(sort) && "asc".equals(order)) {
 				position.sort(Comparator.comparing(Position::getName,Comparator.nullsLast(Comparator.naturalOrder())));
 			}
 			else if ("positionName".equals(sort) && "desc".equals(order)) {
 				position.sort(Comparator.comparing(Position::getName,Comparator.nullsLast(Comparator.naturalOrder())).reversed()); 			}

     		if(departmentRepo.count() != 0) {
     			ArrayList<Department> departmentLastDid = departmentRepo.findTopByOrderByDidDesc();
     			model.addAttribute("lastPid", departmentLastDid.get(0).getDid() + 1);
     		}
     		model.addAttribute("positions", position);
     		return "manage-position-page";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
	
    @PostMapping("/manage/position/add")
    public String addPosition(@RequestParam String addPositionName, @RequestParam String description, Model model) { 
    	 try {
    		 positionCRUDService.insertNewPosition(addPositionName, description);
    		 return "redirect:/manage/position";
    	 } 
    	 catch(Exception e) {
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
 
    @PostMapping("/manage/position/update-or-delete")
    public String updatePosition(@RequestParam String action, @RequestParam Long pid, @RequestParam String positionName, @RequestParam String description, Model model) {
    	 try {
    		 if ("save".equals(action)) {
    			positionCRUDService.updatePositionById(pid, positionName, description);
    		 }
    		 else {
    			 positionCRUDService.deletePositionById(pid);
    		 }
    	 return "redirect:/manage/position";
    	 }
    	 catch(Exception e) {
    		 model.addAttribute(errorMessage, e.getMessage());
    		 return errorPage;
    	 }
    }
}
