package com.app.questing.controller;

import com.app.questing.dto.TodoCreateRequest;
import com.app.questing.dto.TodoResponse;
import com.app.questing.dto.TodoUpdateRequest;
import com.app.questing.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<TodoResponse> getTodayTodos(){
        return todoService.getTodayTodos();
    }

    @PostMapping
    public TodoResponse createTodo(@RequestBody TodoCreateRequest request) {
        return todoService.createTodo(request);
    }

    @PutMapping("/{todoId}")
    public TodoResponse updateTodo(@PathVariable Long todoId,
                                   @RequestBody TodoUpdateRequest request) {
        return todoService.updateTodo(todoId, request);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable Long todoId){
        todoService.deleteTodo(todoId);
    }

    @PatchMapping("/{todoId}/complete")
    public TodoResponse completeTodo(@PathVariable Long todoId){
        return todoService.completeTodo(todoId);
    }

}
