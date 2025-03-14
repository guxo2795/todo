package com.guxo2795.todoParty.domain.user.controller;

import com.guxo2795.todoParty.domain.user.dto.request.UserLoginReq;
import com.guxo2795.todoParty.domain.user.dto.request.UserSignupReq;
import com.guxo2795.todoParty.domain.user.dto.response.UserLoginRes;
import com.guxo2795.todoParty.domain.user.dto.response.UserSignupRes;
import com.guxo2795.todoParty.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupRes> signup(@Valid @RequestBody UserSignupReq req) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.signup(req));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(new UserSignupRes(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRes> login(@RequestBody UserLoginReq req, HttpServletResponse res) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(req, res));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(new UserLoginRes(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }
}
