package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.HomeCategory;

public interface HomeCategoryRepository  extends JpaRepository<HomeCategory,Long> {

}
