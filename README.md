# 编程机构刷题系统 - 技术架构

## 系统架构

```
┌─────────────────────────────────────────────────────────┐
│                      客户端                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐    │
│  │  微信小程序  │  │   安卓App   │  │   iOS App   │    │
│  └─────────────┘  └─────────────┘  └─────────────┘    │
└─────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────┐
│                    后端服务 (Java)                       │
│  ┌──────────────────────────────────────────────────┐ │
│  │              Spring Boot + MyBatis               │ │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐            │ │
│  │  │ 用户API │ │ 题目API │ │ 答题API │ │ 统计API  │ │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────┘  │ │
│  └──────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
      ┌───────────────┐               ┌───────────────┐
      │    MySQL      │               │   七牛云OSS   │
      │  (数据库)     │               │   (文件存储)  │
      └───────────────┘               └───────────────┘

┌─────────────────────────────────────────────────────────┐
│              后台管理面板 (Vue.js)                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐    │
│  │  题库管理   │  │  用户管理   │  │  数据统计   │    │
│  └─────────────┘  └─────────────┘  └─────────────┘    │
└─────────────────────────────────────────────────────────┘
```

## 技术栈

| 层级 | 技术 |
|------|------|
| **后端** | Spring Boot 2.7 + MyBatis-Plus + MySQL |
| **后台前端** | Vue 3 + Element Plus |
| **小程序** | 原生开发 |
| **部署** | 腾讯云/阿里云服务器 |

## 数据库表设计

### 1. user - 用户表
```sql
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `openid` VARCHAR(64) COMMENT '微信openid',
  `username` VARCHAR(50) COMMENT '用户名',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `phone` VARCHAR(20) COMMENT '手机号',
  `role` VARCHAR(20) DEFAULT 'student' COMMENT '角色: student/admin',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1正常',
  `created_at` DATETIME,
  `updated_at` DATETIME
);
```

### 2. question - 题目表
```sql
CREATE TABLE `question` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(200) COMMENT '题目标题',
  `content` TEXT COMMENT '题目内容',
  `type` VARCHAR(20) DEFAULT 'choice' COMMENT '类型: choice/code/answer',
  `options` JSON COMMENT '选择题选项',
  `answer` VARCHAR(500) COMMENT '正确答案',
  `analysis` TEXT COMMENT '解析',
  `difficulty` TINYINT DEFAULT 1 COMMENT '难度: 1简单 2中等 3困难',
  `category` VARCHAR(50) COMMENT '分类',
  `tags` VARCHAR(255) COMMENT '标签',
  `image_url` VARCHAR(255) COMMENT '题目图片',
  `created_by` BIGINT COMMENT '创建者ID',
  `created_at` DATETIME,
  `updated_at` DATETIME
);
```

### 3. submission - 答题记录表
```sql
CREATE TABLE `submission` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT COMMENT '用户ID',
  `question_id` BIGINT COMMENT '题目ID',
  `user_answer` VARCHAR(500) COMMENT '用户答案',
  `is_correct` TINYINT COMMENT '是否正确',
  `submit_time` DATETIME,
  `created_at` DATETIME
);
```

### 4. wrong_note - 错题本
```sql
CREATE TABLE `wrong_note` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT COMMENT '用户ID',
  `question_id` BIGINT COMMENT '题目ID',
  `user_answer` VARCHAR(500) COMMENT '错误答案',
  `correct_answer` VARCHAR(500) COMMENT '正确答案',
  `created_at` DATETIME
);
```

### 5. category - 分类表
```sql
CREATE TABLE `category` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) COMMENT '分类名称',
  `icon` VARCHAR(100) COMMENT '图标',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `created_at` DATETIME
);
```

## API 接口设计

### 用户模块
- `POST /api/user/login` - 微信登录
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/info` - 更新用户信息

### 题目模块
- `GET /api/question/list` - 题目列表
- `GET /api/question/{id}` - 题目详情
- `POST /api/question` - 添加题目 (管理员)
- `PUT /api/question/{id}` - 更新题目 (管理员)
- `DELETE /api/question/{id}` - 删除题目 (管理员)

### 答题模块
- `POST /api/submit` - 提交答案
- `GET /api/submission/history` - 答题历史
- `GET /api/wrong-notes` - 错题本

### 统计模块
- `GET /api/stat/progress` - 用户进度
- `GET /api/stat/ranking` - 排行榜
- `GET /api/stat/overview` - 整体数据 (管理员)

### 管理模块
- `GET /api/admin/question/list` - 题目管理
- `GET /api/admin/user/list` - 用户管理
- `GET /api/admin/stats` - 数据统计

## 目录结构

```
coding-practice/
├── backend/                  # Java后端
│   ├── src/
│   │   └── main/
│   │       ├── java/com/practice/
│   │       │   ├── controller/   # 控制器
│   │       │   ├── service/    # 业务逻辑
│   │       │   ├── mapper/      # 数据访问
│   │       │   ├── entity/      # 实体类
│   │       │   ├── dto/         # 数据传输对象
│   │       │   └── config/      # 配置类
│   │       └── resources/
│   │           ├── application.yml
│   │           └── mapper/      # MyBatis XML
│   └── pom.xml
│
├── admin/                    # 后台管理前端 (Vue)
│   ├── pages/
│   ├── api/
│   └── package.json
│
├── miniprogram/              # 微信小程序
│   └── ...
│
└── README.md
```