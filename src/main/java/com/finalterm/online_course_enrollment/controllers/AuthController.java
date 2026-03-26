package com.finalterm.online_course_enrollment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.finalterm.online_course_enrollment.controllers.dto.RegisterRequest;
import com.finalterm.online_course_enrollment.models.User;
import com.finalterm.online_course_enrollment.models.enums.CourseType;
import com.finalterm.online_course_enrollment.services.AuthService;
import com.finalterm.online_course_enrollment.services.CourseService;
import com.finalterm.online_course_enrollment.controllers.dto.RegisterRequest;
import com.finalterm.online_course_enrollment.repositories.CourseCohortRepository;
import com.finalterm.online_course_enrollment.repositories.LessonRepository;
import com.finalterm.online_course_enrollment.repositories.ModuleRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping
@Validated
public class AuthController {
    private final AuthService authService;
    private final CourseService courseService;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CourseCohortRepository courseCohortRepository;
    private final com.finalterm.online_course_enrollment.services.CartService cartService;

    public AuthController(AuthService authService, CourseService courseService, ModuleRepository moduleRepository,
            LessonRepository lessonRepository, CourseCohortRepository courseCohortRepository,
            com.finalterm.online_course_enrollment.services.CartService cartService) {
        this.authService = authService;
        this.courseService = courseService;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.courseCohortRepository = courseCohortRepository;
        this.cartService = cartService;
    }

    private void addCartCountToModel(Model model, java.security.Principal principal) {
        if (principal != null) {
            model.addAttribute("cartItemCount", cartService.getCartItemCount(principal.getName()));
        } else {
            model.addAttribute("cartItemCount", 0);
        }
    }

    @GetMapping("/")
    public String homePage(Model model, java.security.Principal principal) {
        addCartCountToModel(model, principal);
        model.addAttribute("courses", courseService.getAllCourses());
        return "index";
    }

    @GetMapping("/courses")
    public String coursesPage(Model model, java.security.Principal principal) {
        addCartCountToModel(model, principal);
        model.addAttribute("courses", courseService.getAllCourses());
        return "index";
    }

    @GetMapping("/course/{id}")
    public String courseDetail(@PathVariable Long id, Model model, java.security.Principal principal) {
        addCartCountToModel(model, principal);
        return courseService.getCourseById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    var modules = moduleRepository.findByCourseIdOrderByModuleNumberAsc(id);
                    // Sort lessons in each module by lessonNumber in ascending order
                    modules.forEach(module -> {
                        var sortedLessons = lessonRepository.findByModuleIdOrderByLessonNumberAsc(module.getId());
                        module.setLessons(new java.util.LinkedHashSet<>(sortedLessons));
                    });
                    model.addAttribute("modules", modules);
                    if (course.getCourseType() == CourseType.LIVE_BOOT_CAMP) {
                        model.addAttribute("cohorts", courseCohortRepository.findByCourseId(id));
                    }

                    boolean enrolled = false;
                    if (principal != null) {
                        enrolled = cartService.getOrdersForUser(principal.getName()).stream()
                                .flatMap(order -> order.getOrderItems().stream())
                                .anyMatch(item -> item.getOrderItemCourse() != null
                                        && item.getOrderItemCourse().getId().equals(id));
                    }
                    model.addAttribute("enrolled", enrolled);
                    return "course-detail";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User saved = authService.register(request);
            // no return password/hash to outside
            RegisterResponse response = new RegisterResponse(saved.getId(), saved.getEmail(), saved.getFullName(),
                    saved.getUserType().name());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    public record RegisterResponse(Long id, String email, String fullName, String userType) {
    }
}
