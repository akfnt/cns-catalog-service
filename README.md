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

# 특정 컨테이너만 실행하기
docker-compose up -d polar-postgres

# 중지
docker-compose down
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

### 쿠버네티스 클러스터에 매니패스트 적용하기 (매니패스트 파일이 있는 위치로 이동)
```
# 이미지가 로컬에 있는 경우 (클라우드 네이티브 빌드팩으로 이미지를 생성한 경우) 이미지를 수동으로 미니큐브 로컬 클러스터로 불러오기
minikube image load cns-catalog-service --profile cns-polar

# services 폴더 내의 매니페스트에 정의된 리소스를 생성함 (폴더 대신 파일을 지정해도 됨)
kubectl apply -f services
kubectl apply -f k8s

# 생성된 pod 정보 보기
kubectl get pod
kubectl get pods -l app=cns-catalog-service
kubectl get pods -l app=cns-catalog-service --watch
kubectl describe pods

# pod 로그 확인하기 ('polar-postgres' 이름의 Deployment 객체에 속한 모든 pod 의 로그를 출력) 
kubectl logs deployment/polar-postgres

# 매니페스트로부터 어떤 객체가 만들어졌는지 확인
kubectl get all -l app=cns-catalog-service

# 서비스 객체만 확인하고 싶을땐
kubectl get svc -l app=cns-catalog-service

# 객체(서비스 객체)를 로컬 컴퓨터에 노출하기 위해 구버네티스가 제공하는 포트 포워딩 기능을 사용
kubectl port-forward service/cns-catalog-service 9001:80
```

### 주어진 객체에 대해 쿠버네티스가 지원하는 API 버전 확인하기
```
kubectl explain <object_name>
ex) object_name -> Deployment
```

### 파드 삭제
```
kubectl delete pod <pod-name>
```

### DB 접속
```
kubectl exec -it <pod-name> -- bash
psql -U user
```

### k8s 폴더안의 매니페스트 모든 객체 삭제
```
kubectl delete -f k8s
```

### Tilt 시작하기 (Tiltfile 이 있는 해당 애플리케이션 루트 폴더로 이동)
```
tilt up

# 추가로 실행해야 할 경우 포트를 지정
tilt up --port=12345

# 틸트 종료
tilt down
```

### Octant 실행하기 (옥탄트 대시보드)
```
쿠버네티스 클러스터가 계속 실행되고 있고, 여기에 애플리케이션 서비스 배포도 되어 있는 상태에서 아래 명령어 실행
octant
```

### 큐비발 실행 (쿠버네티스 매니페스트 검증)
```
kubeval --strict -d k8s
```

### 쿠버네티스 인그레스 (Ingress)
```
# ingress 애드온 활성화 하기 (인그레스 NGINX가 로컬 클러스터에 배포됨)
minikube addons enable ingress --profile cns-polar
kubectl get all -n ingress-nginx

# 미니큐브 클러스터에 할당된 IP 주소 확인하기 (리눅스)
minikube ip --profile cns-polar
# 윈도우의 경우 아래 명령을 사용해 클러스터를 로컬 환경에 노출한 후 127.0.0.1 주소를 통해 클러스터를 호출해야 함
minikube tunnel --profile cns-polar

kubectl apply -f k8s
kubectl get ingress

```

## 그래들 커맨드 예시
### 클라우드 네이티브 빌드팩 사용하여 이미지 만들고 깃허브 저장소에 해당 이미지 푸시하기
```
./gradlew bootBuildImage --imageName ghcr.io/akfnt/cns-catalog-service --publishImage -PregistryUrl="ghcr.io" -PregistryUsername=akfnt -PregistryToken=
```

## 기타 커맨드 예시
### 아파치 벤치 (웹서버 성능 테스트 도구)
```
ab -n 21 -c 1 -m POST http://localhost:9000/orders
```

### 디지털 오션
```
API token 사용해 doctl 에 대한 액세스 권한 얻기 (인증 컨텍스트 지정) - doctl 설치 및 디지털 오션 컨트롤 패널에서 API token 발급 먼저 해야함
doctl auth init --context <NAME>
doctl auth list
doctl auth switch --context <NAME>
doctl account get

쿠버네티스 클러스터 실행
doctl k8s options regions
doctl k8s cluster create polar-cluster --node-pool "name=basicnp;size=s-2vcpu-4gb;count=3;label=type=basic;" --region sgp1
doctl k8s cluster list
kubectl config current-context

PostgreSQL DB 실행
doctl database create polar-db --engine pg --region sgp1 --version 14
doctl database list
doctl database firewalls append <postgres_id> --rule k8s:<cluster_id>
doctl database db create <postgres_id> polardb_catalog
doctl database db create <postgres_id> polardb_order
doctl databases connection <postgres_id> --format Host,Port,User,Password

kubectl create secret generic polar-postgres-catalog-credentials --from-literal=spring.datasource.url=jdbc:postgre
sql://<postgres_host>:<postgres_port>/polardb_catalog --from-literal=spring.datasource.username=<postgres_username> --from-literal=spring.datasource.password=<postgres_password>

kubectl create secret generic polar-postgres-order-credentials --from-literal=spring.flyway.url="jdbc:postgresql:/
/<postgres_host>:<postgres_port>/polardb_order" --from-literal="spring.r2dbc.url=r2dbc:postgresql://polar-db-do-user-16449157-0.c.db.ondigitalocean.com:25060/polardb_order?ssl=true&sslMode=require" --from-literal=spring.datasource.username=<postgres_username> --from-literal=spring.datasource.password=<postgres_password>

Redis 실행
doctl database create polar-redis --engine redis --region sgp1 --version 7
doctl databases firewalls append <redis_id> --rule k8s:<cluster_id>
doctl databases connection <redis_id> --format Host,Port,User,Password

kubectl create secret generic polar-redis-credentials --from-literal=spring.data.redis.host=<redis_host> --from-literal=spring.data.redis.port=<redis_port> --from-literal=spring.data.redis.username=<redis_username> --from-literal=spring.data.redis.password=<redis_password> --from-literal=spring.data.redis.ssl=true

```

### ArgoCD
```

```
