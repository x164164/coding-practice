-- 编程机构刷题系统 - 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS coding_practice DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE coding_practice;

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `openid` VARCHAR(64) DEFAULT NULL COMMENT '微信openid',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `password` VARCHAR(100) DEFAULT NULL COMMENT '密码(加密存储)',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `role` VARCHAR(20) DEFAULT 'student' COMMENT '角色: student/admin',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1正常',
    `total_questions` INT DEFAULT 0 COMMENT '总做题数',
    `correct_count` INT DEFAULT 0 COMMENT '正确数',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_openid` (`openid`),
    INDEX `idx_role` (`role`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 题目表
-- ============================================
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '题目ID',
    `title` VARCHAR(200) NOT NULL COMMENT '题目标题',
    `content` TEXT NOT NULL COMMENT '题目内容',
    `type` VARCHAR(20) DEFAULT 'choice' COMMENT '类型: choice选择题 code编程题 answer问答题',
    `options` JSON DEFAULT NULL COMMENT '选择题选项 [{"label":"A","content":"..."}]',
    `answer` VARCHAR(500) NOT NULL COMMENT '正确答案',
    `analysis` TEXT DEFAULT NULL COMMENT '答案解析',
    `difficulty` TINYINT DEFAULT 1 COMMENT '难度: 1简单 2中等 3困难',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
    `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签,逗号分隔',
    `image_url` VARCHAR(255) DEFAULT NULL COMMENT '题目图片URL',
    `score` INT DEFAULT 10 COMMENT '分值',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `view_count` INT DEFAULT 0 COMMENT '查看次数',
    `submit_count` INT DEFAULT 0 COMMENT '提交次数',
    `correct_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '正确率',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_category` (`category`),
    INDEX `idx_difficulty` (`difficulty`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

-- ============================================
-- 3. 答题记录表
-- ============================================
DROP TABLE IF EXISTS `submission`;
CREATE TABLE `submission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `user_answer` VARCHAR(500) NOT NULL COMMENT '用户答案',
    `is_correct` TINYINT NOT NULL COMMENT '是否正确: 0错误 1正确',
    `score` INT DEFAULT 0 COMMENT '得分',
    `time_spent` INT DEFAULT 0 COMMENT '答题耗时(秒)',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `device` VARCHAR(50) DEFAULT NULL COMMENT '设备信息',
    `submit_time` DATETIME NOT NULL COMMENT '提交时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_question_id` (`question_id`),
    INDEX `idx_submit_time` (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='答题记录表';

-- ============================================
-- 4. 错题本表
-- ============================================
DROP TABLE IF EXISTS `wrong_note`;
CREATE TABLE `wrong_note` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `user_answer` VARCHAR(500) DEFAULT NULL COMMENT '用户答案',
    `correct_answer` VARCHAR(500) DEFAULT NULL COMMENT '正确答案',
    `wrong_count` INT DEFAULT 1 COMMENT '错误次数',
    `last_wrong_time` DATETIME DEFAULT NULL COMMENT '最后一次错误时间',
    `is_reviewed` TINYINT DEFAULT 0 COMMENT '是否已复习',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_question_id` (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题本表';

-- ============================================
-- 5. 收藏表
-- ============================================
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_question` (`user_id`, `question_id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ============================================
-- 6. 分类表
-- ============================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `question_count` INT DEFAULT 0 COMMENT '题目数量',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0禁用 1启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_sort` (`sort`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- ============================================
-- 7. 系统配置表
-- ============================================
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `config_key` VARCHAR(50) NOT NULL COMMENT '配置键',
    `config_value` VARCHAR(500) DEFAULT NULL COMMENT '配置值',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ============================================
-- 初始化管理员账号 (admin/123456)
-- ============================================
INSERT INTO `user` (`username`, `nickname`, `password`, `role`, `status`) 
VALUES ('admin', '管理员', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin', 1);

-- ============================================
-- 初始化分类数据
-- ============================================
INSERT INTO `category` (`name`, `icon`, `description`, `sort`) VALUES
('数组', '📚', '数组相关题目', 1),
('字符串', '🔤', '字符串处理', 2),
('链表', '🔗', '链表操作', 3),
('树', '🌲', '树结构', 4),
('堆与优先队列', '📊', '堆与优先队列', 5),
('哈希表', '🔍', '哈希表应用', 6),
('动态规划', '⚡', '动态规划', 7),
('贪心算法', '🎯', '贪心算法', 8),
('回溯算法', '🔙', '回溯算法', 9),
('分治算法', '➗', '分治算法', 10);

-- ============================================
-- 初始化系统配置
-- ============================================
INSERT INTO `sys_config` (`config_key`, `config_value`, `description`) VALUES
('site_name', '编程刷题系统', '网站名称'),
('site_logo', '', '网站Logo'),
('wechat_appid', '', '微信AppID'),
('wechat_secret', '', '微信AppSecret'),
('qiniu_access_key', '', '七牛云AccessKey'),
('qiniu_secret_key', '', '七牛云SecretKey'),
('qiniu_bucket', '', '七牛云存储桶名'),
('qiniu_domain', '', '七牛云CDN域名');