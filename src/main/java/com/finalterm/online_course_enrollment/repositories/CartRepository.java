package com.finalterm.online_course_enrollment.repositories;

import com.finalterm.online_course_enrollment.models.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
