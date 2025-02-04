package com.avnish.to_do_app.service;

import com.avnish.to_do_app.entity.Category;
import com.avnish.to_do_app.entity.CategoryDTO;
import com.avnish.to_do_app.entity.Task;
import com.avnish.to_do_app.entity.TaskDTO;
import com.avnish.to_do_app.mapper.TaskMapper;
import com.avnish.to_do_app.repo.CategoryRepo;
import com.avnish.to_do_app.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    private final CategoryRepo categoryRepo;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepo taskRepo,CategoryRepo categoryRepo,TaskMapper taskMapper){
        this.taskRepo = taskRepo;
        this.categoryRepo = categoryRepo;
        this.taskMapper = taskMapper;
    }



    public List<Task> allTask(){
        return taskRepo.findAll();
    }

    @Transactional
    public ResponseEntity<Task> updateTask(Long id, TaskDTO task){
        Optional<Task> byId = taskRepo.findById(id);
        if(byId.isPresent()){
            Task task1 = byId.get();

            if(task.getTitle() != null)
                task1.setTitle(task.getTitle());
            if(task.getDescription() != null)
                task1.setDescription(task.getDescription());
            if(task.isCompleted() != task1.isCompleted())
                task1.setCompleted(task.isCompleted());
            if(task.getCategories() != null){
                Set<Category> categories = task.getCategories().stream().map(categoryDTO -> {
                    return categoryRepo.findByCategory(categoryDTO.getCategory()).orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setCategory(categoryDTO.getCategory());
                        return categoryRepo.save(newCategory);
                    });
                }).collect(Collectors.toSet());
                task1.getCategories().addAll(categories);
            }
            taskRepo.save(task1);
            return new ResponseEntity<>(task1,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Transactional
    public ResponseEntity<String> deleteTask(long id) {
        Optional<Task> byId = taskRepo.findById(id);
        if(byId.isPresent()){
            Task task = byId.get();
            taskRepo.delete(task);
            return ResponseEntity.ok("Task " + id + " successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not available");
    }

    public ResponseEntity<List<Task>> getTaskByTitle(String title) {
        return ResponseEntity.ok().body(taskRepo.findByTitleContaining(title));
    }

    @Transactional
    public Task addTask(TaskDTO taskDTO) {
//        Task task = new Task();
//        task.setTitle(taskDTO.getTitle());
//        task.setDescription(taskDTO.getDescription());
//        task.setCompleted(taskDTO.isCompleted());

        Task task = taskMapper.taskDTOtoTask(taskDTO);

        Set<Category> categories = taskDTO.getCategories()
                .stream()
                .map(categoryDTO -> {
                    return categoryRepo.findByCategory(categoryDTO.getCategory()).orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setCategory(categoryDTO.getCategory());
                        return categoryRepo.save(newCategory);
                    });
                }).collect(Collectors.toSet());

        task.setCategories(categories);

        return taskRepo.save(task);
    }

    @Transactional
    public boolean deleteTaskCategory(long tid, List<Long> cids) {
        Optional<Task> byId = taskRepo.findById(tid);
        if(byId.isPresent()){
            Task task = byId.get();

            List<Category> allById = categoryRepo.findAllById(cids);
            Set<Category> categories = task.getCategories();
            if (categories.size() == allById.size()) {
                allById.forEach(categories::remove);
                task.setCategories(categories);
                taskRepo.save(task);
                return true;
            }
            return false;
        }
        return false;
    }

    public ResponseEntity<Set<Task>> getTaskByCategory(String category) {
        Optional<Category> byCategory = categoryRepo.findByCategory(category);
        if(byCategory.isPresent()){
            Category category1 = byCategory.get();
            return ResponseEntity.ok(category1.getTasks());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public List<String> getTitles() {
        List<Task> allTasks = taskRepo.findAll();
//        List<String> titles = new ArrayList<>();
//        allTasks.forEach(task -> titles.add(task.getTitle()));
        return allTasks.stream().map(Task::getTitle).toList();
    }

//    @Transactional
//    public ResponseEntity<Task> deleteTaskCategory(long id, List<CategoryDTO> categoryDTOList) {
//        Optional<Task> byId = taskRepo.findById(id);
//        if(byId.isPresent()){
//            Task task = byId.get();
//            Set<Category> categories = task.getCategories().stream().filter(category -> !categoryDTOList.contains(category)).collect(Collectors.toSet());
//
//            task.setCategories(categories);
//            return ResponseEntity.ok(taskRepo.save(task));
//        }
//        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
//    }

//    @Transactional
//    public ResponseEntity<Task> deleteTaskCategory(long tid,long cid){
//        Optional<Task> byId = taskRepo.findById(tid);
//        if(byId.isPresent()){
//            Optional<Category> byId1 = categoryRepo.findById(cid);
//            if(byId1.isPresent()){
//                Category category = byId1.get();
//                Task task = byId.get();
//
//                Set<Category> categories = task.getCategories();
//
//                if(categories.contains(category)){
//                    categories.remove(category);
//                    task.setCategories(categories);
//                    return ResponseEntity.ok(taskRepo.save(task));
//                }
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }

}
