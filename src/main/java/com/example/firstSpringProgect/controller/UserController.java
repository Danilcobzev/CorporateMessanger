package com.example.firstSpringProgect.controller;

import com.example.firstSpringProgect.domen.Role;
import com.example.firstSpringProgect.domen.User;
import com.example.firstSpringProgect.domen.dto.UserPOJO;
import com.example.firstSpringProgect.providers.JwtConfirmProvider;
import com.example.firstSpringProgect.repos.UserRepo;
import com.example.firstSpringProgect.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
//@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private JwtConfirmProvider jwtConfirmProvider;


    @GetMapping
    private String userList(Model model){
        model.addAttribute("users", iUserService.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable("user") Long userId,Model model){
        UserPOJO up = iUserService.getById(userId);
        model.addAttribute("user",up);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userChangeRoles(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") Long userId
    ) {
        iUserService.changeRoles(userId,form);
       return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getprofile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("email",user.getEmail());
        model.addAttribute("username",user.getUsername());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user, @RequestParam String email){
        iUserService.userWannaChangeEmail(user,email);
        return "redirect:/user/profile";
    }

    @GetMapping("/changeEmail/{token}")
    public String changeEmail(@AuthenticationPrincipal User user,@PathVariable String token){
        iUserService.change(user,jwtConfirmProvider.getEmailFromToken(token));
        return "redirect:/user/profile";
    }
}
