package com.app.questing.service;

import com.app.questing.dto.TodoCreateRequest;
import com.app.questing.dto.TodoDTO;
import com.app.questing.dto.TodoResponse;
import com.app.questing.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.TE;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoMapper todoMapper;

    private static final Long TEMP_USER_ID = 1L;

    public List<TodoResponse> getTodayTodos(){
        LocalDate today = LocalDate.now();

        return todoMapper.findTodosByDate(TEMP_USER_ID, today)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TodoResponse createTodo(TodoCreateRequest request){
        TodoDTO todo = new TodoDTO();

        todo.setUserId(TEMP_USER_ID);
        todo.setTitle(request.getTitle());
        todo.setTodoDate(LocalDate.now());
        todo.setTimeSlot(request.getTimeSlot());
        todo.setDurationTime(request.getDurationTime());
        todo.setCategory(request.getCategory());
        todo.setCompleted(false);

        todoMapper.insertTodo(todo);

        TodoDTO savedTodo = todoMapper.findTodoById(todo.getId(), TEMP_USER_ID);
        return toResponse(savedTodo);
    }

    public TodoResponse updateTodo(Long todoId, TodoCreateRequest request) {
        TodoDTO todo = todoMapper.findTodoById(todoId, TEMP_USER_ID);

        if(todo == null) {
            throw new IllegalArgumentException("존재하지 않는 TODO입니다.");
        }

        todo.setTitle(request.getTitle());
        todo.setCategory(request.getCategory());
        todo.setTimeSlot(request.getTimeSlot());
        todo.setDurationTime(request.getDurationTime());

        int updatedCount = todoMapper.updateTodo(todo);

        if(updatedCount == 0) {
            throw new IllegalArgumentException("TODO 수정에 실패했습니다.");
        }

        TodoDTO updatedTodo = todoMapper.findTodoById(todoId, TEMP_USER_ID);
        return toResponse(updatedTodo);
    }

    public void deleteTodo(Long todoId){
        int deletedCount = todoMapper.deleteTodo(todoId, TEMP_USER_ID);

        if (deletedCount == 0){
            throw new IllegalArgumentException("존재하지 않는 TODO 입니다.");
        }
    }

    public TodoResponse completeTodo(Long todoId){
        TodoDTO todo = todoMapper.findTodoById(todoId, TEMP_USER_ID);

        if(todo == null){
            throw new IllegalArgumentException("존재하지 않는 TODO입니다.");
        }

        if(Boolean.TRUE.equals(todo.getCompleted())){
            return toResponse(todo);
        }

        int completedCount = todoMapper.completeTodo(todoId, TEMP_USER_ID);

        if (completedCount == 0){
            throw new IllegalArgumentException("TODO 완료 처리에 실패했습니다.");
        }

        TodoDTO completedTodo = todoMapper.findTodoById(todoId, TEMP_USER_ID);
        return toResponse(completedTodo);
    }

    private TodoResponse toResponse(TodoDTO todo){
        TodoResponse response = new TodoResponse();

        response.setId(todo.getId());
        response.setUserId(todo.getUserId());
        response.setTitle(todo.getTitle());
        response.setCategory(todo.getCategory());
        response.setTimeSlot(todo.getTimeSlot());
        response.setCompleted(todo.getCompleted());
        response.setDurationTime(todo.getDurationTime());
        response.setCreatedAt(todo.getCreatedAt());
        response.setUpdatedAt(todo.getUpdatedAt());

        return response;
    }
}
