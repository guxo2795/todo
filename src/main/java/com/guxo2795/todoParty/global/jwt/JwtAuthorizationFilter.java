package com.guxo2795.todoParty.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guxo2795.todoParty.global.dto.CommonRes;
import com.guxo2795.todoParty.global.security.UserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
                                             // 하나의 HTTP요청 당 한 번만 실행
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

        if(Objects.nonNull(token)) {
            if(jwtUtil.validateToken(token)) {
                // token에서 사용자 정보 가져오기
                // Claims는 JWT에서 인코딩된 페이로드의 데이터를 나타내는 객체
                Claims info = jwtUtil.getUserInfoFromToken(token);

                // 인증정보에 유저정보(username) 넣기
                // username -> user 조회
                String username = info.getSubject();

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // -> userDetails에 담고
                UserDetails userDetails = userDetailsService.getUserDetails(username);
                // -> authentication의 principal에 담고
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // -> securityContent에 담고
                context.setAuthentication(authentication);
                // -> securityContextHolder에 담고
                SecurityContextHolder.setContext(context);
                // -> @AuthenticationPrincipal로 조회 가능

            } else {
                CommonRes commonRes = new CommonRes("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value());
                // 응답값에 status 세팅
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                // body가 깨지면 안되니까 UTF설정
                response.setContentType("application/json; charset=UTF-8");
                // objectMapper를 매핑해서 넣어야하는 이유는 write가 string을 받기때문
                response.getWriter().write(objectMapper.writeValueAsString(commonRes));
                return;
            }
        }

        // 다음 필터로 요청과 응답 전달.
        filterChain.doFilter(request, response);
    }

}
