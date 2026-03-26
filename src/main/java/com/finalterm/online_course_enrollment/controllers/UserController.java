package com.finalterm.online_course_enrollment.controllers;

import com.finalterm.online_course_enrollment.models.User;
import com.finalterm.online_course_enrollment.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

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
