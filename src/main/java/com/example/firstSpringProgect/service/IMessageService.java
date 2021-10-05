package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.dto.MessagePOJO;

import java.util.List;

public interface IMessageService {

    List<MessagePOJO> getAll();

    List<MessagePOJO> findByTag(String tag);

    void save(MessagePOJO messagePOJO);
}
