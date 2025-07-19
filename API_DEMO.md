# 员工管理系统 API 接口演示文档

## 系统说明
这是一个基于Spring Boot + MyBatis + MySQL的员工管理系统，主要功能包括：
1. **机构管理**：多级机构树形结构
2. **员工管理**：完整的CRUD操作
3. **并发短信发送**：异步批量发送短信给员工

## 技术亮点
- **并发处理**：使用@Async + CompletableFuture实现并发短信发送
- **线程池管理**：自定义ThreadPoolTaskExecutor配置
- **数据库设计**：支持多级机构结构和员工管理
- **RESTful API**：标准的REST接口设计

---

## 一、机构管理 API

### 1.1 获取机构树
```
GET /api/organizations/tree
```
**说明**：获取完整的机构树形结构

**示例响应**：
```json
[
  {
    "id": 1,
    "name": "总公司",
    "parentId": null,
    "level": 1,
    "children": [
      {
        "id": 2,
        "name": "技术部",
        "parentId": 1,
        "level": 2,
        "children": []
      }
    ]
  }
]
```

### 1.2 获取单个机构
```
GET /api/organizations/{id}
```
**示例**：`GET /api/organizations/1`

### 1.3 获取子机构
```
GET /api/organizations/{parentId}/children
```
**示例**：`GET /api/organizations/1/children`

### 1.4 创建机构
```
POST /api/organizations
Content-Type: application/json

{
  "name": "新部门",
  "parentId": 1,
  "level": 2,
  "status": 1
}
```

### 1.5 更新机构
```
PUT /api/organizations/{id}
Content-Type: application/json

{
  "name": "更新后的部门名称",
  "parentId": 1,
  "level": 2,
  "status": 1
}
```

### 1.6 删除机构
```
DELETE /api/organizations/{id}
```

---

## 二、员工管理 API

### 2.1 获取所有员工
```
GET /api/employees
```

### 2.2 获取单个员工
```
GET /api/employees/{id}
```
**示例**：`GET /api/employees/1`

### 2.3 按机构获取员工
```
GET /api/employees/organization/{orgId}
```
**示例**：`GET /api/employees/organization/2`

### 2.4 搜索员工
```
GET /api/employees/search?name=张&organizationId=2&status=1
```
**参数说明**：
- `name`：员工姓名（模糊搜索）
- `organizationId`：机构ID
- `status`：员工状态（1-在职，0-离职）

### 2.5 按员工编号获取员工
```
GET /api/employees/by-employee-no/{employeeNo}
```
**示例**：`GET /api/employees/by-employee-no/EMP001`

### 2.6 创建员工
```
POST /api/employees
Content-Type: application/json

{
  "name": "新员工",
  "employeeNo": "EMP006",
  "phone": "13900000006",
  "email": "newuser@company.com",
  "organizationId": 2,
  "position": "开发工程师",
  "status": 1
}
```

### 2.7 更新员工
```
PUT /api/employees/{id}
Content-Type: application/json

{
  "name": "更新后的姓名",
  "phone": "13900000007",
  "email": "updated@company.com",
  "organizationId": 2,
  "position": "高级开发工程师",
  "status": 1
}
```

### 2.8 删除员工
```
DELETE /api/employees/{id}
```

---

## 三、短信管理 API

### 3.1 批量发送短信（核心功能）
```
POST /api/sms/batch
Content-Type: application/json

{
  "employeeIds": [1, 2, 3, 4, 5],
  "content": "【公司通知】请各位员工按时参加明天上午9点的全员大会，请勿迟到。",
  "batchId": "BATCH_20240101001"
}
```

**功能说明**：
- 异步并发发送：使用@Async注解实现异步处理
- 线程池管理：自定义线程池配置（核心线程5个，最大10个）
- 发送模拟：每条短信随机延迟1-3秒，90%成功率
- 状态跟踪：实时更新每条短信的发送状态

### 3.2 获取短信记录
```
GET /api/sms/records/{batchId}
```
**示例**：`GET /api/sms/records/BATCH_20240101001`

**响应示例**：
```json
[
  {
    "id": 1,
    "batchId": "BATCH_20240101001",
    "employeeId": 1,
    "phone": "13900000001",
    "content": "【公司通知】请各位员工按时参加明天上午9点的全员大会，请勿迟到。",
    "status": 1,
    "sendTime": "2024-01-01T10:30:00",
    "employee": {
      "id": 1,
      "name": "张三",
      "employeeNo": "EMP001"
    }
  }
]
```

### 3.3 获取发送统计
```
GET /api/sms/statistics/{batchId}
```
**示例**：`GET /api/sms/statistics/BATCH_20240101001`

**响应示例**：
```
批次 BATCH_20240101001 统计：总计 5 条，成功 4 条，失败 1 条，待发送 0 条
```

### 3.4 发送单条短信
```
POST /api/sms/single?phone=13900000001&content=测试短信内容
```

---

## 四、演示数据

### 4.1 机构数据
```sql
INSERT INTO organization (id, name, parent_id, level, status) VALUES
(1, '总公司', NULL, 1, 1),
(2, '技术部', 1, 2, 1),
(3, '产品部', 1, 2, 1),
(4, '前端组', 2, 3, 1),
(5, '后端组', 2, 3, 1);
```

### 4.2 员工数据
```sql
INSERT INTO employee (id, name, employee_no, phone, email, organization_id, position, status) VALUES
(1, '张三', 'EMP001', '13900000001', 'zhangsan@company.com', 4, '前端工程师', 1),
(2, '李四', 'EMP002', '13900000002', 'lisi@company.com', 5, '后端工程师', 1),
(3, '王五', 'EMP003', '13900000003', 'wangwu@company.com', 5, 'Java工程师', 1),
(4, '赵六', 'EMP004', '13900000004', 'zhaoliu@company.com', 3, '产品经理', 1),
(5, '钱七', 'EMP005', '13900000005', 'qianqi@company.com', 2, '技术总监', 1);
```

---

## 五、测试流程

### 5.1 基础数据测试
1. 获取机构树：`GET /api/organizations/tree`
2. 获取所有员工：`GET /api/employees`
3. 按机构查询员工：`GET /api/employees/organization/2`

### 5.2 并发短信发送测试
1. 发送批量短信：
   ```
   POST /api/sms/batch
   {
     "employeeIds": [1, 2, 3, 4, 5],
     "content": "【系统测试】这是一条测试短信，请忽略。"
   }
   ```

2. 查看发送进度：
   ```
   GET /api/sms/records/{返回的batchId}
   ```

3. 查看统计信息：
   ```
   GET /api/sms/statistics/{返回的batchId}
   ```

### 5.3 CRUD 操作测试
1. 创建员工 → 查询验证 → 更新员工 → 删除员工
2. 创建机构 → 查询验证 → 更新机构 → 删除机构

---

## 六、技术实现说明

### 6.1 并发短信发送原理
```java
@Async("smsTaskExecutor")  // 异步执行
public CompletableFuture<String> sendBatchSms(SmsBatchDTO smsBatchDTO) {
    // 1. 创建短信记录
    // 2. 并发发送短信
    List<CompletableFuture<Void>> futures = new ArrayList<>();
    for (SmsRecord record : smsRecords) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            processSingleSms(record);  // 单条短信处理
        });
        futures.add(future);
    }
    // 3. 等待所有任务完成
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
}
```

### 6.2 线程池配置
```java
@Bean("smsTaskExecutor")
public Executor smsTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);        // 核心线程数
    executor.setMaxPoolSize(10);        // 最大线程数
    executor.setQueueCapacity(100);     // 队列容量
    return executor;
}
```

### 6.3 数据库设计亮点
- 机构表支持多级树形结构
- 员工表关联机构，支持按机构查询
- 短信记录表记录发送状态和时间
- 使用MyBatis处理复杂查询和关联

这个系统展示了Spring Boot企业级应用的核心技能：数据库设计、框架使用、并发处理等，适合作为技术面试的演示项目。