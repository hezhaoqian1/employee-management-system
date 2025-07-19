-- 企业员工管理系统数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS employee_management_system 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE employee_management_system;

-- 机构表
CREATE TABLE organization (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL COMMENT '机构名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '机构编码',
    parent_id BIGINT DEFAULT NULL COMMENT '父级机构ID',
    level INT NOT NULL DEFAULT 1 COMMENT '层级',
    full_path VARCHAR(500) DEFAULT NULL COMMENT '完整路径（用/分隔）',
    address VARCHAR(200) DEFAULT NULL COMMENT '地址',
    phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    description TEXT DEFAULT NULL COMMENT '描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    sort_order INT DEFAULT 0 COMMENT '排序字段',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_parent_id (parent_id),
    INDEX idx_code (code),
    INDEX idx_level (level),
    INDEX idx_status (status),
    CONSTRAINT fk_organization_parent FOREIGN KEY (parent_id) REFERENCES organization(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='机构表';

-- 员工表
CREATE TABLE employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    employee_no VARCHAR(20) NOT NULL UNIQUE COMMENT '工号',
    organization_id BIGINT NOT NULL COMMENT '所属机构ID',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    gender TINYINT DEFAULT NULL COMMENT '性别：1-男，2-女',
    position VARCHAR(50) DEFAULT NULL COMMENT '职位',
    department VARCHAR(50) DEFAULT NULL COMMENT '部门',
    hire_date DATE DEFAULT NULL COMMENT '入职日期',
    birthday DATE DEFAULT NULL COMMENT '生日',
    id_card VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    address VARCHAR(200) DEFAULT NULL COMMENT '住址',
    emergency_contact VARCHAR(50) DEFAULT NULL COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) DEFAULT NULL COMMENT '紧急联系人电话',
    salary DECIMAL(10,2) DEFAULT NULL COMMENT '薪资',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-在职，2-离职，3-试用期',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_employee_no (employee_no),
    INDEX idx_organization_id (organization_id),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    INDEX idx_hire_date (hire_date),
    CONSTRAINT fk_employee_organization FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

-- 短信发送记录表
CREATE TABLE sms_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    batch_id VARCHAR(64) NOT NULL COMMENT '批次ID',
    employee_id BIGINT NOT NULL COMMENT '员工ID',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    content TEXT NOT NULL COMMENT '短信内容',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '发送状态：0-待发送，1-发送成功，2-发送失败',
    send_time DATETIME DEFAULT NULL COMMENT '发送时间',
    error_message VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_batch_id (batch_id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_status (status),
    INDEX idx_send_time (send_time),
    CONSTRAINT fk_sms_employee FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短信发送记录表';

-- 插入初始数据
-- 插入根机构
INSERT INTO organization (name, code, parent_id, level, full_path, address, phone, description, sort_order) VALUES
('集团总部', 'HQ', NULL, 1, '/集团总部', '杭州市西湖区', '0571-12345678', '集团总部', 1),
('杭州分公司', 'HZ', 1, 2, '/集团总部/杭州分公司', '杭州市拱墅区', '0571-87654321', '杭州分公司', 1),
('上海分公司', 'SH', 1, 2, '/集团总部/上海分公司', '上海市浦东新区', '021-12345678', '上海分公司', 2);

-- 插入子机构
INSERT INTO organization (name, code, parent_id, level, full_path, address, phone, description, sort_order) VALUES
('技术部', 'HZ_TECH', 2, 3, '/集团总部/杭州分公司/技术部', '杭州市拱墅区科技园A座', '0571-11111111', '技术研发部门', 1),
('人事部', 'HZ_HR', 2, 3, '/集团总部/杭州分公司/人事部', '杭州市拱墅区科技园B座', '0571-22222222', '人力资源部门', 2),
('财务部', 'HZ_FIN', 2, 3, '/集团总部/杭州分公司/财务部', '杭州市拱墅区科技园C座', '0571-33333333', '财务管理部门', 3);

-- 插入员工数据
INSERT INTO employee (name, employee_no, organization_id, phone, email, gender, position, department, hire_date, status) VALUES
('张三', 'EMP001', 4, '13800138001', 'zhangsan@company.com', 1, 'Java开发工程师', '技术部', '2023-01-15', 1),
('李四', 'EMP002', 4, '13800138002', 'lisi@company.com', 1, '前端开发工程师', '技术部', '2023-02-20', 1),
('王五', 'EMP003', 5, '13800138003', 'wangwu@company.com', 2, '人事专员', '人事部', '2023-03-10', 1),
('赵六', 'EMP004', 6, '13800138004', 'zhaoliu@company.com', 2, '会计', '财务部', '2023-04-05', 1),
('孙七', 'EMP005', 4, '13800138005', 'sunqi@company.com', 1, '架构师', '技术部', '2022-12-01', 1);