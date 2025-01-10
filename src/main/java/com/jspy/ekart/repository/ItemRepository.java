package com.jspy.ekart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jspy.ekart.dto.Items;

@Repository
public interface ItemRepository extends JpaRepository<Items, Integer> {

}
