package com.finalterm.online_course_enrollment.repositories;

import java.util.Optional;

import com.finalterm.online_course_enrollment.models.Interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByName(String name);
}
