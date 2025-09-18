package com.example.demo.repository;

import com.example.demo.entity.AuditReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditReservationRepository extends JpaRepository<AuditReservation, java.util.UUID> {}
