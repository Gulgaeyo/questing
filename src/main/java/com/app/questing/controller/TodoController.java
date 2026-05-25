package com.app.questing.controller;

import com.app.questing.dto.quest.QuestCompleteResponse;
import com.app.questing.dto.todo.TodoCreateRequest;
import com.app.questing.dto.todo.TodoResponse;
import com.app.questing.dto.todo.TodoUpdateRequest;
import com.app.questing.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<TodoResponse> getTodayTodos(@RequestAttribute Long userId){
        return todoService.getTodayTodos(userId);
    }

    @PostMapping
    public TodoResponse createTodo(@RequestAttribute Long userId,
                                   @Valid @RequestBody TodoCreateRequest request) {
        return todoService.createTodo(userId, request);
    }

    @PutMapping("/{todoId}")
    public TodoResponse updateTodo(@RequestAttribute Long userId,
                                   @PathVariable Long todoId,
                                   @Valid @RequestBody TodoUpdateRequest request) {
        return todoService.updateTodo(userId, todoId, request);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@RequestAttribute Long userId,
                           @PathVariable Long todoId){
        todoService.deleteTodo(userId, todoId);
    }

    @PatchMapping("/{todoId}/complete")
    public QuestCompleteResponse completeTodo(@RequestAttribute Long userId,
                                              @PathVariable Long todoId){
        return todoService.completeTodo(userId, todoId);
    }

}
