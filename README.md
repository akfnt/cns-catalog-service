# cns-catalog-service
클라우드 네이티브 스프링 인 액션 - 카탈로그 서비스

## 도커 커맨드 예시
### 실행중인 컨테이너 보기
```
docker ps
```

### 컨테이너 로그 보기
```
docker logs 컨테이너 이름
```

### 컨테이너 접속하기
```
docker exec -it 컨테이너 이름 /bin/bash

# 루트 권한으로 접속 (리눅스) -> 윈도우에서는 wsl2 사용해 리눅스 섈에서 실행해야 함
docker run -it --rm -u root 컨테이너 이름 bash
```

### curl 클라이언트로 특정 컨테이너에 http 요청 보내기
```
docker run --rm --name curl-client --link cns-config-server curlimages/curl curl http://cns-config-server/
docker run --rm --name curl-client --net cns-catalog-network curlimages/curl curl http://cns-config-server/
```

### 도커파일로 컨테이너 이미지 만들기 (Dockerfile 이 있는 폴더로 이동)
```bash
docker build -t my-java-image:1.0.0 .
```

### 이미지 정보 조회
```bash
docker images
docker images my-java-image
```

### 컨테이너 이미지 시작하고 도커파일에 지정된 ENTRYPOINT 실행하기
```bash
docker run --rm my-java-image:1.0.0
```

### 깃허브 컨테이너 저장소로 인증하기
```
docker login ghcr.io
```

### 이미지에 태그 달기 (깃허브 컨테이너 저장소로 이미지 푸시하기 위해)
```
docker tag my-java-image:1.0.0 ghcr.io/akfnt/my-java-image:1.0.0
```

### 깃허브 컨테이너 저장소로 이미지 푸시하기 (깃허브 계정 > profile -> Package 섹션에 이미지 푸시된것 확인가능)
```
docker push ghcr.io/akfnt/my-java-image:1.0.0
```

### 도커 네트워크 만들기
```
docker network create cns-catalog-network
docker network ls

# 삭제
docker network rm cns-catalog-network
```

### PostgreSQL 컨테이너 시작하기 (위에서 만든 도커 네트워크 연결)
```
docker run -d --name polar-postgres --net cns-catalog-network -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=polardb_catalog -p 5432:5432 postgres:14.4
```

### 컨테이너 삭제 하기
```
docker rm -f polar-postgres
```

### Database Commands
Start an interactive PSQL console (postgres 컨테이너(polar-postgres)에 대해 PSQL console 실행하기):

```bash
docker exec -it polar-postgres psql -U user -d polardb_catalog
```

| PSQL Command | Description |
|---|---|
|\list|	List all databases.
|\connect| polardb_catalog	Connect to specific database.
|\dt|	List all tables.
|\d| book	Show the book table schema.
|\d| flyway_schema_history	Show the flyway_schema_history table schema.
|\quit|	Quit interactive psql console.

From within the PSQL console, you can also fetch all the data stored in the book table.
```sql
select * from "book";
```

The following query is to fetch all the data stored in the flyway_schema_history table.
```sql
select * from "flyway_schema_history";
```

### 도커 컴포즈 실행하기 (도커 컴포즈 YAML 파일 있는 위치에서)
```
docker-compose up -d
# 중지
docker-compost down
```


## 쿠버네티스 커맨드 예시
### minikube 사용해 cns-polar 라는 이름의 로컬 쿠버네티스 클러스터 만들기 (도커 엔진 먼저 시작해야 함)
```
# 기본 클러스터가 실행되지 않도록 끄기
minikube stop

# cns-polar 로컬 클러스터 시작하기
minikube start --cpus 2 --memory 4g --driver docker --profile cns-polar

# cns-polar 로컬 클러스터 중지하기
minikube stop --profile cns-polar

# cns-polar 로컬 클러스터 삭제하기
minikube delete --profile cns-polar

# cns-polar 로컬 클러스터 다시 시작하기
minikube start --profile cns-polar

# 현재 클러스터의 모든 노드 목록 얻기
kubectl get nodes

# 상호작용 할 수 있는 모든 콘텍스트 보기
kubectl config get-contexts

# 현재 콘텍스트 확인하기
kubectl config current-context

# cns-polar로 콘텍스트 변경하기
kubectl config use-context cns-polar
```



## 그래들 커맨드 예시
### 클라우드 네이티브 빌드팩 사용하여 이미지 만들고 깃허브 저장소에 해당 이미지 푸시하기
```
./gradlew bootBuildImage --imageName ghcr.io/akfnt/cns-catalog-service --publishImage -PregistryUrl="ghcr.io" -PregistryUsername=akfnt -PregistryToken=
```
