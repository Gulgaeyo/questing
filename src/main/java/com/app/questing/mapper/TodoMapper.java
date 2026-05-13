package com.app.questing.mapper;

import com.app.questing.dto.todo.TodoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TodoMapper {

    List<TodoDTO> findTodosByDate(@Param("userId") Long userId,
                                  @Param("todoDate") LocalDate todoDate);

    TodoDTO findTodoById(@Param("id") Long id,
                         @Param("userId") Long userId);

    void insertTodo(TodoDTO todo);

    int updateTodo(TodoDTO todo);

    int deleteTodo(@Param("id") Long id,
                   @Param("userId") Long userId);

    int completeTodo(@Param("id") Long id,
                     @Param("userId") Long userId);



}
