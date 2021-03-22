package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.Message;
import com.example.firstSpringProgect.domen.dto.MessagePOJO;

import java.util.List;

public interface IMessageService {
    public List<MessagePOJO> getAll();

    List<MessagePOJO> findByTag(String tag);

    void save(MessagePOJO m);

}
