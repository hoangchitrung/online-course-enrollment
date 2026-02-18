package com.finalterm.online_course_enrollment.models;

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
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(name = "unique_enrollment", columnNames = { "user_id", "course_id" })
})
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User userEnrollment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course courseEnrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order orderEnrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cohort_id")
    private CourseCohort cohortEnrollment;

    @Column(name = "type", nullable = false)
    private String type = "SELF_PACED";

    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;

    @Column(name = "completed_lessons")
    private Integer completedLessons = 0;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Enrollment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserEnrollment() {
        return userEnrollment;
    }

    public void setUserEnrollment(User userEnrollment) {
        this.userEnrollment = userEnrollment;
    }

    public Course getCourseEnrollment() {
        return courseEnrollment;
    }

    public void setCourseEnrollment(Course courseEnrollment) {
        this.courseEnrollment = courseEnrollment;
    }

    public Order getOrderEnrollment() {
        return orderEnrollment;
    }

    public void setOrderEnrollment(Order orderEnrollment) {
        this.orderEnrollment = orderEnrollment;
    }

    public CourseCohort getCohortEnrollment() {
        return cohortEnrollment;
    }

    public void setCohortEnrollment(CourseCohort cohortEnrollment) {
        this.cohortEnrollment = cohortEnrollment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Integer getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(Integer completedLessons) {
        this.completedLessons = completedLessons;
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

    
}
