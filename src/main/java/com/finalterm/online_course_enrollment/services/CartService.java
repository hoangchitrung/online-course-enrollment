package com.finalterm.online_course_enrollment.services;

import org.springframework.stereotype.Service;
import com.finalterm.online_course_enrollment.models.Cart;
import com.finalterm.online_course_enrollment.models.Course;
import com.finalterm.online_course_enrollment.models.CourseCohort;
import com.finalterm.online_course_enrollment.models.User;
import com.finalterm.online_course_enrollment.repositories.CartRepository;
import com.finalterm.online_course_enrollment.repositories.CourseCohortRepository;
import com.finalterm.online_course_enrollment.repositories.CourseRepository;
import com.finalterm.online_course_enrollment.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseCohortRepository courseCohortRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, CourseRepository courseRepository, CourseCohortRepository courseCohortRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseCohortRepository = courseCohortRepository;
    }

    @Transactional
    public void addCourseToCart(String username, Long courseId, Long cohortId) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Check if the item is already in the cart
        Optional<Cart> existingCartItem = cartRepository.findByUserCartAndCourseCart(user, course);
        if (existingCartItem.isPresent()) {
            // Or maybe update the cohort? For now, we just ignore.
            return;
        }

        Cart cartItem = new Cart();
        cartItem.setUserCart(user);
        cartItem.setCourseCart(course);
        cartItem.setPrice(course.getPrice());

        if (cohortId != null) {
            CourseCohort cohort = courseCohortRepository.findById(cohortId).orElseThrow(() -> new IllegalArgumentException("Cohort not found"));
            if (!cohort.getCourse().getId().equals(courseId)) {
                throw new IllegalArgumentException("Cohort does not belong to this course.");
            }
            cartItem.setCourseCohort(cohort);
        }

        cartRepository.save(cartItem);
    }

    public List<Cart> getCartForUser(String username) {
        return cartRepository.findByUserCart_Email(username);
    }

    public int getCartItemCount(String username) {
        return cartRepository.countByUserCart_Email(username);
    }
}
