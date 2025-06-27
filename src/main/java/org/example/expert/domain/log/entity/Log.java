package org.example.expert.domain.log.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "logs")
public class Log {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long mangerId;

    private Long todoId;

    private LocalDateTime requestTime;

    private String requestURI;

    private String requestMethod;

    public Log(Long userId,Long mangerId,Long todoId, LocalDateTime requestTime, String requestURI,String requestMethod){
        this.userId = userId;
        this.mangerId = mangerId;
        this.todoId = todoId;
        this.requestTime = requestTime;
        this.requestURI = requestURI;
        this.requestMethod = requestMethod;
    }
}
