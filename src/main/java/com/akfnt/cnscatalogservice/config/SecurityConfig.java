package com.akfnt.cnscatalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/", "/books/**").permitAll()     // 인증하지 않고도 인사말과 책의 정보를 제공하도록 허용한다
                        .anyRequest().authenticated())                                              // 그 외 다른 요청은 인증이 필요하다
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))              // JWT 에 기반한 기본 설정을 사용해 OAuth2 리소스 서버 지원을 활성화한다(JWT 인증)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))                    // 각 요청은 액세스 토큰을 가지고 있어야 하는데, 그래야만 사용자는 요청 간 사용자 세션을 계속 유지할 수 있다. 상태를 갖지 않기를 원한다.
                .csrf(AbstractHttpConfigurer::disable)                                              // 인증 전략이 상태를 갖지 않고, 브라우저 기반 클라이언트가 관여되지 않기 때문에 CSRF 보호는 비활성화해도 된다
                .build();
    }
}
