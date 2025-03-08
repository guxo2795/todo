package com.guxo2795.todoParty.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserSignupReq(
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    String username,
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    String password
) {

}

