# Questing

Questing은 사용자의 TODO와 HABIT을 현실 성장 퀘스트로 변환하고,  
완료한 행동을 `durationTime` 기반 경험치로 기록하여 사용자의 성장을 시각화하는 앱입니다.

> 현실을 게임처럼, 성장을 일상처럼.

---

## 📌 Project Overview

Questing은 단순한 할 일 관리 앱이 아니라,  
사용자가 매일 수행하는 작은 행동을 성장 데이터로 바꾸는 것을 목표로 합니다.

- TODO: 오늘 처리할 작은 일
- HABIT: 반복적으로 수행하는 루틴
- Habit Log: 습관 완료 기록
- User Stat: 행동 시간 누적 기반 성장 수치

---

## 🛠️ Tech Stack

| 구분 | 기술 |
|---|---|
| Language | Java |
| Framework | Spring Boot |
| Database | PostgreSQL |
| Persistence | MyBatis |
| IDE | IntelliJ IDEA |
| Deploy | AWS 예정 |

---

## 🧩 Core Features

### TODO

- 오늘 할 일 등록
- 오늘 할 일 조회
- 할 일 수정
- 할 일 삭제
- 할 일 완료 처리

### HABIT

- 습관 등록
- 오늘 유효한 습관 조회
- 습관 수정
- 습관 삭제
- 습관 완료 처리
- 완료 시 `habit_logs`에 기록 저장

### TODAY

- 오늘의 TODO와 HABIT을 한 번에 조회
- HABIT의 오늘 완료 여부 확인

### STAT PROGRESSION

- `durationTime`을 기반으로 성장 시간 누적
- 30분 누적 시 스탯 1 상승
- 남은 시간은 다음 성장까지 이월

---

## 🧠 Domain Rule

### TODO

TODO는 당일에 처리할 작은 작업입니다.

예시:

- 장보기
- 다이소 가기
- 코딩 문제 풀기
- 방 정리하기

TODO는 빠르게 등록하는 것이 중요하므로 최소한의 정보만 입력합니다.

| 필드 | 설명 |
|---|---|
| `timeSlot` | 아침, 점심, 저녁, 새벽 |
| `title` | 할 일 |
| `category` | 성장 카테고리 |
| `durationTime` | 예상 또는 진행 시간 |

---

### HABIT

HABIT은 반복적으로 수행하는 루틴입니다.

예시:

- 물 마시기
- 스트레칭
- 명상
- 운동
- 독서

HABIT은 장기적으로 관리되는 데이터이므로 TODO보다 상세한 정보를 가집니다.

---

### HABIT LOG

습관 완료 기록은 `habits` 테이블에 직접 저장하지 않고,  
`habit_logs` 테이블에 별도로 저장합니다.

```text
habits
= 습관 원본 / 설정 정보

habit_logs
= 특정 날짜에 완료한 기록

📈 Stat Progression Rule

Questing의 성장 방식은 durationTime 기반입니다.

durationTime = 행동에 투자한 시간
30분 누적 = 스탯 1 상승
남은 시간 = 다음 성장까지 이월

예시:

현재 지능 누적 시간: 20분
코딩 TODO 완료: 40분

20 + 40 = 60분
지능 스탯 +2
남은 누적 시간 0분

카테고리별 스탯:

Category	Stat
STRENGTH	힘
MENTAL	정신
INTELLIGENCE	지능
📡 API Documentation
Todo API
Todo 등록
POST /api/todos

Request

{
  "timeSlot": "MORNING",
  "title": "코딩 문제 풀기",
  "category": "INTELLIGENCE",
  "durationTime": 30
}
오늘 Todo 조회
GET /api/todos
Todo 수정
PUT /api/todos/{todoId}

Request

{
  "timeSlot": "EVENING",
  "title": "코딩 문제 2개 풀기",
  "category": "INTELLIGENCE",
  "durationTime": 60
}
Todo 완료
PATCH /api/todos/{todoId}/complete

완료 시 해당 TODO는 완료 상태로 변경되고,
durationTime 기반으로 사용자 스탯이 누적됩니다.

Todo 삭제
DELETE /api/todos/{todoId}
Habit API
Habit 등록
POST /api/habits

Request

{
  "title": "명상",
  "content": "20분 명상하기",
  "category": "MENTAL",
  "durationTime": 20,
  "strtDate": "2026-05-07",
  "endDate": "2026-05-20"
}
오늘 Habit 조회
GET /api/habits

오늘 날짜 기준으로 유효한 습관 목록을 조회합니다.

Habit 수정
PUT /api/habits/{habitId}

Request

{
  "title": "아침 명상",
  "content": "아침에 20분 명상하기",
  "category": "MENTAL",
  "durationTime": 20,
  "strtDate": "2026-05-07",
  "endDate": "2026-05-20"
}
Habit 완료
PATCH /api/habits/{habitId}/complete

완료 시 동작:

1. habitId로 습관 조회
2. 오늘 이미 완료 기록이 있는지 확인
3. 완료 기록이 없으면 habit_logs에 저장
4. durationTime 기반 earnedExp 저장
5. user_stats에 성장 시간 누적

Response 예시

{
  "habitId": 1,
  "isCompleted": true,
  "completedDate": "2026-05-10",
  "completedAt": "2026-05-10T10:30:00",
  "category": "MENTAL",
  "durationTime": 20,
  "earnedExp": 20
}
Habit 삭제
DELETE /api/habits/{habitId}
Today API
오늘 전체 조회
GET /api/today

오늘의 TODO와 HABIT을 한 번에 조회합니다.

Response 예시

{
  "today": "2026-05-10",
  "todos": [
    {
      "id": 1,
      "title": "코딩 문제 풀기",
      "category": "INTELLIGENCE",
      "timeSlot": "MORNING",
      "durationTime": 30,
      "completed": false
    }
  ],
  "habits": [
    {
      "id": 1,
      "title": "명상",
      "content": "20분 명상하기",
      "category": "MENTAL",
      "durationTime": 20,
      "completedToday": true
    }
  ]
}
🗄️ Main Tables
todos

TODO 정보를 저장합니다.

habits

습관 원본 정보를 저장합니다.

habit_logs

습관 완료 기록을 저장합니다.

user_stats

사용자별 성장 수치와 누적 시간을 저장합니다.

strength_stat
strength_minutes
mental_stat
mental_minutes
intelligence_stat
intelligence_minutes
🧪 Test Progress

현재 구현 및 테스트 완료:

Todo CRUD API
Todo complete API
Habit CRUD API
Habit complete API
Habit log 저장
Today overview API
durationTime 기반 stat progression system