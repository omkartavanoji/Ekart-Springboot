package com.jspy.ekart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jspy.ekart.dto.Vendordto;
@Repository
public interface VendorRepository extends JpaRepository<Vendordto, Integer>{

	 boolean existsByEmail(String email) ;

   	boolean existsByMobile(long mobile);

	Vendordto findByEmail(String email);

}
