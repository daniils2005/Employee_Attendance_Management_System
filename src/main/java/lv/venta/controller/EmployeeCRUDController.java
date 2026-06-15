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
import lv.venta.model.Employee;
import lv.venta.service.IEmployeeCRUDService;

@Controller
@RequestMapping("/employee/crud")
public class EmployeeCRUDController {

    @Autowired
    private IEmployeeCRUDService employeeService;

    @GetMapping("/show/all")
    public String getAllEmployees(Model model) {
        try {
            model.addAttribute("employees", employeeService.selectAllEmployees());
            return "employee-show-all-page";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/one")
    public String getOneEmployeeById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("employee", employeeService.selectEmployeeById(id));
            return "employee-show-one-page";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove")
    public String getDeleteEmployeeById(@RequestParam(name = "id") long id, Model model) {
        try {
            employeeService.deleteEmployeeById(id);
            return "redirect:/employee/crud/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getAddNewEmployee(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-add-page";
    }

    @PostMapping("/add")
    public String postAddNewEmployee(@Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "employee-add-page";
        }
        try {
            employeeService.insertNewEmployee(employee.getName(), employee.getSurname(), employee.getPersonCode(), employee.getNumber(), employee.getEmail(), employee.getHourlyRate(), employee.getDepartment(), employee.getStatus(), employee.getPosition());
            return "redirect:/employee/crud/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update")
    public String getUpdateEmployeeById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("employee", employeeService.selectEmployeeById(id));
            model.addAttribute("id", id);
            return "employee-update-page";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update")
    public String postUpdateEmployeeById(@RequestParam(name = "id") long id, @Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "employee-update-page";
        }
        try {
            employeeService.updateEmployeeById(id, employee.getName(), employee.getSurname(), employee.getNumber(), employee.getEmail(), employee.getHourlyRate(), employee.getDepartment(), employee.getStatus(), employee.getPosition());
            return "redirect:/employee/crud/show/all";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
}
