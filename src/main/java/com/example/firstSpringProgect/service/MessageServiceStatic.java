package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.Message;
import com.example.firstSpringProgect.domen.User;
import com.example.firstSpringProgect.domen.dto.MessagePOJO;
import com.example.firstSpringProgect.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class MessageServiceStatic implements IMessageService {

    List<MessagePOJO> list = Arrays.asList(
            new MessagePOJO("asd","asd",new User())
    );

    @Override
    public List<MessagePOJO> getAll() {
        return list;
    }

    @Override
    public List<MessagePOJO> findByTag(String tag) {
        return new ArrayList<MessagePOJO>();
    }

    @Override
    public void save(MessagePOJO message) {
        list.add(message);
    }
}
