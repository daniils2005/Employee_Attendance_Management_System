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
import lv.venta.model.User;
import lv.venta.service.IUserCRUDService;

@Controller
@RequestMapping("/user/crud")
public class UserCRUDController {

    @Autowired
    private IUserCRUDService userService;

    @GetMapping("/show/all")
    public String getAllUsers(Model model) {
        try {
            model.addAttribute("users", userService.selectAllUsers());
            return "user-show-all-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/show/one")
    public String getOneUserById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("user", userService.selectUserById(id));
            return "user-show-one-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/remove")
    public String getDeleteUserById(@RequestParam(name = "id") long id, Model model) {
        try {
            userService.deleteUserById(id);
            return "redirect:/user/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String getAddNewUser(Model model) {
        model.addAttribute("user", new User());
        return "user-add-page";
    }

    @PostMapping("/add")
    public String postAddNewUser(@Valid User user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "user-add-page";
        }
        try {
            userService.insertNewUser(user.getUsername(), user.getPassword(), user.getRole(), user.getEmployee());
            return "redirect:/user/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/update")
    public String getUpdateUserById(@RequestParam(name = "id") long id, Model model) {
        try {
            model.addAttribute("user", userService.selectUserById(id));
            model.addAttribute("id", id);
            return "user-update-page";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/update")
    public String postUpdateUserById(@RequestParam(name = "id") long id, @Valid User user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("id", id);
            return "user-update-page";
        }
        try {
            userService.updateUserById(id, user.getUsername(), user.getPassword(), user.getRole(), user.getEmployee());
            return "redirect:/user/crud/show/all";
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }
}
