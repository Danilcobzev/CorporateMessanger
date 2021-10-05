package com.example.firstSpringProgect.controller;

import com.example.firstSpringProgect.domen.User;
import com.example.firstSpringProgect.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.firstSpringProgect.constans.Attribute.MESSAGE;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService=userService;
    }

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String addUser(User user, Map<String, Object> model) {
        if (!userService.addUser(user)) {
            model.put(MESSAGE, "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/activate/{code}", method = RequestMethod.GET)
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute(MESSAGE, "User successfully activated");// todo: вынести ВСЕ Attribute в отдельный класс
        } else {
            model.addAttribute(MESSAGE, "Activation code is not found!");
        }
        return "login";
    }
}
