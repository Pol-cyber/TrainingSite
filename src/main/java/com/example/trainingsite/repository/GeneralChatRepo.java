package com.example.trainingsite.repository;

import com.example.trainingsite.entity.MessageGeneralChat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface GeneralChatRepo extends CrudRepository<MessageGeneralChat, Integer> {
    @Query(value = "SELECT m FROM MessageGeneralChat m ORDER BY m.id DESC")
    List<MessageGeneralChat> findLast10Messages(Pageable pageable);
}
