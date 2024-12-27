package com.jspy.ekart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jspy.ekart.dto.Productdto;
import com.jspy.ekart.dto.Vendordto;
@Repository
public interface ProductRepository extends JpaRepository<Productdto, Integer> {

	List<Productdto> findByVendordto(Vendordto vendordto);
   
}
