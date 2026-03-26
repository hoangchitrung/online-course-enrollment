package com.finalterm.online_course_enrollment.controllers;

import com.finalterm.online_course_enrollment.models.Cart;
import com.finalterm.online_course_enrollment.models.CourseCohort;
import com.finalterm.online_course_enrollment.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
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
        var userOpt = cartService.getUserByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/signin";
        }

        List<Cart> cartItems = cartService.getCartForUser(principal.getName());

        java.util.Map<Long, java.util.List<CourseCohort>> cohortOptions = new java.util.HashMap<>();
        cartItems.forEach(item -> {
            if (item.getCourseCart()
                    .getCourseType() == com.finalterm.online_course_enrollment.models.enums.CourseType.SELF_PACED) {
                var cohorts = cartService.findCohortsByCourseId(item.getCourseCart().getId());
                cohortOptions.put(item.getId(), cohorts);
            }
        });

        var totalPrice = cartItems.stream()
                .map(Cart::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        BigDecimal discountRate = BigDecimal.ZERO;
        if (cartItems.size() >= 3) {
            discountRate = new BigDecimal("0.10"); // 10% discount for 3 or more items
        }

        BigDecimal discountAmount = totalPrice.multiply(discountRate).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal finalPrice = totalPrice.subtract(discountAmount).setScale(2, java.math.RoundingMode.HALF_UP);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("courseCohortMap", cohortOptions);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("discountRate", discountRate);
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("finalPrice", finalPrice);
        return "cart";
    }

    @PostMapping("/upgrade/{cartItemId}")
    public String upgradeCartItem(@PathVariable Long cartItemId,
            @RequestParam Long cohortId,
            Principal principal,
            Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        var userOpt = cartService.getUserByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/signin";
        }
        try {
            cartService.upgradeCartItem(userOpt.get().getId(), cartItemId, cohortId);
            return "redirect:/cart";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return viewCart(principal, model);
        }
    }

    @PostMapping("/remove/{cartItemId}")
    public String removeCartItem(@PathVariable Long cartItemId, Principal principal) {
        if (principal == null) {
            return "redirect:/signin";
        }
        var user = cartService.getUserByEmail(principal.getName());
        if (user.isEmpty()) {
            return "redirect:/signin";
        }
        cartService.removeCartItem(user.get().getId(), cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/signin";
        }
        try {
            cartService.checkoutWithBalance(principal.getName());
            return "payment-success";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return viewCart(principal, model);
        }
    }
}
