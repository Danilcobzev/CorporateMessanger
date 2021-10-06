package com.example.corporatemessenger.service;

import com.example.corporatemessenger.domen.Message;
import com.example.corporatemessenger.domen.dto.MessagePOJO;
import com.example.corporatemessenger.repos.MessageRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class MessageService implements IMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    public List<MessagePOJO> getAll() {
        ArrayList<MessagePOJO> list = new ArrayList<MessagePOJO>();
        for (Message item : messageRepo.findAll()) {
            MessagePOJO mp = new MessagePOJO(
                    item.getText(),
                    item.getTag(),
                    item.getAuthor()
            );
            mp.setFilename(item.getFilename());
            list.add(mp);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Returning it:" + list.toString());
        }
        return list;
    }

    @Override
    public List<MessagePOJO> findByTag(String tag) {
        ArrayList<MessagePOJO> list = new ArrayList<MessagePOJO>();
        LOGGER.info("Method findByTag called");
        for (Message item : messageRepo.findByTag(tag)) {
            MessagePOJO messagePOJO = new MessagePOJO(
                    item.getText(),
                    item.getTag(),
                    item.getAuthor()
            );
            messagePOJO.setFilename(item.getFilename());
            list.add(messagePOJO);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Return it from the findByTag method :" + list.toString());
        }
        return list;
    }

    @Override
    public void save(MessagePOJO messagePojo) {
        LOGGER.info("Method save called");
        Message message = messageRepo.save(
                new Message(
                        messagePojo.getText(),
                        messagePojo.getTag(),
                        messagePojo.getAuthor(),
                        messagePojo.getFilename()
                )
        );
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Saved in db:" + message.toString());
        }
    }
}
