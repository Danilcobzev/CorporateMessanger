package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.dto.UserPOJO;

import java.util.List;

public interface IUserService {
    List<UserPOJO> findAll();
    UserPOJO save(UserPOJO userPOJO);
    UserPOJO getById(Long id);
}
