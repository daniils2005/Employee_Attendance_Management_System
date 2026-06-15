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
import lv.venta.model.Vacation;
import lv.venta.service.IVacationCRUDService;

@Controller
@RequestMapping("/vacation/crud")
public class VacationCRUDController {

    @Autowired
    private IVacationCRUDService vacationService;

    @GetMapping("/show/all")
    public String getAllVacations(Model model) {
        try {
            model.addAttribute("vacations", vacationService.selectAllVacations());
            return "vacation-show-all-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/one")
    public String getOneVacationById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("vacation", vacationService.selectVacationById(id));
            return "vacation-show-one-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove")
    public String getDeleteVacationById(@RequestParam(name = "id") long id, Model model) {
        try {
            vacationService.deleteVacationById(id);
            return "redirect:/vacation/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getAddNewVacation(Model model) {
        model.addAttribute("vacation", new Vacation());
        return "vacation-add-page";
    }

    @PostMapping("/add")
    public String postAddNewVacation(@Valid Vacation vacation, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "vacation-add-page";
        }
        try {
            vacationService.insertNewVacation(vacation.getStartDate(), vacation.getEndDate(), vacation.getEmployee());
            return "redirect:/vacation/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update")
    public String getUpdateVacationById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("vacation", vacationService.selectVacationById(id));
            model.addAttribute("id", id);
            return "vacation-update-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update")
    public String postUpdateVacationById(@RequestParam(name = "id") long id, @Valid Vacation vacation, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("id", id);
            return "vacation-update-page";
        }
        try {
            vacationService.updateVacationById(id, vacation.getStartDate(), vacation.getEndDate(), vacation.getEmployee());
            return "redirect:/vacation/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
}