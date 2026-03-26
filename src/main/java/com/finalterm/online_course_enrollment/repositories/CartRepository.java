package com.finalterm.online_course_enrollment.repositories;

import com.finalterm.online_course_enrollment.models.Cart;
import com.finalterm.online_course_enrollment.models.Course;
import com.finalterm.online_course_enrollment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserCartAndCourseCart(User user, Course course);
    List<Cart> findByUserCart_Email(String email);
    int countByUserCart_Email(String email);
}
