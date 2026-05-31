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
- JWT 인증 실패 응답 body 정리
- TODO/HABIT/UserStat 도메인 예외 응답 정리
- `@Valid` 기반 요청값 검증
- 회원가입/로그인/TODO/HABIT 요청값 검증 컨트롤러 테스트 코드
- JWT 인증 실패 컨트롤러 테스트 코드
- TODO/HABIT/TODAY 사용자별 데이터 분리 컨트롤러 테스트 코드
- TODO CRUD
- TODO 완료 처리
- HABIT CRUD
- HABIT 완료 처리
- HABIT 완료 기록을 `habit_logs`에 저장
- TODAY 전체 조회
- User Stat 조회 API
- `durationTime` 기반 스탯 성장 처리

진행 예정:

- Refresh token 흐름
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
- `/api/stats/**`

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

## 에러 응답 정책

Questing은 주요 API 오류를 HTTP 상태 코드와 JSON body로 응답합니다.

공통 응답 형태:

```json
{
  "timestamp": "2026-05-24T19:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "존재하지 않는 TODO입니다."
}
```

현재 정리된 예외 응답:

| 상황 | HTTP Status |
|---|---|
| 로그인 실패 | `401 Unauthorized` |
| JWT 토큰 없음 | `401 Unauthorized` |
| JWT 토큰 형식 오류 또는 검증 실패 | `401 Unauthorized` |
| 중복 회원가입 | `409 Conflict` |
| 존재하지 않는 TODO/HABIT/UserStat | `404 Not Found` |
| 지원하지 않는 category 등 잘못된 요청 | `400 Bad Request` |
| 요청값 검증 실패 | `400 Bad Request` |

---

## 요청값 검증 정책

회원가입, 로그인, TODO, HABIT 요청은 `@Valid` 기반으로 검증합니다.

검증 실패 시 `400 Bad Request`와 함께 필드별 오류 메시지를 응답합니다.

응답 예시:

```json
{
  "timestamp": "2026-05-25T19:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "요청값 검증에 실패했습니다.",
  "fieldErrors": {
    "title": "TODO 제목은 필수입니다.",
    "durationTime": "진행 시간은 1분 이상이어야 합니다."
  }
}
```

### TODO 검증 규칙

| 필드 | 규칙 |
|---|---|
| `timeSlot` | 필수 |
| `title` | 필수 |
| `category` | 필수 |
| `durationTime` | 필수, 1분 이상 |

### HABIT 검증 규칙

| 필드 | 규칙 |
|---|---|
| `title` | 필수 |
| `content` | 필수 |
| `category` | 필수 |
| `durationTime` | 필수, 1분 이상 |
| `strtDate` | 필수 |
| `endDate` | 선택값, 입력 시 오늘 또는 미래 날짜 |

`endDate`가 없는 장기 HABIT은 프론트에서 `9999-12-31`을 보내는 방식으로 처리합니다.

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

Todo 수정은 전체 수정 방식입니다. 프론트는 수정하지 않은 기존 값도 포함해 전체 필드를 다시 전송합니다.

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

기한 미정 HABIT은 프론트에서 `endDate`를 `9999-12-31`로 전송합니다.

#### 오늘 Habit 조회

`GET /api/habits`

로그인한 사용자의 오늘 유효한 HABIT 목록을 조회합니다.

#### Habit 수정

`PUT /api/habits/{habitId}`

Habit 수정은 전체 수정 방식입니다. 프론트는 수정하지 않은 기존 값도 포함해 전체 필드를 다시 전송합니다.

Request:

```json
{
  "title": "명상",
  "content": "30분 명상하기",
  "category": "MENTAL",
  "durationTime": 30,
  "strtDate": "2026-05-25",
  "endDate": "9999-12-31"
}
```

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

### Stat API

Stat API는 access token이 필요합니다.

```http
Authorization: Bearer {accessToken}
```

#### 사용자 스탯 조회

`GET /api/stats`

로그인한 사용자의 현재 성장 수치와 다음 성장까지 누적된 시간을 조회합니다.

응답 예시:

```json
{
  "userId": 2,
  "strengthStat": 1,
  "strengthMinutes": 20,
  "mentalStat": 1,
  "mentalMinutes": 0,
  "intelligenceStat": 7,
  "intelligenceMinutes": 0,
  "createdAt": "2026-05-23T16:20:35.846168",
  "updatedAt": "2026-05-24T17:59:08.756554"
}
```

`strengthMinutes`, `mentalMinutes`, `intelligenceMinutes`는 다음 스탯 증가까지 누적된 잔여 시간입니다. 예를 들어 `strengthMinutes`가 `20`이면, 10분을 더 완료했을 때 `strengthStat`이 1 증가합니다.

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

1. TODO/HABIT 완료 처리 테스트 코드 작성
2. Refresh token 흐름 설계
3. README API 문서와 실제 응답 구조 지속 동기화
