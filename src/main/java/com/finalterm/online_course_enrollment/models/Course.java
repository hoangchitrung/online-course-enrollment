package com.finalterm.online_course_enrollment.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.finalterm.online_course_enrollment.models.enums.CourseType;
import com.finalterm.online_course_enrollment.models.enums.CourseTypeConverter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "courses")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @NotBlank
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull
    @Convert(converter = CourseTypeConverter.class)
    @Column(name = "type", nullable = false)
    private CourseType courseType;

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "modules_count", nullable = false)
    private int modulesCount = 0;

    @Column(name = "enrollment_count", nullable = false)
    private int enrollmentCount = 0;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<CourseCohort> cohorts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<Module> modules = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "courseCart")
    private Set<Cart> carts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "orderItemCourse")
    private Set<OrderItem> orderItems = new HashSet<>();

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getModulesCount() {
        return modulesCount;
    }

    public void setModulesCount(int modulesCount) {
        this.modulesCount = modulesCount;
    }

    public int getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(int enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
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

    public Set<CourseCohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(Set<CourseCohort> cohorts) {
        this.cohorts = cohorts;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

}
