# 빌드
custom_build(
    # 컨테이너 이미지의 이름
    ref = 'cns-catalog-service',
    # 컨테이너 이미지를 빌드하기 위한 명령
    # 윈도우 에서는 $EXPECTED_REF 를 %EXPECTED_REF% 로 변경할 것 -> 근데 안됨..
    command = "./gradlew bootBuildImage --imageName %EXPECTED_REF%",
    # 새로운 빌드를 시작하기 위해 지켜봐야 하는 파일
    deps = ['build.gradle', 'src']
)

# 배포
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# 관리
k8s_resource('cns-catalog-service', port_forwards=['9001'])