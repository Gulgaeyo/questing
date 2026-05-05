## Todo API

### Todo register
POST /api/todos

Request
---json
{
  "timeSlot": "MORNING",
  "title": "코딩 문제 풀기",
  "category": "INTELLIGENCE",
  "durationTime": 30
}

GET /api/todos
PATCH /api/todos/{todoId}/complete
DELETE /api/todos/{todoId}

