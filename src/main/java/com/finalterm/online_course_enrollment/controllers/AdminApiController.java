package com.finalterm.online_course_enrollment.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalterm.online_course_enrollment.models.Course;
import com.finalterm.online_course_enrollment.services.CourseService;
import com.finalterm.online_course_enrollment.services.CourseService.CreateCourseRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private final CourseService courseService;

    public AdminApiController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public List<Course> listCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest request) {
        Course created = courseService.createCourse(request);
        return ResponseEntity.created(URI.create("/api/admin/courses/" + created.getId())).body(created);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
