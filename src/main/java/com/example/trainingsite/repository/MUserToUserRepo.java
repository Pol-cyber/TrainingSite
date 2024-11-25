package com.example.trainingsite.repository;


import com.example.trainingsite.entity.MessageUserToUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MUserToUserRepo extends CrudRepository<MessageUserToUser,Long> {
//    @Query("SELECT m FROM MessageUserToUser m WHERE (m.senderId = :user1 AND m.receiverId = :user2) OR (m.senderId = :user2 AND m.receiverId = :user1) ORDER BY m.data DESC")
//    List<MessageUserToUser> findLast10MessagesBetweenUsers(String user1, String user2, Pageable pageable);

    @Query("SELECT m FROM MessageUserToUser m WHERE (m.senderId = :user1 AND m.receiverId = :user2) OR (m.senderId = :user2 AND m.receiverId = :user1) ORDER BY m.data DESC")
    List<MessageUserToUser> findLast10ReceiveMessagesBetweenUsers(String user1, String user2, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE MessageUserToUser m SET m.isRead = true WHERE m.senderId = :senderId AND m.receiverId = :receiverId AND m.isRead = false")
    Integer receiveAllUnreadMessage(@Param("senderId") String senderId, @Param("receiverId") String receiverId);

    @Transactional
    @Modifying
    @Query("UPDATE MessageUserToUser m SET m.isRead = true WHERE m.id = :specificId")
    void updateSpecificMessage(@Param("specificId") Long specificId);
}
