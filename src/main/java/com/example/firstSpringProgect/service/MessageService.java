package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.Message;
import com.example.firstSpringProgect.domen.dto.MessagePOJO;
import com.example.firstSpringProgect.repos.MessageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class MessageService implements IMessageService{

    private Logger l = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepo messageRepo;

    @Override
    public List<MessagePOJO> getAll() {
        ArrayList<MessagePOJO> list = new ArrayList<MessagePOJO>();
        l.debug("Вызван метод getAll. Возвращаю :");
        for (Message item : messageRepo.findAll()) {
            MessagePOJO mp = new MessagePOJO(
                    item.getText(),
                    item.getTag(),
                    item.getAuthor()
            );
            list.add(mp);
            l.debug(mp.toString());
        }
        l.debug("_____________");
        return list;
    }

    @Override
    public List<MessagePOJO> findByTag(String tag) {
        ArrayList<MessagePOJO> list = new ArrayList<MessagePOJO>();
        for (Message item : messageRepo.findByTag(tag)) {
            list.add(new MessagePOJO(
                    item.getText(),
                    item.getTag(),
                    item.getAuthor()
            ));
        }
        return list;
    }

    @Override
    public void save(MessagePOJO messagePojo) {
        messageRepo.save(
                new Message(
                        messagePojo.getText(),
                        messagePojo.getTag(),
                        messagePojo.getAuthor()
                )
        );
    }
}
