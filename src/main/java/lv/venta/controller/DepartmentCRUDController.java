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
import lv.venta.model.Department;
import lv.venta.service.IDepartmentCRUDService;

@Controller
@RequestMapping("/department/crud")
public class DepartmentCRUDController {

    @Autowired
    private IDepartmentCRUDService departmentService;

    @GetMapping("/show/all")
    public String getAllDepartments(Model model) {
        try {
            model.addAttribute("departments", departmentService.selectAllDepartments());
            return "department-show-all-page";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/one")
    public String getOneDepartmentById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("department", departmentService.selectDepartmentById(id));
            return "department-show-one-page";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove")
    public String getDeleteDepartmentById(@RequestParam(name = "id") long id, Model model) {
        try {
            departmentService.deleteDepartmentById(id);
            return "redirect:/department/crud/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getAddNewDepartment(Model model) {
        model.addAttribute("department", new Department());
        return "department-add-page";
    }

    @PostMapping("/add")
    public String postAddNewDepartment(@Valid Department department, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "department-add-page";
        }
        try {
            departmentService.insertNewDepartment(department.getDepartmentName(), department.getDescription());
            return "redirect:/department/crud/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update")
    public String getUpdateDepartmentById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("department", departmentService.selectDepartmentById(id));
            model.addAttribute("id", id);
            return "department-update-page";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update")
    public String postUpdateDepartmentById(@RequestParam(name = "id") long id, @Valid Department department, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "department-update-page";
        }
        try {
            departmentService.updateDepartmentById(id, department.getDepartmentName(), department.getDescription());
            return "redirect:/department/crud/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
}