package com.avnish.to_do_app.controller;

import com.avnish.to_do_app.entity.CategoryDTO;
import com.avnish.to_do_app.entity.Task;
import com.avnish.to_do_app.entity.TaskDTO;
import com.avnish.to_do_app.service.TaskService;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.addTask(taskDTO));
    }

    @GetMapping(value = "/tasks")
    public ResponseEntity<List<Task>> allTask(){
        return ResponseEntity.ok(taskService.allTask());
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

    @GetMapping(value = "/taskbycategory")
    public ResponseEntity<Set<Task>> getTaskByCategory(@RequestParam String category){
        return taskService.getTaskByCategory(category);
    }

    @DeleteMapping(value = "/task/category")
    public ResponseEntity<String> deleteCategory(@RequestParam long tid, @RequestBody List<Long> cids){
        if(taskService.deleteTaskCategory(tid, cids))
            return ResponseEntity.ok("Categories removed from task successfully");
        else
            return ResponseEntity.ok("Task not found or some categories do not exist");
    }

    @GetMapping("/task/title")
    public ResponseEntity<List<String>> getAllTitles(){
        return ResponseEntity.ok(taskService.getTitles());
    }

    @GetMapping("/task/dynamic/{show}")
    public MappingJacksonValue dynamic(@PathVariable String show){
        List<Task> tasks = taskService.allTask();
        MappingJacksonValue jacksonValue = new MappingJacksonValue(tasks);
        if(show.equals("true")){
            SimpleBeanPropertyFilter propertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("title");
            SimpleFilterProvider taskFilter = new SimpleFilterProvider().addFilter("TaskFilter", propertyFilter);
            jacksonValue.setFilters(taskFilter);
        }
        return jacksonValue;
    }
}
