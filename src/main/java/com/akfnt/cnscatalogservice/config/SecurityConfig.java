package com.akfnt.cnscatalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/", "/books/**").permitAll()     // 인증하지 않고도 인사말과 책의 정보를 제공하도록 허용한다
                        .anyRequest().hasRole("employee"))                                          // 그 외 다른 요청은 인증이 필요할 뿐만 아니라 employee 역할도 가지고 있어야 한다 (ROLE_employee 권한과 동일 -> 내부적으로 'ROLE_' 접두사 붙여서 처리됨)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))              // JWT 에 기반한 기본 설정을 사용해 OAuth2 리소스 서버 지원을 활성화한다(JWT 인증)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))                    // 각 요청은 액세스 토큰을 가지고 있어야 하는데, 그래야만 사용자는 요청 간 사용자 세션을 계속 유지할 수 있다. 상태를 갖지 않기를 원한다.
                .csrf(AbstractHttpConfigurer::disable)                                              // 인증 전략이 상태를 갖지 않고, 브라우저 기반 클라이언트가 관여되지 않기 때문에 CSRF 보호는 비활성화해도 된다
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();                  // 클레임을 GrantedAuthority 객체로 매핑하기 위한 변환기
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");                                 // 각각의 사용자 역할에 'ROLE_' 접두어를 붙인다 (권한의 그룹화)
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");                            // roles 클레임에서 역할을 추출한다
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();                          // jwt 를 변환할 방법을 정의한다. 부여된 권한을 만드는 방법만 사용자 지정한다
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
