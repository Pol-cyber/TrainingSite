package com.example.trainingsite.repository;

import com.example.trainingsite.Entity.UserCharacteristic;
import org.springframework.data.repository.CrudRepository;

public interface CharacteristicRepo extends CrudRepository<UserCharacteristic,String> {

}
