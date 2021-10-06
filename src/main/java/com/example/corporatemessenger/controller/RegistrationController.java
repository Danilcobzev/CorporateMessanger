package com.example.corporatemessenger.controller;

import com.example.corporatemessenger.domen.User;
import com.example.corporatemessenger.service.UserService;
import com.example.corporatemessenger.constans.Attribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(User user, Map<String, Object> model) {
        if (!userService.addUser(user)) {
            model.put(Attribute.MESSAGE, "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/activate/{code}", method = RequestMethod.GET)
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute(Attribute.MESSAGE, "User successfully activated");
        } else {
            model.addAttribute(Attribute.MESSAGE, "Activation code is not found!");
        }
        return "login";
    }
}
