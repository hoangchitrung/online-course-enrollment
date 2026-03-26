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
                            Principal principal) {
        if (principal == null) {
            return "redirect:/signin";
        }
        cartService.addCourseToCart(principal.getName(), courseId, cohortId);
        return "redirect:/cart";
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
