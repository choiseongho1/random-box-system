# 랜덤박스 티켓팅 시스템

티켓팅 시스템과 랜덤박스를 결합한 새로운 형태의 티켓팅 시스템입니다.

## 기술 스택

- Backend: Spring Boot 3.x
- Database: MySQL 8.0
- Build Tool: Gradle
- Java Version: 17

## 주요 기능

- 회원 관리 (가입/수정/조회)
- 티켓팅 시스템
- 랜덤박스 시스템
- 쿠폰 시스템

## 프로젝트 구조

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── randombox
│   │           ├── domain
│   │           │   ├── user
│   │           │   ├── ticket
│   │           │   ├── randombox
│   │           │   └── coupon
│   │           ├── global
│   │           │   ├── config
│   │           │   ├── error
│   │           │   └── util
│   │           └── api
│   │               └── v1
│   └── resources
│       └── application.yml
└── test
    └── java
        └── com
            └── randombox
```
