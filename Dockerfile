# NOTE: 도커 파일 보다는 '클라우드 네이티브 빌드팩'을 이용해서 컨테이너 이미지를 만들자 (이 파일은 도커파일 스터디용)
# 첫 번째 단계를 위한 OpenJDK 베이스 이미지
# JRE가 이미 설치되어 있는 이클립스 테무린 배포판 우분투 베이스 이미지
FROM eclipse-temurin:21 AS builder
# 현재 작업 폴더를 workspace로 변경
WORKDIR workspace

# 프로젝트에서 애플리케이션 JAR 파일의 위치를 지정하는 빌드 인수
ARG JAR_FILE=build/libs/*.jar
# 애플리케이션 JAR 파일을 로컬 머신에서 이미지의 'workspace' 폴더로 복사
COPY ${JAR_FILE} cns-catalog-service.jar
# 계층 JAR 모드를 적용해 아카이브에서 계층을 추출한다
RUN java -Djarmode=layertools -jar cns-catalog-service.jar extract


# 두 번째 단계를 위한 OpenJDK 베이스 이미지
FROM eclipse-temurin:21
# 'spring' 이라는 이름의 유저를 만든다 (보안 - 최소 권한 액세스)
RUN useradd spring
# 'spring' 을 현재 유저로 설정한다 (보안 - 최소 권한 액세스)
USER spring
# 현재 작업 폴더를 workspace로 변경
WORKDIR workspace

# 첫번째 단계에서 추출한 JAR 계층을 두 번째 단계로 복사한다
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./

# 스프링 부트 런처를 사용해 우버 JAR 대신 계층으로 애플리케이션을 시작한다
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]