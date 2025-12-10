package com.ecom.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.project.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
