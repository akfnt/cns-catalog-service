package com.akfnt.cnscatalogservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

// JDBC 관련 설정을 위한 클래스
@Configuration
@EnableJdbcAuditing     // 지속성 엔티티에 대한 감사(Auditing)를 활성화. 데이터의 생성, 변경, 삭제 시 해당 시점에 감사 메타 데이터(생성 / 수정 시간 등)를 캡처해 DB에 저장
public class DataConfig {
}
