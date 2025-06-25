package org.example.expert.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Setter
@AllArgsConstructor
public class CustomUserDetails {
    Long id;
    String email;
    String userRole;
    String nickname;
}
