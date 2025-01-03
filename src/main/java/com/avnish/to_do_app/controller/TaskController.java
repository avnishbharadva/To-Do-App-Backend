package com.avnish.to_do_app.controller;

import com.avnish.to_do_app.entity.CategoryDTO;
import com.avnish.to_do_app.entity.Task;
import com.avnish.to_do_app.entity.TaskDTO;
import com.avnish.to_do_app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping(value = "/task",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO taskDTO){
//        task.setAtCreated();
        return taskService.addTask(taskDTO);
    }

    @GetMapping(value = "/tasks")
    public ResponseEntity<List<Task>> allTask(){
        return taskService.allTask();
    }

    @PutMapping(value = "/task")
    public ResponseEntity<Task> updateTask(@RequestParam long id, @RequestBody TaskDTO taskDTO){
        return taskService.updateTask(id,taskDTO);
    }

    @DeleteMapping(value = "/task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id){
        return taskService.deleteTask(id);
    }

    @GetMapping(value = "/task")
    public ResponseEntity<List<Task>> getTaskByTitle(@RequestParam String title){
        return taskService.getTaskByTitle(title);
    }

    @DeleteMapping(value = "/task/category")
    public ResponseEntity<String> deleteCategory(@RequestParam long tid, @RequestBody List<Long> cids){
        if(taskService.deleteTaskCategory(tid, cids))
            return ResponseEntity.ok("Categories removed from task successfully");
        else
            return ResponseEntity.ok("Task not found or some categories do not exist");
    }
}
