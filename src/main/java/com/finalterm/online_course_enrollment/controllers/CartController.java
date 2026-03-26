package com.finalterm.online_course_enrollment.controllers;

import com.finalterm.online_course_enrollment.models.Cart;
import com.finalterm.online_course_enrollment.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/{courseId}")
    public String addToCart(@PathVariable Long courseId,
            @RequestParam(required = false) Long cohortId,
            Principal principal,
            Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        try {
            cartService.addCourseToCart(principal.getName(), courseId, cohortId);
            return "redirect:/cart";
        } catch (IllegalArgumentException ex) {
            // Lấy lại dữ liệu để render lại trang chi tiết khóa học
            var courseOpt = cartService.findCourseById(courseId);
            if (courseOpt.isEmpty())
                return "redirect:/";
            var course = courseOpt.get();
            model.addAttribute("course", course);
            var modules = cartService.findCourseById(courseId)
                    .map(c -> c.getModules()).orElse(java.util.Collections.emptySet());
            model.addAttribute("modules", modules);
            if (course.getCourseType() != null && course.getCourseType().name().equals("LIVE_BOOT_CAMP")) {
                model.addAttribute("cohorts", cartService.findCohortsByCourseId(courseId));
                model.addAttribute("selectedCohortId", cohortId);
            }
            model.addAttribute("errorMessage", ex.getMessage());
            return "course-detail";
        }
    }

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        List<Cart> cartItems = cartService.getCartForUser(principal.getName());
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }
}
