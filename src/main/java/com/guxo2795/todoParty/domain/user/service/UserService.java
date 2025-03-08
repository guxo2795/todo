package com.guxo2795.todoParty.domain.user.service;

import com.guxo2795.todoParty.domain.user.dto.request.UserSignupReq;
import com.guxo2795.todoParty.domain.user.dto.response.UserSignupRes;
import com.guxo2795.todoParty.domain.user.entity.User;
import com.guxo2795.todoParty.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserSignupRes signup(UserSignupReq req) {
        String username = req.username();
        String password = passwordEncoder.encode(req.password());

        if(userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저 입니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);

        return new UserSignupRes("회원가입 완료", HttpStatus.CREATED.value());
    }
}
