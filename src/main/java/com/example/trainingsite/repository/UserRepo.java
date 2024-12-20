package com.example.trainingsite.repository;


import com.example.trainingsite.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepo extends CrudRepository<User,String> {
    User findByUsername(String username);

    @Query("SELECT u.image FROM User u WHERE u.username = :id")
    byte[] findByteImage(@Param("id") String id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :newStatus WHERE u.username = :username")
    void updateStatusByLogin(@Param("username") String username, @Param("newStatus") User.Status newStatus);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.image = :newImage WHERE u.username = :username")
    void updateImageByLogin(@Param("username") String username, @Param("newImage") byte[] newImage);


    @Query("SELECT u FROM User u WHERE u.username <> :activeUsername")
    List<User> getOnlineUserWithoutActiveUser(@Param("activeUsername") String activeUsername);

    @Query("SELECT u.email FROM User u WHERE u.newsletterSub = true")
    List<String> getEmailsByNewsletterSubIsTrue();

    @Query("SELECT u FROM User u WHERE u.role = 'ROLE_ADMIN' AND u.username <> :currentUserName")
    List<User> getAllAdminUsers(@Param("currentUserName") String currentUserName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.newsletterSub = :newsletterSubscribed WHERE u.username = :username")
    void updateNewsletterSubscription(@Param("username") String username, @Param("newsletterSubscribed") boolean newsletterSubscribed);
}


