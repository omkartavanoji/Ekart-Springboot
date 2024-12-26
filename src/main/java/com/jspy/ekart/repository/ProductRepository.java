package com.jspy.ekart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jspy.ekart.dto.Productdto;
@Repository
public interface ProductRepository extends JpaRepository<Productdto, Integer> {
   
}
