package com.akfnt.cnscatalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

// JDBC 관련 설정을 위한 클래스
@Configuration
@EnableJdbcAuditing     // 지속성 엔티티에 대한 감사(Auditing)를 활성화. 데이터의 생성, 변경, 삭제 시 해당 시점에 감사 메타 데이터(생성 / 수정 시간 등)를 캡처해 DB에 저장
public class DataConfig {
    //감사 목적으로 현재 인증된 사용자를 반환
    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional
                .ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)        // 인증되지 않은 사용자가 데이터를 변경하려는 경우를 처리하기 위한 부분
                .map(Authentication::getName);                  // Authentication 객체로부터 현재 인증된 사용자의 유저명을 추출한다
    }
}
