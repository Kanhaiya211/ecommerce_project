package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
