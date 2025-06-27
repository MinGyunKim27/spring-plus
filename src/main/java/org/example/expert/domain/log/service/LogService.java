package org.example.expert.domain.log.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Long userId,Long mangerId, Long todoId, LocalDateTime requestTime, String requestURL, String requestMethod){

        logRepository.save(
                new Log(userId,mangerId,todoId,requestTime,requestURL,requestMethod
                ));
    }
}
