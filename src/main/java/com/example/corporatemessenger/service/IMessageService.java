package com.example.corporatemessenger.service;

import com.example.corporatemessenger.domen.dto.MessagePOJO;

import java.util.List;

public interface IMessageService {

    List<MessagePOJO> getAll();

    List<MessagePOJO> findByTag(String tag);

    void save(MessagePOJO messagePOJO);
}
