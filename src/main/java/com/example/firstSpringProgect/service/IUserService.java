package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.User;
import com.example.firstSpringProgect.domen.dto.UserPOJO;

import java.util.List;
import java.util.Map;

public interface IUserService {
    List<UserPOJO> findAll();
    UserPOJO save(UserPOJO userPOJO, String password);
    UserPOJO getById(Long id);
    UserPOJO change(User user , String email);
    UserPOJO changeRoles(Long userId, Map<String, String> form);
    void userWannaChangeEmail(User user, String email);
}
