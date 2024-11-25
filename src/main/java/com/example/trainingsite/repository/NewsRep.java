package com.example.trainingsite.repository;

import com.example.trainingsite.entity.News;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsRep extends CrudRepository<News,Integer> {

    @Query("SELECT n.image FROM News n WHERE n.id = :id")
    byte[] findByteImage(@Param("id") int id);

    @Query("SELECT n FROM News n WHERE n.actual = true ORDER BY n.data DESC")
    List<News> findActualNews();

    @Transactional
    @Modifying
    @Query("UPDATE News n SET n.actual = false WHERE n.id = :newsId")
    void changeActual(@Param("newsId") Long newsId);

}

