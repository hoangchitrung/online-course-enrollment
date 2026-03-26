package com.finalterm.online_course_enrollment.services;

import org.springframework.stereotype.Service;
import com.finalterm.online_course_enrollment.models.Cart;
import com.finalterm.online_course_enrollment.models.Course;
import com.finalterm.online_course_enrollment.models.CourseCohort;
import com.finalterm.online_course_enrollment.models.Order;
import com.finalterm.online_course_enrollment.models.OrderItem;
import com.finalterm.online_course_enrollment.models.User;
import com.finalterm.online_course_enrollment.repositories.CartRepository;
import com.finalterm.online_course_enrollment.repositories.CourseCohortRepository;
import com.finalterm.online_course_enrollment.repositories.CourseRepository;
import com.finalterm.online_course_enrollment.repositories.OrderItemRepository;
import com.finalterm.online_course_enrollment.repositories.OrderRepository;
import com.finalterm.online_course_enrollment.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseCohortRepository courseCohortRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, CourseRepository courseRepository,
            CourseCohortRepository courseCohortRepository, OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseCohortRepository = courseCohortRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public void addCourseToCart(String username, Long courseId, Long cohortId) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Nếu là LIVE_BOOT_CAMP mà không có cohortId thì báo lỗi
        if (course.getCourseType() != null && course.getCourseType().name().equals("LIVE_BOOT_CAMP")
                && cohortId == null) {
            throw new IllegalArgumentException("Bạn phải chọn lớp học (cohort) cho khóa học Live Boot Camp.");
        }

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
            CourseCohort cohort = courseCohortRepository.findById(cohortId)
                    .orElseThrow(() -> new IllegalArgumentException("Cohort not found"));
            if (!cohort.getCourse().getId().equals(courseId)) {
                throw new IllegalArgumentException("Cohort does not belong to this course.");
            }
            // Only allow UPCOMING cohort enrollments
            if (cohort
                    .getCourseCohortStatus() != com.finalterm.online_course_enrollment.models.enums.CourseCohortStatus.UPCOMING) {
                throw new IllegalArgumentException("Chỉ có thể đăng ký các lớp UPCOMING.");
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

    public java.util.Optional<Course> findCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public java.util.List<CourseCohort> findCohortsByCourseId(Long courseId) {
        return courseCohortRepository.findByCourseId(courseId);
    }

    public java.util.Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public java.util.Optional<Cart> findCartItemById(Long cartItemId) {
        return cartRepository.findById(cartItemId);
    }

    public void upgradeCartItem(Long userId, Long cartItemId, Long cohortId) {
        Cart cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (!cartItem.getUserCart().getId().equals(userId)) {
            throw new IllegalArgumentException("Không có quyền nâng cấp mục này.");
        }

        if (cartItem.getCourseCart()
                .getCourseType() != com.finalterm.online_course_enrollment.models.enums.CourseType.SELF_PACED) {
            throw new IllegalArgumentException("Chỉ khóa học Self-Paced mới có thể nâng cấp.");
        }

        CourseCohort cohort = courseCohortRepository.findById(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohort không tồn tại"));

        if (!cohort.getCourse().getId().equals(cartItem.getCourseCart().getId())) {
            throw new IllegalArgumentException("Cohort không thuộc khóa học này.");
        }

        if (cohort
                .getCourseCohortStatus() != com.finalterm.online_course_enrollment.models.enums.CourseCohortStatus.UPCOMING) {
            throw new IllegalArgumentException("Chỉ có thể chọn cohort đang UPCOMING.");
        }

        cartItem.setCourseCohort(cohort);
        cartRepository.save(cartItem);
    }

    public void topUpBalance(String username, java.math.BigDecimal amount) {
        if (amount == null || amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0.");
        }
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    public java.util.List<Order> getOrdersForUser(String username) {
        return orderRepository.findByUserOrder_Email(username);
    }

    public void checkoutWithBalance(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Cart> cartItems = getCartForUser(username);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng trống.");
        }
        java.math.BigDecimal totalPrice = cartItems.stream()
                .map(Cart::getPrice)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        java.math.BigDecimal discountRate = cartItems.size() >= 3 ? new java.math.BigDecimal("0.10")
                : java.math.BigDecimal.ZERO;
        java.math.BigDecimal discountAmount = totalPrice.multiply(discountRate).setScale(2,
                java.math.RoundingMode.HALF_UP);
        java.math.BigDecimal finalPrice = totalPrice.subtract(discountAmount).setScale(2,
                java.math.RoundingMode.HALF_UP);

        if (user.getBalance().compareTo(finalPrice) < 0) {
            throw new IllegalArgumentException("Số dư không đủ. Vui lòng nạp thêm.");
        }

        user.setBalance(user.getBalance().subtract(finalPrice));
        userRepository.save(user);

        // Tao order va order item
        Order order = new Order();
        order.setUserOrder(user);
        order.setTotalPrice(totalPrice);
        order.setDiscountPrice(discountAmount);
        order.setFinalPrice(finalPrice);
        order.setOrderStatus(com.finalterm.online_course_enrollment.models.enums.OrderStatus.PAID);
        orderRepository.save(order);

        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemOrder(order);
            orderItem.setOrderItemCourse(cartItem.getCourseCart());
            orderItem.setOrderItemCohort(cartItem.getCourseCohort());
            orderItem.setPriceAtPurchase(cartItem.getPrice());
            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteAll(cartItems);
    }

    public void removeCartItem(Long userId, Long cartItemId) {
        Cart cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (!cartItem.getUserCart().getId().equals(userId)) {
            throw new IllegalArgumentException("Không có quyền xóa mục này");
        }
        cartRepository.delete(cartItem);
    }
}
