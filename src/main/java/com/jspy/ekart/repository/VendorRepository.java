package com.jspy.ekart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspy.ekart.dto.Vendordto;

public interface VendorRepository extends JpaRepository<Vendordto, Integer>{

}
