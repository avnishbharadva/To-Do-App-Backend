package com.avnish.to_do_app.repo;

import com.avnish.to_do_app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {

    List<Task> findByTitleContaining(String title);
}
