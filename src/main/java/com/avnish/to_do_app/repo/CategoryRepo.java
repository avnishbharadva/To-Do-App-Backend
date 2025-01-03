package com.avnish.to_do_app.repo;

import com.avnish.to_do_app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Optional<Category> findByCategory(String category);
}
