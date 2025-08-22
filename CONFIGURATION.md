# Configuration Guide

## 설정 파일 관리

### 🚫 Git에서 제외된 파일들
- `src/main/resources/application.yml` - 애플리케이션 설정
- `src/main/resources/application.properties` - 애플리케이션 설정

### 📋 로컬 개발 설정 방법

1. **템플릿 복사**
   ```bash
   cp src/main/resources/application.yml.template src/main/resources/application.yml
   ```

2. **필요한 값 수정**
   - `google.maps.api.key`: 실제 Google Maps API 키
   - `app.s3.bucket.name`: 실제 S3 버킷 이름
   - 기타 환경에 맞는 설정값들

3. **환경별 설정**
   - **로컬 개발**: `spring.profiles.active: dev`
   - **운영 환경**: `spring.profiles.active: prod`

### 🔧 주요 설정 항목

#### 데이터베이스
```yaml
spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/storeservice  # 로컬 H2
    # url: jdbc:oracle:thin:@localhost:1521:XE  # Oracle 사용 시
```

#### 보안
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://cognito-idp.ap-northeast-2.amazonaws.com/ap-northeast_2_bdkXgjghs
```

#### 포트
```yaml
server:
  port: 8081  # 로컬 개발용
  # port: 8080  # 운영 환경용
```

### ⚠️ 주의사항

- **민감한 정보** (API 키, 비밀번호 등)는 Git에 커밋하지 마세요
- **환경별 설정**은 `application-{profile}.yml`로 분리하는 것을 권장합니다
- **로컬 설정**은 `application.yml`에, **공통 설정**은 `application-common.yml`에 작성하세요

### 🚀 환경 변수 사용

운영 환경에서는 환경 변수를 사용하여 설정을 주입할 수 있습니다:

```bash
export GOOGLE_MAPS_API_KEY="your-actual-api-key"
export SPRING_PROFILES_ACTIVE="prod"
export SERVER_PORT="8080"
```
