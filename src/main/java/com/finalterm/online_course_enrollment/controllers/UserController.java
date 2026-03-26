package com.finalterm.online_course_enrollment.controllers;

import com.finalterm.online_course_enrollment.models.Lesson;
import com.finalterm.online_course_enrollment.models.Module;
import com.finalterm.online_course_enrollment.models.User;
import com.finalterm.online_course_enrollment.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.openpdf.text.Document;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/user")
public class UserController {

    private final CartService cartService;

    public UserController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        var userOpt = cartService.getUserByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/signin";
        }
        User user = userOpt.get();
        var myOrders = cartService.getOrdersForUser(principal.getName());
        int totalCourses = myOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(item -> item.getOrderItemCourse())
                .collect(java.util.stream.Collectors.toSet())
                .size();
        int totalOrders = myOrders.size();
        int cartItemsCount = cartService.getCartItemCount(principal.getName());

        var enrolledCourses = myOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(item -> item.getOrderItemCourse())
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));

        model.addAttribute("currentUser", user);
        model.addAttribute("myOrders", myOrders);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "user/dashboard";
    }

    @GetMapping("/syllabus/{courseId}")
    public ResponseEntity<byte[]> downloadSyllabus(Principal principal, @PathVariable Long courseId) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        String email = principal.getName();
        var userOpt = cartService.getUserByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        boolean isEnrolled = cartService.getOrdersForUser(email).stream()
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(item -> item.getOrderItemCourse() != null
                        && item.getOrderItemCourse().getId().equals(courseId));

        if (!isEnrolled) {
            return ResponseEntity.status(403).build();
        }

        var courseOpt = cartService.findCourseById(courseId);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var course = courseOpt.get();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Syllabus for " + course.getName(), titleFont));
            document.add(new Paragraph("Author: " + course.getAuthor(), normalFont));
            document.add(new Paragraph(
                    "Type: " + (course.getCourseType() != null ? course.getCourseType().name() : "N/A"), normalFont));
            document.add(new Paragraph(
                    "Price: " + (course.getPrice() != null ? course.getPrice().toString() + " VND" : "N/A"),
                    normalFont));
            document.add(new Paragraph("", normalFont));

            if (course.getDescription() != null && !course.getDescription().isBlank()) {
                document.add(new Paragraph("Description:", headerFont));
                document.add(new Paragraph(course.getDescription(), normalFont));
                document.add(new Paragraph("", normalFont));
            }

            List<Module> modules = new ArrayList<>(course.getModules());
            modules.sort(Comparator.comparing(Module::getModuleNumber));

            if (modules.isEmpty()) {
                document.add(new Paragraph("No modules are available for this course.", normalFont));
            } else {
                for (Module module : modules) {
                    document.add(
                            new Paragraph("Module " + module.getModuleNumber() + ": " + module.getTitle(), headerFont));
                    if (module.getDescription() != null && !module.getDescription().isBlank()) {
                        document.add(new Paragraph(module.getDescription(), normalFont));
                    }

                    List<Lesson> lessons = new ArrayList<>(module.getLessons());
                    lessons.sort(Comparator.comparing(Lesson::getLessonNumber));
                    if (!lessons.isEmpty()) {
                        for (Lesson lesson : lessons) {
                            String lessonText = "  - Lesson " + lesson.getLessonNumber() + ": " + lesson.getTitle();
                            if (lesson.getDurationMinutes() != null) {
                                lessonText += " (" + lesson.getDurationMinutes() + " mins)";
                            }
                            document.add(new Paragraph(lessonText, normalFont));
                        }
                    }
                    document.add(new Paragraph("", normalFont));
                }
            }

            document.close();

            byte[] pdfBytes = baos.toByteArray();
            String filename = "syllabus-" + course.getId() + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/topup")
    public String topUp(Principal principal, Model model, @RequestParam("amount") java.math.BigDecimal amount) {
        if (principal == null) {
            return "redirect:/signin";
        }
        try {
            cartService.topUpBalance(principal.getName(), amount);
            model.addAttribute("successMessage", "Nạp tiền thành công: " + amount + " VNĐ");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return dashboard(principal, model);
    }
}
