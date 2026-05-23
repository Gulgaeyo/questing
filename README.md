# Questing

Questing은 TODO와 HABIT을 현실의 퀘스트처럼 다루고, 완료한 행동 시간을 캐릭터 성장 수치로 바꾸는 생산성 API 프로젝트입니다.

단순히 할 일을 체크하는 앱이 아니라, 사용자가 매일 쌓은 행동을 `STRENGTH`, `MENTAL`, `INTELLIGENCE` 같은 성장 데이터로 기록하는 것을 목표로 합니다.

> 현실을 게임처럼, 성장을 일상처럼.

---

## 프로젝트 개요

Questing은 크게 네 가지 흐름을 관리합니다.

- `TODO`: 오늘 처리할 단발성 작업
- `HABIT`: 기간을 가진 반복 루틴
- `TODAY`: 오늘의 TODO와 오늘 유효한 HABIT을 한 번에 보는 화면
- `STAT PROGRESSION`: 완료한 행동 시간 기반의 사용자 성장 수치

TODO나 HABIT을 완료하면 `durationTime`이 경험치처럼 사용됩니다. 누적 30분마다 해당 카테고리의 스탯이 1 증가하고, 남은 시간은 다음 성장까지 이월됩니다.

---

## 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Database | PostgreSQL |
| Persistence | MyBatis |
| Authentication | BCrypt, JWT access token |
| Build | Gradle |
| IDE | IntelliJ IDEA |

---

## 현재 구현 상태

구현 완료:

- 회원가입
- `loginId`, `nickName`, `email` 중복 검사
- BCrypt 기반 비밀번호 암호화 저장
- 로그인
- JWT access token 발급
- JWT 인터셉터를 통한 보호 API 인증
- 토큰에서 추출한 `userId` 기반 TODO/HABIT/TODAY 데이터 분리
- 로그인 실패와 중복 가입에 대한 전역 예외 처리
- TODO CRUD
- TODO 완료 처리
- HABIT CRUD
- HABIT 완료 처리
- HABIT 완료 기록을 `habit_logs`에 저장
- TODAY 전체 조회
- `durationTime` 기반 스탯 성장 처리

진행 예정:

- Refresh token 흐름
- JWT 인증 실패 응답 body 개선
- `@Valid` 기반 요청값 검증
- TODO/HABIT 도메인 예외 응답 정리
- User Stat 조회 API
- 인증, TODO, HABIT, 스탯 성장 테스트 코드 작성

---

## 인증 흐름

### 회원가입

`POST /api/users/signup`

새 사용자를 생성하고, 해당 사용자의 기본 스탯 정보를 초기화합니다.

비밀번호는 BCrypt로 암호화해서 저장합니다.

### 로그인

`POST /api/users/login`

`loginId`와 `password`를 검증한 뒤, 사용자 정보와 JWT access token을 응답합니다.

응답 예시:

```json
{
  "id": 2,
  "loginId": "sikhye",
  "nickName": "Sikhye",
  "email": "sikhye@example.com",
  "accessToken": "eyJ..."
}
```

### 보호 API 요청

보호된 API는 `Authorization` 헤더에 access token을 담아 요청해야 합니다.

```http
Authorization: Bearer {accessToken}
```

JWT 인터셉터는 토큰을 검증하고, 토큰의 subject에서 `userId`를 추출해 request attribute에 저장합니다. 컨트롤러는 이 `userId`를 받아 로그인한 사용자 기준으로 TODO/HABIT/TODAY 데이터를 처리합니다.

토큰이 필요한 경로:

- `/api/todos/**`
- `/api/habits/**`
- `/api/today/**`

토큰 없이 접근 가능한 경로:

- `/api/users/signup`
- `/api/users/login`

---

## 도메인 규칙

### TODO

TODO는 오늘 처리할 단발성 퀘스트입니다.

예시:

- 코딩 문제 풀기
- 방 정리하기
- 책 읽기
- 운동 가기

주요 필드:

| 필드 | 설명 |
|---|---|
| `timeSlot` | `MORNING`, `AFTERNOON`, `EVENING`, `DAWN` 같은 시간대 |
| `title` | 작업 제목 |
| `category` | 성장 카테고리 |
| `durationTime` | 행동 시간 또는 예상 시간 |

### HABIT

HABIT은 반복적으로 수행하는 루틴 퀘스트입니다.

예시:

- 물 마시기
- 스트레칭
- 명상
- 운동
- 독서

HABIT은 시작일과 종료일을 가지며, 오늘 날짜가 해당 기간에 포함될 때 오늘의 HABIT으로 조회됩니다.

### HABIT LOG

HABIT의 완료 여부는 `habits` 테이블에 직접 저장하지 않고, `habit_logs` 테이블에 별도로 저장합니다.

```text
habits
= 반복 루틴의 원본 정보

habit_logs
= 특정 날짜에 HABIT을 완료한 기록
```

이 구조를 통해 하나의 HABIT을 날짜별로 완료 기록을 남기면서 관리할 수 있습니다.

### STAT PROGRESSION

사용자 성장은 완료한 행동 시간인 `durationTime`을 기반으로 계산합니다.

```text
durationTime = 행동 시간
누적 30분 = 스탯 +1
남은 시간 = 다음 성장까지 이월
```

예시:

```text
현재 INTELLIGENCE 누적 시간: 20분
코딩 TODO 완료: 40분

20 + 40 = 60분
INTELLIGENCE +2
남은 누적 시간 = 0분
```

카테고리:

| Category | Stat |
|---|---|
| `STRENGTH` | 신체 성장 |
| `MENTAL` | 정신 성장 |
| `INTELLIGENCE` | 지적 성장 |

---

## API 문서

### User API

#### 회원가입

`POST /api/users/signup`

Request:

```json
{
  "loginId": "sikhye",
  "password": "password1234",
  "userName": "Sikhye",
  "nickName": "Sikhye",
  "email": "sikhye@example.com",
  "birth": "2000-01-01"
}
```

중복 회원가입은 `409 Conflict`를 응답합니다.

#### 로그인

`POST /api/users/login`

Request:

```json
{
  "loginId": "sikhye",
  "password": "password1234"
}
```

로그인 실패는 `401 Unauthorized`를 응답합니다.

---

### Todo API

모든 Todo API는 access token이 필요합니다.

```http
Authorization: Bearer {accessToken}
```

#### Todo 생성

`POST /api/todos`

Request:

```json
{
  "timeSlot": "MORNING",
  "title": "코딩 문제 풀기",
  "category": "INTELLIGENCE",
  "durationTime": 30
}
```

#### 오늘 Todo 조회

`GET /api/todos`

로그인한 사용자의 오늘 TODO 목록을 조회합니다.

#### Todo 수정

`PUT /api/todos/{todoId}`

Request:

```json
{
  "timeSlot": "EVENING",
  "title": "코딩 문제 2개 풀기",
  "category": "INTELLIGENCE",
  "durationTime": 60
}
```

#### Todo 완료

`PATCH /api/todos/{todoId}/complete`

TODO를 완료 상태로 변경하고, `durationTime`을 기반으로 스탯 성장을 적용합니다.

#### Todo 삭제

`DELETE /api/todos/{todoId}`

TODO를 soft delete 처리합니다.

---

### Habit API

모든 Habit API는 access token이 필요합니다.

```http
Authorization: Bearer {accessToken}
```

#### Habit 생성

`POST /api/habits`

Request:

```json
{
  "title": "명상",
  "content": "20분 명상하기",
  "category": "MENTAL",
  "durationTime": 20,
  "strtDate": "2026-05-23",
  "endDate": "2026-05-30"
}
```

#### 오늘 Habit 조회

`GET /api/habits`

로그인한 사용자의 오늘 유효한 HABIT 목록을 조회합니다.

#### Habit 수정

`PUT /api/habits/{habitId}`

#### Habit 완료

`PATCH /api/habits/{habitId}/complete`

완료 흐름:

1. `habitId`와 로그인한 `userId`로 HABIT을 조회합니다.
2. 오늘 이미 완료 기록이 있는지 확인합니다.
3. 오늘 완료 기록이 없으면 `habit_logs`에 저장합니다.
4. `durationTime`을 기반으로 스탯 성장을 적용합니다.
5. 완료 결과를 응답합니다.

#### Habit 삭제

`DELETE /api/habits/{habitId}`

HABIT을 soft delete 처리합니다.

---

### Today API

Today API는 access token이 필요합니다.

```http
Authorization: Bearer {accessToken}
```

#### 오늘 전체 조회

`GET /api/today`

로그인한 사용자의 오늘 TODO와 오늘 유효한 HABIT을 함께 조회합니다.

응답 예시:

```json
{
  "today": "2026-05-23",
  "todos": [
    {
      "id": 1,
      "userId": 2,
      "title": "코딩 문제 풀기",
      "category": "INTELLIGENCE",
      "timeSlot": "MORNING",
      "durationTime": 30,
      "isCompleted": false
    }
  ],
  "habits": [
    {
      "id": 1,
      "userId": 2,
      "title": "명상",
      "content": "20분 명상하기",
      "category": "MENTAL",
      "durationTime": 20,
      "completedToday": true
    }
  ]
}
```

---

## 주요 테이블

| 테이블 | 역할 |
|---|---|
| `users` | 사용자 계정 정보 |
| `user_stats` | 사용자 성장 수치와 누적 시간 |
| `todos` | 단발성 TODO 퀘스트 |
| `habits` | 반복 HABIT 원본 정보 |
| `habit_logs` | HABIT 날짜별 완료 기록 |

---

## 다음 마일스톤

추천 다음 작업:

1. User Stat 조회 API 추가
2. JWT 인증 실패 응답 body 개선
3. 회원가입, 로그인, TODO, HABIT 요청값 검증 추가
4. 인증과 사용자별 데이터 분리 테스트 코드 작성
5. Refresh token 흐름 설계
