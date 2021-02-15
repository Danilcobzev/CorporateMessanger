package com.example.firstSpringProgect.repos;

import com.example.firstSpringProgect.domen.Message;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message,Long> {
    List<Message> findByTag(String tag);
}
