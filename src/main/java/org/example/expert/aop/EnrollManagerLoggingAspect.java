package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.common.dto.CustomUserDetails;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EnrollManagerLoggingAspect {

    private final HttpServletRequest request;
    private final LogService logService;
    private final JwtUtil jwtUtil;

    @Before("execution(* org.example.expert.domain.manager.controller.ManagerController.*(..))")
    public void managerEnroll(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        Long userId = null;
        Long managerId = null;
        Long todoId = null;
        LocalDateTime now = LocalDateTime.now();
        String requestURI = String.valueOf(request.getRequestURI());
        String requestMethod = String.valueOf(request.getMethod());

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof CustomUserDetails userDetails) {
                userId = userDetails.getId();
                log.info("User ID: {}", userDetails.getId());
            } else if (arg instanceof ManagerSaveRequest req) {
                managerId = req.getManagerUserId();
                log.info("ManagerSaveRequest name: {}", req.getManagerUserId());
            } else if (arg instanceof Long todoIdArgs) {
                log.info("Todo ID: {}", todoIdArgs);
                todoId = todoIdArgs;
            }
        }

        if (userId == null) {
            log.warn("UserDetails not found in JoinPoint args");
            return;
        }


        logService.saveLog(userId,managerId,todoId, now, requestURI, requestMethod);
    }
}
