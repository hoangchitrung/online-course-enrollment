package com.finalterm.online_course_enrollment.repositories;

import com.finalterm.online_course_enrollment.models.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
