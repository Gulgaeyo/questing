
## Project Overview 
Questing는 사용자의 일정과 습관을 현실 성장 퀘스트로 변환하고  
행동을 경험치로 기록하여 캐릭터와 사용자 모두의 성장을 시각화하는 앱이다.

### Skills
1. Java
2. Spring Boot
3. postgreSQL
4. AWS


## Todo API

### Todo register
POST /api/todos

Request  
---json  
{  
  "timeSlot": "MORNING",  
  "title": "Solve Coding Problem",  
  "category": "INTELLIGENCE",  
  "durationTime": 30  
}  

GET /api/todos  
PATCH /api/todos/{todoId}/complete  
DELETE /api/todos/{todoId}  

## Habit API  
### Habit register  
POST /api/habits  
Request  
---json     
{  
"title": "명상",  
"content": "20분",  
"category": "INTELLIGENCE",  
"strtDate": "2026-05-07",  
"endDate": "2026-05-20"  
}  
GET /api/habits  
PUT /api/habits/{habitId}   
DELETE /api/habits/{habitId}    
PATCH /api/habits/{habitId}/complete  