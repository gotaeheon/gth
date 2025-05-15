
# 포트폴리오 여행 추천 서비스

## 📌 프로젝트 개요
사용자의 지역 및 예산 정보를 기반으로 맞춤형 여행 계획을 추천해주는 웹 서비스입니다.  
회원 가입, 여행 계획 찜하기, 리뷰 등록 기능 등을 포함하고 있으며 Spring Boot 기반으로 개발되었습니다.

## 🛠️ 기술 스택
- **Back-end**: Spring Boot, Spring Security
- **Front-end**: JSP / Thymeleaf
- **Database**: MySQL (JPA 활용)
- **Build Tool**: Gradle

## 🔑 주요 기능
- 출발 지역과 예산을 기반으로 한 여행 추천
- 사용자 리뷰 및 이미지 업로드 기능
- 찜하기(저장) 기능을 통한 계획 보관
- 관리자 페이지 및 권한 설정

## 📁 프로젝트 구조
```
├── controller/       # 웹 요청 처리
├── service/          # 비즈니스 로직 처리
├── dto/              # 데이터 전달 객체
├── entity/           # JPA 엔티티 정의
├── repository/       # DB 접근 계층
└── config/           # 보안 및 설정 관련 구성
```

## 💡 향후 개선 방향
- AI 추천 알고리즘 도입
- 지도 API 연동을 통한 여행 시각화
- SNS 공유 기능 추가

## 🚀 실행 방법
1. 프로젝트 클론: `git clone <repo-url>`
2. DB 설정 후 `application.properties` 수정
3. Gradle로 빌드 및 실행
4. `localhost:8080/home` 접속

## 🙌 기여
Pull Request는 언제든 환영입니다. 개선 아이디어나 버그 제보도 자유롭게 올려주세요!

---

감사합니다! 😊
