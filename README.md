# 企业员工管理系统

一个基于Spring Boot开发的企业级员工管理系统，支持多层级组织架构管理、员工信息管理和批量短信通知功能。

## 技术栈

- **后端框架**: Spring Boot 2.6.13
- **持久层**: MyBatis 2.2.2
- **数据库**: MySQL 8.0+
- **连接池**: HikariCP
- **API文档**: Swagger 2.9.2
- **开发工具**: Lombok

## 主要功能

### 1. 组织架构管理
- 支持无限层级的树形组织结构（如：集团总部/杭州分公司/技术部）
- 组织信息的增删改查
- 自动生成组织编码和完整路径
- 递归算法构建组织架构树

### 2. 员工信息管理
- 员工基本信息维护（姓名、工号、联系方式等）
- 员工与组织关联管理
- 自动生成员工工号
- 支持按组织查询员工

### 3. 批量短信通知
- 异步批量发送短信功能
- 并发处理提升发送效率
- 完整的发送状态跟踪
- 支持批次管理和状态查询

## 快速启动

### 环境要求
- JDK 8+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目到本地**
```bash
# 解压项目文件到本地目录
```

2. **创建数据库**
```bash
# 执行database_design.sql文件创建数据库和初始数据
mysql -u root -p < database_design.sql
```

3. **修改数据库配置**
编辑 `src/main/resources/application.properties`，修改数据库连接信息：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management_system
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. **启动项目**
```bash
mvn spring-boot:run
```

5. **访问系统**
- 应用地址：http://localhost:8080
- API文档：http://localhost:8080/swagger-ui.html

## API接口

系统提供RESTful API接口，主要包括：

### 组织管理
- GET `/api/organizations/tree` - 获取组织架构树
- POST `/api/organizations` - 新增组织
- PUT `/api/organizations/{id}` - 更新组织信息
- DELETE `/api/organizations/{id}` - 删除组织

### 员工管理  
- GET `/api/employees` - 获取员工列表
- POST `/api/employees` - 新增员工
- PUT `/api/employees/{id}` - 更新员工信息
- DELETE `/api/employees/{id}` - 删除员工

### 短信通知
- POST `/api/sms/batch` - 批量发送短信
- GET `/api/sms/records/{batchId}` - 查询发送状态

## 数据库设计

系统采用三张核心表设计：

- **organization**: 组织架构表，支持树形结构存储
- **employee**: 员工信息表，关联组织架构
- **sms_record**: 短信发送记录表，用于状态跟踪

详细的表结构和索引设计见 `database_design.sql` 文件。

## 项目特色

1. **高性能组织架构**：采用parent_id + level + full_path设计，支持快速查询和无限层级扩展

2. **并发短信处理**：双层异步设计（@Async + CompletableFuture），大幅提升批量处理效率

3. **完善的异常处理**：全局异常拦截，统一返回格式，提供友好的错误信息

4. **企业级规范**：标准的三层架构，清晰的代码结构，便于维护和扩展

## 项目结构

```
src/main/java/com/company/ems/
├── controller/          # 控制层
├── service/            # 业务逻辑层
├── mapper/             # 数据访问层
├── entity/             # 实体类
├── dto/                # 数据传输对象
├── config/             # 配置类
├── common/             # 公共类
└── exception/          # 异常处理
```

## 开发说明

本项目严格按照企业级开发规范设计：
- 采用标准的MVC架构模式
- 使用MyBatis进行数据库操作
- 集成Swagger提供API文档
- 支持并发处理和异步操作
- 具备完善的异常处理机制

系统设计考虑了实际业务场景，具备良好的扩展性和可维护性，适合作为企业级应用的基础框架。