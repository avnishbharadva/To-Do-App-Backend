package com.avnish.to_do_app.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@JsonIgnoreProperties(value = {"id","atCreated"})
@JsonFilter("TaskFilter")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    private boolean isCompleted;
    private LocalDateTime atCreated;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "task_category_table",
            joinColumns = {
                @JoinColumn(name = "task_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "category_id",referencedColumnName = "id")
            }
    )
    @JsonManagedReference
    private Set<Category> categories = new HashSet<>();

    public Task() {
    }

    public Task(long id, String title, String description, boolean isCompleted, Set<Category> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.categories = categories;
    }

    @PrePersist
    protected void onCreate() {
        this.atCreated = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDateTime getAtCreated() {
        return atCreated;
    }

    public void setAtCreated(LocalDateTime atCreated) {
        this.atCreated = atCreated;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
