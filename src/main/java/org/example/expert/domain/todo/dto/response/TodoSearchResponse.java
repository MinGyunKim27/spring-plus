package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoSearchResponse {
    private Long id;

    private String title;

    private Long managerCount;

    private Long commentCount;


    @QueryProjection
    public TodoSearchResponse(Long id, String title, Long managerCount, Long commentCount){
        this.id = id;
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }
}
