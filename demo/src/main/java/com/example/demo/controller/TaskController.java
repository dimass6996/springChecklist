package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    private Task findTaskOrThrow(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Задачи " + id + " не существует"
                ));
    }

    @PostMapping("/tasks")
    public Task create(@RequestBody Task task){
        return taskRepository.save(task);
    }
    @PutMapping("/tasks/{id}")
    public Task edit(@RequestBody Task newTask, @PathVariable long id) {
        Task task = findTaskOrThrow(id);

        task.setDate(newTask.getDate());
        task.setDescription(newTask.getDescription());
        task.setDone(newTask.isDone());

        return taskRepository.save(task);
    }



    @PutMapping("/tasks/{id}:mark-as-undone")
    public Task markAsUndone(@PathVariable long id){
        Task task = findTaskOrThrow(id);
        task.setDone(false);
        taskRepository.save(task);
        return task;
    }



    @DeleteMapping("/tasks/{id}")
    public void removeById(@PathVariable long id){
        boolean exists = taskRepository.existsById(id);
        if(!exists){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Задачи " + id + "не существует");
        }
        taskRepository.deleteById(id);

    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Задачи " + id + "не существует"
                ));
        return task;
    }

    @GetMapping("/tasks")
    public List<Task> getAll(){
        return taskRepository.findAll();
    }
}
