package com.jspy.ekart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jspy.ekart.dto.Customerdto;

@Repository
public interface CustomerRepository extends JpaRepository<Customerdto, Integer>{

	boolean existsByEmail(String email);

	boolean existsByMobile(String mobile);

	Customerdto findByEmail(String email);
	
}
