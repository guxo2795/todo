package com.guxo2795.todoParty.domain.user.service;

import com.guxo2795.todoParty.domain.user.dto.request.UserLoginReq;
import com.guxo2795.todoParty.domain.user.dto.request.UserSignupReq;
import com.guxo2795.todoParty.domain.user.dto.response.UserLoginRes;
import com.guxo2795.todoParty.domain.user.dto.response.UserSignupRes;
import com.guxo2795.todoParty.domain.user.entity.User;
import com.guxo2795.todoParty.domain.user.repository.UserRepository;
import com.guxo2795.todoParty.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

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

    public UserLoginRes login(UserLoginReq req, HttpServletResponse res) {
        String username = req.username();
        String password = req.password();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(req.username()));

        return new UserLoginRes("로그인 완료", HttpStatus.CREATED.value());
    }
}
