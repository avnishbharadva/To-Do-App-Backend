package com.avnish.to_do_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String category;
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "categories",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Task> tasks = new HashSet<>();

    public Category() {
    }

    public Category(long id, String category, Set<Task> tasks) {
        this.id = id;
        this.category = category;
        this.tasks = tasks;
    }

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
