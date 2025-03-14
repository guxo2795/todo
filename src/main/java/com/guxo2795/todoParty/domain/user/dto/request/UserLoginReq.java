package com.guxo2795.todoParty.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserLoginReq(
        String username,
        String password
) {
}
