package com.example.corporatemessenger.controller;

import com.example.corporatemessenger.domen.Role;
import com.example.corporatemessenger.domen.User;
import com.example.corporatemessenger.domen.dto.UserPOJO;
import com.example.corporatemessenger.providers.JwtConfirmProvider;
import com.example.corporatemessenger.service.IUserService;
import com.example.corporatemessenger.constans.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final IUserService iUserService;

    private final JwtConfirmProvider jwtConfirmProvider;

    public UserController(IUserService iUserService, JwtConfirmProvider jwtConfirmProvider) {
        this.iUserService = iUserService;
        this.jwtConfirmProvider = jwtConfirmProvider;
    }

    @GetMapping
    private String userList(Model model) {
        model.addAttribute(Attribute.USERS, iUserService.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable("user") Long userId, Model model) {
        UserPOJO userPOJO = iUserService.getById(userId);
        model.addAttribute(Attribute.USER, userPOJO);
        model.addAttribute(Attribute.ROLES, Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userChangeRoles(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") Long userId
    ) {
        iUserService.changeRoles(userId, form);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(Attribute.EMAIL, user.getEmail());
        model.addAttribute(Attribute.USERNAME, user.getUsername());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user, @RequestParam String email) {
        iUserService.userWannaChangeEmail(user, email);
        return "redirect:/user/profile";
    }

    @GetMapping("/changeEmail/{token}")
    public String changeEmail(@AuthenticationPrincipal User user, @PathVariable String token) {
        try {
            iUserService.change(user, jwtConfirmProvider.getEmailFromToken(token));
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return "redirect:/user/profile";
    }
}
