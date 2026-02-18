package com.finalterm.online_course_enrollment.repositories;

import com.finalterm.online_course_enrollment.models.Module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
