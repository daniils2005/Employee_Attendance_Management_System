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
import lv.venta.model.Overtime;
import lv.venta.service.IOvertimeCRUDService;

@Controller
@RequestMapping("/overtime/crud")
public class OvertimeCRUDController {

    @Autowired
    private IOvertimeCRUDService overtimeService;

    @GetMapping("/show/all")
    public String getAllOvertimes(Model model) {
        try {
            model.addAttribute("overtimes", overtimeService.selectAllOvertimes());
            return "overtime-show-all-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/one")
    public String getOneOvertimeById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("overtime", overtimeService.selectOvertimeById(id));
            return "overtime-show-one-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove")
    public String getDeleteOvertimeById(@RequestParam(name = "id") long id, Model model) {
        try {
            overtimeService.deleteOvertimeById(id);
            return "redirect:/overtime/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getAddNewOvertime(Model model) {
        model.addAttribute("overtime", new Overtime());
        return "overtime-add-page";
    }

    @PostMapping("/add")
    public String postAddNewOvertime(@Valid Overtime overtime, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "overtime-add-page";
        }
        try {
            overtimeService.insertNewOvertime(overtime.getOvertimeHours(), overtime.getDescription(), overtime.getEmployee());
            return "redirect:/overtime/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update")
    public String getUpdateOvertimeById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("overtime", overtimeService.selectOvertimeById(id));
            model.addAttribute("id", id);
            return "overtime-update-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update")
    public String postUpdateOvertimeById(@RequestParam(name = "id") long id, @Valid Overtime overtime, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("id", id);
            return "overtime-update-page";
        }
        try {
            overtimeService.updateOvertimeById(id, overtime.getOvertimeHours(), overtime.getOvertimeRate(), overtime.getDescription(), overtime.getEmployee());
            return "redirect:/overtime/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
}