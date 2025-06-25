package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.CustomUserDetails;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.todo.repository.TodoRepositoryQuery;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;
    private final TodoRepositoryQuery todoRepositoryQuery;


    @Transactional
    public TodoSaveResponse saveTodo(CustomUserDetails userDetails, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(userDetails);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail(), user.getNickname())
        );
    }

    public Page<TodoResponse> getTodos(int page, int size, String weather, LocalDate startDate, LocalDate endDate){
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Todo> todos;

        boolean hasWeather = weather != null && !weather.isBlank();
        boolean hasStart = startDate != null;
        boolean hasEnd = endDate != null;

        if (hasWeather && hasStart && hasEnd) {
            todos = todoRepository.findByWeatherAndDateRange(weather, startDate, endDate, pageable);
        } else if (hasWeather && hasStart) {
            todos = todoRepository.findByWeatherFromStartDate(weather, startDate, pageable);
        } else if (hasWeather && hasEnd) {
            todos = todoRepository.findByWeatherUntilEndDate(weather, endDate, pageable);
        } else if (hasWeather) {
            todos = todoRepository.findByWeatherContainingOrderByModifiedAtDesc(weather, pageable);
        } else if (hasStart && hasEnd) {
            todos = todoRepository.findByDateRange(startDate, endDate, pageable);
        } else if (hasStart) {
            todos = todoRepository.findByStartDate(startDate, pageable);
        } else if (hasEnd) {
            todos = todoRepository.findByEndDate(endDate, pageable);
        } else {
            todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);
        }


        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail(),todo.getUser().getNickname()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepositoryQuery.findByIdWithUser(todoId);
        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail(), user.getNickname()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }

    public Page<TodoSearchResponse> searchTodo(int page, int size, String search, String nickname,
                                               LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TodoSearchResponse> todos;
        todos = todoRepositoryQuery.searchTodos(search,nickname,startDate,endDate,pageable);

        return todos;
    }
}
