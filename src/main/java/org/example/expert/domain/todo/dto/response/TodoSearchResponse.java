package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class TodoSearchResponse {
    private String title;

    private Long managerCount;

    private Long commentCount;


}
