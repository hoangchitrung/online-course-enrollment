package com.finalterm.online_course_enrollment.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.finalterm.online_course_enrollment.models.Course;
import com.finalterm.online_course_enrollment.models.enums.CourseType;
import com.finalterm.online_course_enrollment.repositories.CourseRepository;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course updateCourse(Long id, CreateCourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        course.setName(request.name());
        course.setDescription(request.description());
        course.setImage(request.image());
        course.setAuthor(request.author());
        course.setCourseType(CourseType.fromString(request.courseType));
        course.setPrice(request.price());
        course.setModulesCount(request.modulesCount() != null ? request.modulesCount() : 0);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    public Course createCourse(CreateCourseRequest request) {
        if (request.name == null || request.name.isBlank()) {
            throw new IllegalArgumentException("Course name is required");
        }
        if (request.author == null || request.author.isBlank()) {
            throw new IllegalArgumentException("Author is required");
        }
        if (request.price == null) {
            throw new IllegalArgumentException("Price is required");
        }
        CourseType courseType;
        try {
            courseType = CourseType.fromString(request.courseType);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid course type. Supported: SELF_PACED, LIVE_BOOT_CAMP");
        }

        Course course = new Course();
        course.setName(request.name);
        course.setDescription(request.description);
        course.setImage(request.image);
        course.setAuthor(request.author);
        course.setCourseType(courseType);
        course.setPrice(request.price);
        course.setModulesCount(request.modulesCount != null ? request.modulesCount : 0);

        return courseRepository.save(course);
    }

    public static record CreateCourseRequest(String name, String description, String image, String author,
            String courseType, BigDecimal price, Integer modulesCount) {
    }
}
