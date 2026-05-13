package com.app.questing.service;

import com.app.questing.dto.quest.QuestCompleteResponse;
import com.app.questing.dto.stat.UserStatResult;
import com.app.questing.dto.todo.TodoCreateRequest;
import com.app.questing.dto.todo.TodoDTO;
import com.app.questing.dto.todo.TodoResponse;
import com.app.questing.dto.todo.TodoUpdateRequest;
import com.app.questing.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoMapper todoMapper;
    private final UserStatService userStatService;
    private static final Long TEMP_USER_ID = 1L;
    private final RewardMessageService rewardMessageService;

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
        todo.setIsCompleted(false);

        todoMapper.insertTodo(todo);

        TodoDTO savedTodo = todoMapper.findTodoById(todo.getId(), TEMP_USER_ID);
        return toResponse(savedTodo);
    }

    public TodoResponse updateTodo(Long todoId, TodoUpdateRequest request) {
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

    public QuestCompleteResponse completeTodo(Long todoId){
        TodoDTO todo = todoMapper.findTodoById(todoId, TEMP_USER_ID);

        if(todo == null){
            throw new IllegalArgumentException("존재하지 않는 TODO입니다.");
        }

        if(Boolean.TRUE.equals(todo.getIsCompleted())){
            return toAlreadyCompletedResponse(todo);
        }

        int completedCount = todoMapper.completeTodo(todoId, TEMP_USER_ID);

        if (completedCount == 0){
            throw new IllegalArgumentException("TODO 완료 처리에 실패했습니다.");
        }

        UserStatResult statResult = userStatService.addProgress(
                TEMP_USER_ID,
                todo.getCategory(),
                todo.getDurationTime()
        );

        String message = rewardMessageService.createRewardMessage(statResult);

        TodoDTO completedTodo = todoMapper.findTodoById(todoId, TEMP_USER_ID);

        return toQuestCompleteResponse(completedTodo, statResult);
    }

    private QuestCompleteResponse toQuestCompleteResponse(TodoDTO todo,
                                                          UserStatResult statResult) {
        QuestCompleteResponse response = new QuestCompleteResponse();

        response.setQuestType("TODO");
        response.setQuestId(todo.getId());
        response.setIsCompleted(true);

        response.setCompletedDate(LocalDate.now());
        response.setCompletedAt(todo.getCompletedAt());

        response.setCategory(todo.getCategory());
        response.setDurationTime(todo.getDurationTime());
        response.setEarnedExp(todo.getDurationTime());

        response.setGainedStat(statResult.getGainedStat());
        response.setRemainingMinutes(statResult.getRemainingMinutes());
        response.setCurrentStat(statResult.getCurrentStat());

        response.setMessage(rewardMessageService.createRewardMessage(statResult));

        return response;
    }

    private QuestCompleteResponse toAlreadyCompletedResponse(TodoDTO todo){
        QuestCompleteResponse response = new QuestCompleteResponse();

        response.setQuestType("TODO");
        response.setQuestId(todo.getId());
        response.setIsCompleted(true);

        response.setCompletedDate(LocalDate.now());
        response.setCompletedAt(todo.getCompletedAt());

        response.setCategory(todo.getCategory());
        response.setDurationTime(todo.getDurationTime());
        response.setEarnedExp(todo.getDurationTime());

        response.setGainedStat(0);
        response.setRemainingMinutes(null);
        response.setCurrentStat(null);

        response.setMessage(rewardMessageService.createAlreadyCompletedMessage());

        return response;
    }

    private TodoResponse toResponse(TodoDTO todo){
        TodoResponse response = new TodoResponse();

        response.setId(todo.getId());
        response.setUserId(todo.getUserId());
        response.setTitle(todo.getTitle());
        response.setCategory(todo.getCategory());
        response.setTimeSlot(todo.getTimeSlot());
        response.setIsCompleted(todo.getIsCompleted());
        response.setDurationTime(todo.getDurationTime());
        response.setCreatedAt(todo.getCreatedAt());
        response.setUpdatedAt(todo.getUpdatedAt());

        return response;
    }
}
