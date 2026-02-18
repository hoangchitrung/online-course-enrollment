package com.finalterm.online_course_enrollment.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(name = "unique_cart_item", columnNames = { "user_id",
        "course_id" }))
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "selected_cohort_date")
    private LocalDate selectedCohortDate;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User userCart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course courseCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_cohort_id")
    private CourseCohort courseCohort;

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSelectedCohortDate() {
        return selectedCohortDate;
    }

    public void setSelectedCohortDate(LocalDate selectedCohortDate) {
        this.selectedCohortDate = selectedCohortDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUserCart() {
        return userCart;
    }

    public void setUserCart(User userCart) {
        this.userCart = userCart;
    }

    public Course getCourseCart() {
        return courseCart;
    }

    public void setCourseCart(Course courseCart) {
        this.courseCart = courseCart;
    }

    public CourseCohort getCourseCohort() {
        return courseCohort;
    }

    public void setCourseCohort(CourseCohort courseCohort) {
        this.courseCohort = courseCohort;
    }

}
