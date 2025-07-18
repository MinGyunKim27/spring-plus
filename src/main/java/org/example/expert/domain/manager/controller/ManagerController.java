package org.example.expert.domain.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.CustomUserDetails;
import org.example.expert.domain.log.service.LogService;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final LogService logService;

    @PostMapping("/todos/{todoId}/managers")
    public ResponseEntity<ManagerSaveResponse> saveManager(
            HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long todoId,
            @Valid @RequestBody ManagerSaveRequest managerSaveRequest
    ) {
        LocalDateTime now = LocalDateTime.now();
        logService.saveLog(userDetails.getId(),managerSaveRequest.getManagerUserId(),todoId,now,request.getRequestURI(),request.getMethod());
        return ResponseEntity.ok(managerService.saveManager(userDetails, todoId, managerSaveRequest));
    }

    @GetMapping("/todos/{todoId}/managers")
    public ResponseEntity<List<ManagerResponse>> getMembers(@PathVariable long todoId) {
        return ResponseEntity.ok(managerService.getManagers(todoId));
    }

    @DeleteMapping("/todos/{todoId}/managers/{managerId}")
    public void deleteManager(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long todoId,
            @PathVariable long managerId
    ) {
        managerService.deleteManager(userDetails, todoId, managerId);
    }
}
