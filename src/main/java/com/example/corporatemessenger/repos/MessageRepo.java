package com.example.corporatemessenger.repos;

import com.example.corporatemessenger.domen.Message;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Long> {
    List<Message> findByTag(String tag);
}
