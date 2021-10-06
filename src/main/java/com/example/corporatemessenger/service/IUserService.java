package com.example.corporatemessenger.service;

import com.example.corporatemessenger.domen.User;
import com.example.corporatemessenger.domen.dto.UserPOJO;

import java.util.List;
import java.util.Map;

public interface IUserService {
    List<UserPOJO> findAll();
    UserPOJO getById(Long id);
    UserPOJO change(User user, String email);
    UserPOJO changeRoles(Long userId, Map<String, String> form);
    void userWannaChangeEmail(User user, String email);
}
