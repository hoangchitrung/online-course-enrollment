package com.finalterm.online_course_enrollment.controllers;

import com.finalterm.online_course_enrollment.models.CourseCohort;
import com.finalterm.online_course_enrollment.models.Lesson;
import com.finalterm.online_course_enrollment.models.Module;
import com.finalterm.online_course_enrollment.models.enums.CourseCohortStatus;
import com.finalterm.online_course_enrollment.models.enums.CourseType;
import com.finalterm.online_course_enrollment.repositories.CourseCohortRepository;
import com.finalterm.online_course_enrollment.repositories.LessonRepository;
import com.finalterm.online_course_enrollment.repositories.ModuleRepository;
import com.finalterm.online_course_enrollment.services.CourseService;
import com.finalterm.online_course_enrollment.services.CourseService.CreateCourseRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CourseService courseService;
    private final CourseCohortRepository courseCohortRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public AdminController(CourseService courseService, CourseCohortRepository courseCohortRepository,
            ModuleRepository moduleRepository, LessonRepository lessonRepository) {
        this.courseService = courseService;
        this.courseCohortRepository = courseCohortRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/dashboard";
    }

    @GetMapping("/courses/new")
    public String showNewCourseForm(Model model) {
        model.addAttribute("course", null);
        return "admin/course-form";
    }

    @PostMapping("/courses")
    public String createCourse(@RequestParam String name,
            @RequestParam String description,
            @RequestParam String image,
            @RequestParam String author,
            @RequestParam String courseType,
            @RequestParam String price,
            @RequestParam(defaultValue = "0") Integer modulesCount,
            @RequestParam(required = false) Integer moduleNumber,
            @RequestParam(required = false) String moduleTitle,
            @RequestParam(required = false) String moduleDescription,
            @RequestParam(required = false) Integer lessonNumber,
            @RequestParam(required = false) String lessonTitle,
            @RequestParam(required = false) String lessonDescription,
            @RequestParam(required = false) String lessonVideoUrl,
            @RequestParam(required = false) Integer lessonDurationMinutes,
            @RequestParam(required = false, defaultValue = "false") Boolean lessonIsPreview) {

        var savedCourse = courseService.createCourse(new CreateCourseRequest(name, description, image, author,
                courseType, new java.math.BigDecimal(price), modulesCount));

        if (savedCourse.getCourseType() == CourseType.LIVE_BOOT_CAMP) {
            var startDate = java.time.LocalDate.now().plusWeeks(1);
            var cohort = new CourseCohort();
            cohort.setCourse(savedCourse);
            cohort.setCohortName(savedCourse.getName() + " - Cohort 1");
            cohort.setStartDate(startDate);
            cohort.setEndDate(startDate.plusWeeks(10));
            cohort.setMaxStudents(30);
            cohort.setCourseCohortStatus(CourseCohortStatus.UPCOMING);
            courseCohortRepository.save(cohort);
        }

        if (moduleTitle != null && !moduleTitle.isBlank()) {
            var module = new Module();
            module.setCourse(savedCourse);
            module.setModuleNumber(moduleNumber != null ? moduleNumber : 1);
            module.setTitle(moduleTitle);
            module.setDescription(moduleDescription);
            module = moduleRepository.save(module);

            if (lessonTitle != null && !lessonTitle.isBlank()) {
                var lesson = new Lesson();
                lesson.setModule(module);
                lesson.setLessonNumber(lessonNumber != null ? lessonNumber : 1);
                lesson.setTitle(lessonTitle);
                lesson.setDescription(lessonDescription);
                lesson.setVideoUrl(lessonVideoUrl);
                lesson.setDurationMinutes(lessonDurationMinutes);
                lesson.setIsPreview(lessonIsPreview != null && lessonIsPreview);
                lessonRepository.save(lesson);
            }
        }

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/courses/{id}/edit")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        var courseOpt = courseService.getCourseById(id);
        if (courseOpt.isEmpty()) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("course", courseOpt.get());
        return "admin/course-form";
    }

    @PostMapping("/courses/{id}/edit")
    public String updateCourse(@PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String image,
            @RequestParam String author,
            @RequestParam String courseType,
            @RequestParam String price,
            @RequestParam(defaultValue = "0") Integer modulesCount,
            @RequestParam(required = false) Integer moduleNumber,
            @RequestParam(required = false) String moduleTitle,
            @RequestParam(required = false) String moduleDescription,
            @RequestParam(required = false) Integer lessonNumber,
            @RequestParam(required = false) String lessonTitle,
            @RequestParam(required = false) String lessonDescription,
            @RequestParam(required = false) String lessonVideoUrl,
            @RequestParam(required = false) Integer lessonDurationMinutes,
            @RequestParam(required = false, defaultValue = "false") Boolean lessonIsPreview) {

        var course = courseService.updateCourse(id, new CreateCourseRequest(name, description, image, author,
                courseType, new java.math.BigDecimal(price), modulesCount));

        if (moduleTitle != null && !moduleTitle.isBlank()) {
            var module = new Module();
            module.setCourse(course);
            module.setModuleNumber(moduleNumber != null ? moduleNumber : 1);
            module.setTitle(moduleTitle);
            module.setDescription(moduleDescription);
            module = moduleRepository.save(module);

            if (lessonTitle != null && !lessonTitle.isBlank()) {
                var lesson = new Lesson();
                lesson.setModule(module);
                lesson.setLessonNumber(lessonNumber != null ? lessonNumber : 1);
                lesson.setTitle(lessonTitle);
                lesson.setDescription(lessonDescription);
                lesson.setVideoUrl(lessonVideoUrl);
                lesson.setDurationMinutes(lessonDurationMinutes);
                lesson.setIsPreview(lessonIsPreview != null && lessonIsPreview);
                lessonRepository.save(lesson);
            }
        }

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/dashboard";
    }
}
