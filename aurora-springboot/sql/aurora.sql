SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_about
-- ----------------------------
DROP TABLE IF EXISTS `t_about`;
CREATE TABLE `t_about`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_about
-- ----------------------------
INSERT INTO `t_about` VALUES (1, '{\"content\":\"this is about\"}', '2022-07-24 17:22:13', '2022-09-23 14:37:34');

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '作者',
  `category_id` int NULL DEFAULT NULL COMMENT '文章分类',
  `article_cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章缩略图',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶 0否 1是',
  `is_featured` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否推荐 0否 1是',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除  0否 1是',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态值 1公开 2私密 3草稿',
  `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '文章类型 1原创 2转载 3翻译',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问密码',
  `original_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原文链接',
  `create_time` datetime NOT NULL COMMENT '发表时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL COMMENT '文章id',
  `tag_id` int NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_article_tag_1`(`article_id` ASC) USING BTREE,
  INDEX `fk_article_tag_2`(`tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 217 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '评论用户Id',
  `topic_id` int NULL DEFAULT NULL COMMENT '评论主题id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `reply_user_id` int NULL DEFAULT NULL COMMENT '回复用户id',
  `parent_id` int NULL DEFAULT NULL COMMENT '父评论id',
  `type` tinyint NOT NULL COMMENT '评论类型 1.文章 2.留言 3.关于我 4.友链 5.说说',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除  0否 1是',
  `is_review` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否审核',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_comment_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_comment_parent`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1032 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `t_friend_link`;
CREATE TABLE `t_friend_link`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `link_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接名',
  `link_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接头像',
  `link_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接地址',
  `link_intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接介绍',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_friend_link_user`(`link_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_job
-- ----------------------------
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` tinyint(1) NULL DEFAULT 3 COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` tinyint(1) NULL DEFAULT 1 COMMENT '是否并发执行（0禁止 1允许）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0暂停 1正常）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_job
-- ----------------------------
INSERT INTO `t_job` VALUES (81, '统计用户地域分布', '默认', 'auroraQuartz.statisticalUserArea', '0 0,30 * * * ?', 3, 1, 1, '2022-08-11 21:49:27', '2022-08-13 08:49:47', '统计用户的地域分布');
INSERT INTO `t_job` VALUES (82, '统计访问量', '默认', 'auroraQuartz.saveUniqueView', '0 0 0 * * ?', 3, 1, 1, '2022-08-12 16:35:11', NULL, '向数据库中写入每天的访问量');
INSERT INTO `t_job` VALUES (83, '清空redis访客记录', '默认', 'auroraQuartz.clear', '0 0 1 * * ?', 3, 1, 1, '2022-08-12 16:36:30', '2022-08-13 08:47:48', '清空redis访客记录');
INSERT INTO `t_job` VALUES (84, '百度SEO', '默认', 'auroraQuartz.baiduSeo', '0 0/10 * * * ?', 3, 1, 1, '2022-08-13 21:19:08', '2022-08-19 14:13:52', '百度SEO');
INSERT INTO `t_job` VALUES (85, '清理定时任务日志', '默认', 'auroraQuartz.clearJobLogs', '0 0 0 * * ?', 3, 1, 1, '2022-08-13 21:26:21', NULL, '清理定时任务日志');

-- ----------------------------
-- Table structure for t_job_log
-- ----------------------------
DROP TABLE IF EXISTS `t_job_log`;
CREATE TABLE `t_job_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_id` int NOT NULL COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6175 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单路径',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组件',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单icon',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `order_num` tinyint(1) NOT NULL COMMENT '排序',
  `parent_id` int NULL DEFAULT NULL COMMENT '父id',
  `is_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏  0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 225 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (1, '首页', '/', '/home/Home.vue', 'el-icon-myshouye', '2021-01-26 17:06:51', '2022-07-27 16:33:11', 1, NULL, 0);
INSERT INTO `t_menu` VALUES (2, '文章管理', '/article-submenu', 'Layout', 'el-icon-mywenzhang-copy', '2021-01-25 20:43:07', '2022-07-27 16:32:55', 2, NULL, 0);
INSERT INTO `t_menu` VALUES (3, '消息管理', '/message-submenu', 'Layout', 'el-icon-myxiaoxi', '2021-01-25 20:44:17', '2022-07-27 16:32:57', 3, NULL, 0);
INSERT INTO `t_menu` VALUES (4, '系统管理', '/system-submenu', 'Layout', 'el-icon-myshezhi', '2021-01-25 20:45:57', '2021-01-25 20:45:59', 5, NULL, 0);
INSERT INTO `t_menu` VALUES (5, '个人中心', '/setting', '/setting/Setting.vue', 'el-icon-myuser', '2021-01-26 17:22:38', '2021-01-26 17:22:41', 7, NULL, 0);
INSERT INTO `t_menu` VALUES (6, '发布文章', '/articles', '/article/Article.vue', 'el-icon-myfabiaowenzhang', '2021-01-26 14:30:48', '2021-01-26 14:30:51', 1, 2, 0);
INSERT INTO `t_menu` VALUES (7, '修改文章', '/articles/*', '/article/Article.vue', 'el-icon-myfabiaowenzhang', '2021-01-26 14:31:32', '2022-07-28 16:28:06', 2, 2, 1);
INSERT INTO `t_menu` VALUES (8, '文章列表', '/article-list', '/article/ArticleList.vue', 'el-icon-mywenzhangliebiao', '2021-01-26 14:32:13', '2021-01-26 14:32:16', 3, 2, 0);
INSERT INTO `t_menu` VALUES (9, '分类管理', '/categories', '/category/Category.vue', 'el-icon-myfenlei', '2021-01-26 14:33:42', '2021-01-26 14:33:43', 4, 2, 0);
INSERT INTO `t_menu` VALUES (10, '标签管理', '/tags', '/tag/Tag.vue', 'el-icon-myicontag', '2021-01-26 14:34:33', '2021-01-26 14:34:36', 5, 2, 0);
INSERT INTO `t_menu` VALUES (11, '评论管理', '/comments', '/comment/Comment.vue', 'el-icon-mypinglunzu', '2021-01-26 14:35:31', '2021-01-26 14:35:34', 1, 3, 0);
INSERT INTO `t_menu` VALUES (13, '用户列表', '/users', '/user/User.vue', 'el-icon-myyonghuliebiao', '2021-01-26 14:38:09', '2021-01-26 14:38:12', 1, 202, 0);
INSERT INTO `t_menu` VALUES (14, '角色管理', '/roles', '/role/Role.vue', 'el-icon-myjiaoseliebiao', '2021-01-26 14:39:01', '2021-01-26 14:39:03', 2, 213, 0);
INSERT INTO `t_menu` VALUES (15, '接口管理', '/resources', '/resource/Resource.vue', 'el-icon-myjiekouguanli', '2021-01-26 14:40:14', '2021-08-07 20:00:28', 2, 213, 0);
INSERT INTO `t_menu` VALUES (16, '菜单管理', '/menus', '/menu/Menu.vue', 'el-icon-mycaidan', '2021-01-26 14:40:54', '2021-08-07 10:18:49', 2, 213, 0);
INSERT INTO `t_menu` VALUES (17, '友链管理', '/links', '/friendLink/FriendLink.vue', 'el-icon-mydashujukeshihuaico-', '2021-01-26 14:41:35', '2021-01-26 14:41:37', 3, 4, 0);
INSERT INTO `t_menu` VALUES (18, '关于我', '/about', '/about/About.vue', 'el-icon-myguanyuwo', '2021-01-26 14:42:05', '2021-01-26 14:42:10', 4, 4, 0);
INSERT INTO `t_menu` VALUES (19, '日志管理', '/log-submenu', 'Layout', 'el-icon-myguanyuwo', '2021-01-31 21:33:56', '2021-01-31 21:33:59', 6, NULL, 0);
INSERT INTO `t_menu` VALUES (20, '操作日志', '/operation/log', '/log/OperationLog.vue', 'el-icon-myguanyuwo', '2021-01-31 15:53:21', '2022-07-28 10:51:28', 1, 19, 0);
INSERT INTO `t_menu` VALUES (201, '在线用户', '/online/users', '/user/Online.vue', 'el-icon-myyonghuliebiao', '2021-02-05 14:59:51', '2021-02-05 14:59:53', 7, 202, 0);
INSERT INTO `t_menu` VALUES (202, '用户管理', '/users-submenu', 'Layout', 'el-icon-myyonghuliebiao', '2021-02-06 23:44:59', '2022-07-27 16:32:59', 4, NULL, 0);
INSERT INTO `t_menu` VALUES (205, '相册管理', '/album-submenu', 'Layout', 'el-icon-myimage-fill', '2021-08-03 15:10:54', '2021-08-07 20:02:06', 5, NULL, 0);
INSERT INTO `t_menu` VALUES (206, '相册列表', '/albums', '/album/Album.vue', 'el-icon-myzhaopian', '2021-08-03 20:29:19', '2021-08-04 11:45:47', 1, 205, 0);
INSERT INTO `t_menu` VALUES (208, '照片管理', '/albums/:albumId', '/album/Photo.vue', 'el-icon-myzhaopian', '2021-08-03 21:37:47', '2021-08-05 10:24:08', 1, 205, 1);
INSERT INTO `t_menu` VALUES (209, '定时任务', '/quartz', '/quartz/Quartz.vue', 'el-icon-myyemianpeizhi', '2021-08-04 11:36:27', '2021-08-07 20:01:26', 2, 4, 0);
INSERT INTO `t_menu` VALUES (210, '照片回收站', '/photos/delete', '/album/Delete.vue', 'el-icon-myhuishouzhan', '2021-08-05 13:55:19', NULL, 3, 205, 1);
INSERT INTO `t_menu` VALUES (213, '权限管理', '/permission-submenu', 'Layout', 'el-icon-mydaohanglantubiao_quanxianguanli', '2021-08-07 19:56:55', '2021-08-07 19:59:40', 4, NULL, 0);
INSERT INTO `t_menu` VALUES (214, '网站管理', '/website', '/website/Website.vue', 'el-icon-myxitong', '2021-08-07 20:06:41', NULL, 1, 4, 0);
INSERT INTO `t_menu` VALUES (220, '定时任务日志', '/quartz/log/:quartzId', '/log/QuartzLog.vue', 'el-icon-myguanyuwo', '2022-07-28 10:53:23', '2022-08-05 10:27:47', 2, 19, 1);
INSERT INTO `t_menu` VALUES (221, '说说管理', '/talk-submenu', 'Layout', 'el-icon-mypinglun', '2022-08-15 17:27:10', '2022-08-15 17:27:39', 3, NULL, 0);
INSERT INTO `t_menu` VALUES (222, '说说列表', '/talk-list', '/talk/TalkList.vue', 'el-icon-myiconfontdongtaidianji', '2022-08-15 17:29:05', NULL, 1, 221, 0);
INSERT INTO `t_menu` VALUES (223, '发布说说', '/talks', '/talk/Talk.vue', 'el-icon-myfabusekuai', '2022-08-15 17:34:26', '2022-08-16 16:06:04', 2, 221, 0);
INSERT INTO `t_menu` VALUES (224, '修改说说', '/talks/:talkId', '/talk/Talk.vue', 'el-icon-myfabusekuai', '2022-08-16 16:06:59', '2022-08-16 16:08:21', 3, 221, 1);
INSERT INTO `t_menu` VALUES (225, '异常日志', '/exception/log', '/log/ExceptionLog.vue', 'el-icon-myguanyuwo', '2022-08-25 13:40:08', '2022-08-25 13:40:31', 1, 19, 0);

-- ----------------------------
-- Table structure for t_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `opt_module` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作模块',
  `opt_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `opt_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作url',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作方法',
  `opt_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作描述',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求参数',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方式',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '返回数据',
  `user_id` int NOT NULL COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1671 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_photo
-- ----------------------------
DROP TABLE IF EXISTS `t_photo`;
CREATE TABLE `t_photo`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `album_id` int NOT NULL COMMENT '相册id',
  `photo_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '照片名',
  `photo_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '照片描述',
  `photo_src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '照片地址',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '照片' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_photo_album
-- ----------------------------
DROP TABLE IF EXISTS `t_photo_album`;
CREATE TABLE `t_photo_album`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `album_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '相册名',
  `album_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '相册描述',
  `album_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '相册封面',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态值 1公开 2私密',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '相册' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源名',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限路径',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方式',
  `parent_id` int NULL DEFAULT NULL COMMENT '父模块id',
  `is_anonymous` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否匿名访问 0否 1是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 506 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_resource
-- ----------------------------
INSERT INTO `t_resource` VALUES (1050, 'aurora信息', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1051, '分类模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1052, '友链模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1053, '定时任务日志模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1054, '定时任务模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1055, '异常处理模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1056, '操作日志模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1057, '文章模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1058, '标签模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1059, '照片模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1060, '用户信息模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1061, '用户账号模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1062, '相册模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1063, '菜单模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1064, '角色模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1065, '评论模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1066, '说说模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1067, '资源模块', NULL, NULL, NULL, 0, '2022-08-19 22:26:21', NULL);
INSERT INTO `t_resource` VALUES (1068, '获取系统信息', '/', 'GET', 1050, 1, '2022-08-19 22:26:22', '2022-08-19 22:26:55');
INSERT INTO `t_resource` VALUES (1069, '查看关于我信息', '/about', 'GET', 1050, 1, '2022-08-19 22:26:22', '2022-08-19 22:26:57');
INSERT INTO `t_resource` VALUES (1070, '获取系统后台信息', '/admin', 'GET', 1050, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1071, '修改关于我信息', '/admin/about', 'PUT', 1050, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1072, '获取后台文章', '/admin/articles', 'GET', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1073, '保存和修改文章', '/admin/articles', 'POST', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1074, '删除或者恢复文章', '/admin/articles', 'PUT', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1075, '物理删除文章', '/admin/articles/delete', 'DELETE', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1076, '导出文章', '/admin/articles/export', 'POST', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1077, '上传文章图片', '/admin/articles/images', 'POST', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1078, '导入文章', '/admin/articles/import', 'POST', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1079, '修改文章是否置顶和推荐', '/admin/articles/topAndFeatured', 'PUT', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1080, '根据id查看后台文章', '/admin/articles/*', 'GET', 1057, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1081, '查看后台分类列表', '/admin/categories', 'GET', 1051, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1082, '添加或修改分类', '/admin/categories', 'POST', 1051, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1083, '删除分类', '/admin/categories', 'DELETE', 1051, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1084, '搜索文章分类', '/admin/categories/search', 'GET', 1051, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1085, '查询后台评论', '/admin/comments', 'GET', 1065, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1086, '删除评论', '/admin/comments', 'DELETE', 1065, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1087, '审核评论', '/admin/comments/review', 'PUT', 1065, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1088, '上传博客配置图片', '/admin/config/images', 'POST', 1050, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1089, '获取定时任务的日志列表', '/admin/jobLogs', 'GET', 1053, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1090, '删除定时任务的日志', '/admin/jobLogs', 'DELETE', 1053, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1091, '清除定时任务的日志', '/admin/jobLogs/clean', 'DELETE', 1053, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1092, '获取定时任务日志的所有组名', '/admin/jobLogs/jobGroups', 'GET', 1053, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1093, '获取任务列表', '/admin/jobs', 'GET', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1094, '添加定时任务', '/admin/jobs', 'POST', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1095, '修改定时任务', '/admin/jobs', 'PUT', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1096, '删除定时任务', '/admin/jobs', 'DELETE', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1097, '获取所有job分组', '/admin/jobs/jobGroups', 'GET', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1098, '执行某个任务', '/admin/jobs/run', 'PUT', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1099, '更改任务的状态', '/admin/jobs/status', 'PUT', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1100, '根据id获取任务', '/admin/jobs/*', 'GET', 1054, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1101, '查看后台友链列表', '/admin/links', 'GET', 1052, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1102, '保存或修改友链', '/admin/links', 'POST', 1052, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1103, '删除友链', '/admin/links', 'DELETE', 1052, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1104, '查看菜单列表', '/admin/menus', 'GET', 1063, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1105, '新增或修改菜单', '/admin/menus', 'POST', 1063, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1106, '修改目录是否隐藏', '/admin/menus/isHidden', 'PUT', 1063, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1107, '删除菜单', '/admin/menus/*', 'DELETE', 1063, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1108, '查看操作日志', '/admin/operation/logs', 'GET', 1056, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1109, '删除操作日志', '/admin/operation/logs', 'DELETE', 1056, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1110, '根据相册id获取照片列表', '/admin/photos', 'GET', 1059, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1111, '保存照片', '/admin/photos', 'POST', 1059, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1112, '更新照片信息', '/admin/photos', 'PUT', 1059, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1113, '删除照片', '/admin/photos', 'DELETE', 1059, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1114, '移动照片相册', '/admin/photos/album', 'PUT', 1059, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1115, '查看后台相册列表', '/admin/photos/albums', 'GET', 1062, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1116, '保存或更新相册', '/admin/photos/albums', 'POST', 1062, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1117, '上传相册封面', '/admin/photos/albums/cover', 'POST', 1062, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1118, '获取后台相册列表信息', '/admin/photos/albums/info', 'GET', 1062, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1119, '根据id删除相册', '/admin/photos/albums/*', 'DELETE', 1062, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1120, '根据id获取后台相册信息', '/admin/photos/albums/*/info', 'GET', 1062, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1121, '更新照片删除状态', '/admin/photos/delete', 'PUT', 1059, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1122, '查看资源列表', '/admin/resources', 'GET', 1067, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1123, '新增或修改资源', '/admin/resources', 'POST', 1067, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1124, '删除资源', '/admin/resources/*', 'DELETE', 1067, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1125, '保存或更新角色', '/admin/role', 'POST', 1064, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1126, '查看角色菜单选项', '/admin/role/menus', 'GET', 1063, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1127, '查看角色资源选项', '/admin/role/resources', 'GET', 1067, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1128, '查询角色列表', '/admin/roles', 'GET', 1064, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1129, '删除角色', '/admin/roles', 'DELETE', 1064, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1130, '查询后台标签列表', '/admin/tags', 'GET', 1058, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1131, '添加或修改标签', '/admin/tags', 'POST', 1058, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1132, '删除标签', '/admin/tags', 'DELETE', 1058, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1133, '搜索文章标签', '/admin/tags/search', 'GET', 1058, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1134, '查看后台说说', '/admin/talks', 'GET', 1066, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1135, '保存或修改说说', '/admin/talks', 'POST', 1066, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1136, '删除说说', '/admin/talks', 'DELETE', 1066, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1137, '上传说说图片', '/admin/talks/images', 'POST', 1066, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1138, '根据id查看后台说说', '/admin/talks/*', 'GET', 1066, 1, '2022-08-19 22:26:22', '2022-08-19 22:33:52');
INSERT INTO `t_resource` VALUES (1139, '查看当前用户菜单', '/admin/user/menus', 'GET', 1063, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1140, '查询后台用户列表', '/admin/users', 'GET', 1061, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1141, '获取用户区域分布', '/admin/users/area', 'GET', 1061, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1142, '修改用户禁用状态', '/admin/users/disable', 'PUT', 1060, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1143, '查看在线用户', '/admin/users/online', 'GET', 1060, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1144, '修改管理员密码', '/admin/users/password', 'PUT', 1061, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1145, '查询用户角色选项', '/admin/users/role', 'GET', 1064, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1146, '修改用户角色', '/admin/users/role', 'PUT', 1060, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1147, '下线用户', '/admin/users/*/online', 'DELETE', 1060, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1148, '获取网站配置', '/admin/website/config', 'GET', 1050, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1149, '更新网站配置', '/admin/website/config', 'PUT', 1050, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1150, '根据相册id查看照片列表', '/albums/*/photos', 'GET', 1059, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:54');
INSERT INTO `t_resource` VALUES (1151, '获取所有文章归档', '/archives/all', 'GET', 1057, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:35');
INSERT INTO `t_resource` VALUES (1152, '获取所有文章', '/articles/all', 'GET', 1057, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:37');
INSERT INTO `t_resource` VALUES (1153, '根据分类id获取文章', '/articles/categoryId', 'GET', 1057, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:38');
INSERT INTO `t_resource` VALUES (1154, '搜索文章', '/articles/search', 'GET', 1057, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:40');
INSERT INTO `t_resource` VALUES (1155, '根据标签id获取文章', '/articles/tagId', 'GET', 1057, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:40');
INSERT INTO `t_resource` VALUES (1156, '获取置顶和推荐文章', '/articles/topAndFeatured', 'GET', 1057, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:41');
INSERT INTO `t_resource` VALUES (1157, '根据id获取文章', '/articles/*', 'GET', 1057, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:42');
INSERT INTO `t_resource` VALUES (1158, '/处理BizException', '/bizException', 'GET', 1055, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1159, '/处理BizException', '/bizException', 'HEAD', 1055, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1160, '/处理BizException', '/bizException', 'POST', 1055, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1161, '/处理BizException', '/bizException', 'PUT', 1055, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1162, '/处理BizException', '/bizException', 'DELETE', 1055, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1163, '/处理BizException', '/bizException', 'OPTIONS', 1055, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1164, '/处理BizException', '/bizException', 'PATCH', 1055, 0, '2022-08-19 22:26:22', NULL);
INSERT INTO `t_resource` VALUES (1165, '获取所有分类', '/categories/all', 'GET', 1051, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:05');
INSERT INTO `t_resource` VALUES (1166, '获取评论', '/comments', 'GET', 1065, 1, '2022-08-19 22:26:22', '2022-08-19 22:33:50');
INSERT INTO `t_resource` VALUES (1167, '添加评论', '/comments/save', 'POST', 1065, 0, '2022-08-19 22:26:22', '2022-08-19 22:33:47');
INSERT INTO `t_resource` VALUES (1168, '获取前七个评论', '/comments/topSeven', 'GET', 1065, 1, '2022-08-19 22:26:22', '2022-08-19 22:33:44');
INSERT INTO `t_resource` VALUES (1169, '查看友链列表', '/links', 'GET', 1052, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:13');
INSERT INTO `t_resource` VALUES (1170, '获取相册列表', '/photos/albums', 'GET', 1062, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:25');
INSERT INTO `t_resource` VALUES (1171, 'report', '/report', 'POST', 1050, 1, '2022-08-19 22:26:22', '2022-08-19 22:27:00');
INSERT INTO `t_resource` VALUES (1172, '获取所有标签', '/tags/all', 'GET', 1058, 1, '2022-08-19 22:26:22', '2022-08-19 22:31:23');
INSERT INTO `t_resource` VALUES (1173, '获取前十个标签', '/tags/topTen', 'GET', 1058, 1, '2022-08-19 22:26:22', '2022-08-19 22:31:27');
INSERT INTO `t_resource` VALUES (1174, '查看说说列表', '/talks', 'GET', 1066, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:38');
INSERT INTO `t_resource` VALUES (1175, '根据id查看说说', '/talks/*', 'GET', 1066, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:38');
INSERT INTO `t_resource` VALUES (1176, '更新用户头像', '/users/avatar', 'POST', 1060, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:05');
INSERT INTO `t_resource` VALUES (1177, '发送邮箱验证码', '/users/code', 'GET', 1061, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:15');
INSERT INTO `t_resource` VALUES (1178, '绑定用户邮箱', '/users/email', 'PUT', 1060, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:06');
INSERT INTO `t_resource` VALUES (1179, '更新用户信息', '/users/info', 'PUT', 1060, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:07');
INSERT INTO `t_resource` VALUES (1180, '根据id获取用户信息', '/users/info/*', 'GET', 1060, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:07');
INSERT INTO `t_resource` VALUES (1181, '用户登出', '/users/logout', 'POST', 1061, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:15');
INSERT INTO `t_resource` VALUES (1182, 'qq登录', '/users/oauth/qq', 'POST', 1061, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:16');
INSERT INTO `t_resource` VALUES (1183, '修改密码', '/users/password', 'PUT', 1061, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:18');
INSERT INTO `t_resource` VALUES (1184, '用户注册', '/users/register', 'POST', 1061, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:17');
INSERT INTO `t_resource` VALUES (1185, '修改用户的订阅状态', '/users/subscribe', 'PUT', 1060, 1, '2022-08-19 22:26:22', '2022-08-19 22:28:08');
INSERT INTO `t_resource` VALUES (1186, '异常日志模块', NULL, NULL, NULL, 0, '2022-08-25 15:13:40', NULL);
INSERT INTO `t_resource` VALUES (1187, '获取异常日志', '/admin/exception/logs', 'GET', 1186, 0, '2022-08-25 15:14:27', NULL);
INSERT INTO `t_resource` VALUES (1188, '删除异常日志', '/admin/exception/logs', 'DELETE', 1186, 0, '2022-08-25 15:14:59', NULL);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用  0否 1是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'admin', 0, '2022-07-20 13:25:19', '2022-08-16 16:07:49');
INSERT INTO `t_role` VALUES (2, 'user', 0, '2022-07-20 13:25:40', '2022-08-19 22:55:26');
INSERT INTO `t_role` VALUES (14, 'test', 0, '2022-08-19 21:48:14', '2022-08-19 22:38:15');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2751 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES (2784, 1, 1);
INSERT INTO `t_role_menu` VALUES (2785, 1, 2);
INSERT INTO `t_role_menu` VALUES (2786, 1, 6);
INSERT INTO `t_role_menu` VALUES (2787, 1, 7);
INSERT INTO `t_role_menu` VALUES (2788, 1, 8);
INSERT INTO `t_role_menu` VALUES (2789, 1, 9);
INSERT INTO `t_role_menu` VALUES (2790, 1, 10);
INSERT INTO `t_role_menu` VALUES (2791, 1, 3);
INSERT INTO `t_role_menu` VALUES (2792, 1, 11);
INSERT INTO `t_role_menu` VALUES (2793, 1, 221);
INSERT INTO `t_role_menu` VALUES (2794, 1, 222);
INSERT INTO `t_role_menu` VALUES (2795, 1, 223);
INSERT INTO `t_role_menu` VALUES (2796, 1, 224);
INSERT INTO `t_role_menu` VALUES (2797, 1, 202);
INSERT INTO `t_role_menu` VALUES (2798, 1, 13);
INSERT INTO `t_role_menu` VALUES (2799, 1, 201);
INSERT INTO `t_role_menu` VALUES (2800, 1, 213);
INSERT INTO `t_role_menu` VALUES (2801, 1, 14);
INSERT INTO `t_role_menu` VALUES (2802, 1, 15);
INSERT INTO `t_role_menu` VALUES (2803, 1, 16);
INSERT INTO `t_role_menu` VALUES (2804, 1, 4);
INSERT INTO `t_role_menu` VALUES (2805, 1, 214);
INSERT INTO `t_role_menu` VALUES (2806, 1, 209);
INSERT INTO `t_role_menu` VALUES (2807, 1, 17);
INSERT INTO `t_role_menu` VALUES (2808, 1, 18);
INSERT INTO `t_role_menu` VALUES (2809, 1, 205);
INSERT INTO `t_role_menu` VALUES (2810, 1, 206);
INSERT INTO `t_role_menu` VALUES (2811, 1, 208);
INSERT INTO `t_role_menu` VALUES (2812, 1, 210);
INSERT INTO `t_role_menu` VALUES (2813, 1, 19);
INSERT INTO `t_role_menu` VALUES (2814, 1, 20);
INSERT INTO `t_role_menu` VALUES (2815, 1, 225);
INSERT INTO `t_role_menu` VALUES (2816, 1, 220);
INSERT INTO `t_role_menu` VALUES (2817, 1, 5);
INSERT INTO `t_role_menu` VALUES (2852, 14, 1);
INSERT INTO `t_role_menu` VALUES (2853, 14, 2);
INSERT INTO `t_role_menu` VALUES (2854, 14, 6);
INSERT INTO `t_role_menu` VALUES (2855, 14, 7);
INSERT INTO `t_role_menu` VALUES (2856, 14, 8);
INSERT INTO `t_role_menu` VALUES (2857, 14, 9);
INSERT INTO `t_role_menu` VALUES (2858, 14, 10);
INSERT INTO `t_role_menu` VALUES (2859, 14, 3);
INSERT INTO `t_role_menu` VALUES (2860, 14, 11);
INSERT INTO `t_role_menu` VALUES (2861, 14, 221);
INSERT INTO `t_role_menu` VALUES (2862, 14, 222);
INSERT INTO `t_role_menu` VALUES (2863, 14, 223);
INSERT INTO `t_role_menu` VALUES (2864, 14, 224);
INSERT INTO `t_role_menu` VALUES (2865, 14, 202);
INSERT INTO `t_role_menu` VALUES (2866, 14, 13);
INSERT INTO `t_role_menu` VALUES (2867, 14, 201);
INSERT INTO `t_role_menu` VALUES (2868, 14, 213);
INSERT INTO `t_role_menu` VALUES (2869, 14, 14);
INSERT INTO `t_role_menu` VALUES (2870, 14, 15);
INSERT INTO `t_role_menu` VALUES (2871, 14, 16);
INSERT INTO `t_role_menu` VALUES (2872, 14, 4);
INSERT INTO `t_role_menu` VALUES (2873, 14, 214);
INSERT INTO `t_role_menu` VALUES (2874, 14, 209);
INSERT INTO `t_role_menu` VALUES (2875, 14, 17);
INSERT INTO `t_role_menu` VALUES (2876, 14, 18);
INSERT INTO `t_role_menu` VALUES (2877, 14, 205);
INSERT INTO `t_role_menu` VALUES (2878, 14, 206);
INSERT INTO `t_role_menu` VALUES (2879, 14, 208);
INSERT INTO `t_role_menu` VALUES (2880, 14, 210);
INSERT INTO `t_role_menu` VALUES (2881, 14, 19);
INSERT INTO `t_role_menu` VALUES (2882, 14, 20);
INSERT INTO `t_role_menu` VALUES (2883, 14, 225);
INSERT INTO `t_role_menu` VALUES (2884, 14, 220);
INSERT INTO `t_role_menu` VALUES (2885, 14, 5);

-- ----------------------------
-- Table structure for t_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_role_resource`;
CREATE TABLE `t_role_resource`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `resource_id` int NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5240 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role_resource
-- ----------------------------
INSERT INTO `t_role_resource` VALUES (5406, 2, 1146);
INSERT INTO `t_role_resource` VALUES (5407, 2, 1167);
INSERT INTO `t_role_resource` VALUES (5408, 1, 1050);
INSERT INTO `t_role_resource` VALUES (5409, 1, 1070);
INSERT INTO `t_role_resource` VALUES (5410, 1, 1071);
INSERT INTO `t_role_resource` VALUES (5411, 1, 1088);
INSERT INTO `t_role_resource` VALUES (5412, 1, 1148);
INSERT INTO `t_role_resource` VALUES (5413, 1, 1149);
INSERT INTO `t_role_resource` VALUES (5414, 1, 1051);
INSERT INTO `t_role_resource` VALUES (5415, 1, 1081);
INSERT INTO `t_role_resource` VALUES (5416, 1, 1082);
INSERT INTO `t_role_resource` VALUES (5417, 1, 1083);
INSERT INTO `t_role_resource` VALUES (5418, 1, 1084);
INSERT INTO `t_role_resource` VALUES (5419, 1, 1052);
INSERT INTO `t_role_resource` VALUES (5420, 1, 1101);
INSERT INTO `t_role_resource` VALUES (5421, 1, 1102);
INSERT INTO `t_role_resource` VALUES (5422, 1, 1103);
INSERT INTO `t_role_resource` VALUES (5423, 1, 1053);
INSERT INTO `t_role_resource` VALUES (5424, 1, 1089);
INSERT INTO `t_role_resource` VALUES (5425, 1, 1090);
INSERT INTO `t_role_resource` VALUES (5426, 1, 1091);
INSERT INTO `t_role_resource` VALUES (5427, 1, 1092);
INSERT INTO `t_role_resource` VALUES (5428, 1, 1054);
INSERT INTO `t_role_resource` VALUES (5429, 1, 1093);
INSERT INTO `t_role_resource` VALUES (5430, 1, 1094);
INSERT INTO `t_role_resource` VALUES (5431, 1, 1095);
INSERT INTO `t_role_resource` VALUES (5432, 1, 1096);
INSERT INTO `t_role_resource` VALUES (5433, 1, 1097);
INSERT INTO `t_role_resource` VALUES (5434, 1, 1098);
INSERT INTO `t_role_resource` VALUES (5435, 1, 1099);
INSERT INTO `t_role_resource` VALUES (5436, 1, 1100);
INSERT INTO `t_role_resource` VALUES (5437, 1, 1055);
INSERT INTO `t_role_resource` VALUES (5438, 1, 1158);
INSERT INTO `t_role_resource` VALUES (5439, 1, 1159);
INSERT INTO `t_role_resource` VALUES (5440, 1, 1160);
INSERT INTO `t_role_resource` VALUES (5441, 1, 1161);
INSERT INTO `t_role_resource` VALUES (5442, 1, 1162);
INSERT INTO `t_role_resource` VALUES (5443, 1, 1163);
INSERT INTO `t_role_resource` VALUES (5444, 1, 1164);
INSERT INTO `t_role_resource` VALUES (5445, 1, 1056);
INSERT INTO `t_role_resource` VALUES (5446, 1, 1108);
INSERT INTO `t_role_resource` VALUES (5447, 1, 1109);
INSERT INTO `t_role_resource` VALUES (5448, 1, 1057);
INSERT INTO `t_role_resource` VALUES (5449, 1, 1072);
INSERT INTO `t_role_resource` VALUES (5450, 1, 1073);
INSERT INTO `t_role_resource` VALUES (5451, 1, 1074);
INSERT INTO `t_role_resource` VALUES (5452, 1, 1075);
INSERT INTO `t_role_resource` VALUES (5453, 1, 1076);
INSERT INTO `t_role_resource` VALUES (5454, 1, 1077);
INSERT INTO `t_role_resource` VALUES (5455, 1, 1078);
INSERT INTO `t_role_resource` VALUES (5456, 1, 1079);
INSERT INTO `t_role_resource` VALUES (5457, 1, 1080);
INSERT INTO `t_role_resource` VALUES (5458, 1, 1058);
INSERT INTO `t_role_resource` VALUES (5459, 1, 1130);
INSERT INTO `t_role_resource` VALUES (5460, 1, 1131);
INSERT INTO `t_role_resource` VALUES (5461, 1, 1132);
INSERT INTO `t_role_resource` VALUES (5462, 1, 1133);
INSERT INTO `t_role_resource` VALUES (5463, 1, 1059);
INSERT INTO `t_role_resource` VALUES (5464, 1, 1110);
INSERT INTO `t_role_resource` VALUES (5465, 1, 1111);
INSERT INTO `t_role_resource` VALUES (5466, 1, 1112);
INSERT INTO `t_role_resource` VALUES (5467, 1, 1113);
INSERT INTO `t_role_resource` VALUES (5468, 1, 1114);
INSERT INTO `t_role_resource` VALUES (5469, 1, 1121);
INSERT INTO `t_role_resource` VALUES (5470, 1, 1060);
INSERT INTO `t_role_resource` VALUES (5471, 1, 1142);
INSERT INTO `t_role_resource` VALUES (5472, 1, 1143);
INSERT INTO `t_role_resource` VALUES (5473, 1, 1146);
INSERT INTO `t_role_resource` VALUES (5474, 1, 1147);
INSERT INTO `t_role_resource` VALUES (5475, 1, 1061);
INSERT INTO `t_role_resource` VALUES (5476, 1, 1140);
INSERT INTO `t_role_resource` VALUES (5477, 1, 1141);
INSERT INTO `t_role_resource` VALUES (5478, 1, 1144);
INSERT INTO `t_role_resource` VALUES (5479, 1, 1062);
INSERT INTO `t_role_resource` VALUES (5480, 1, 1115);
INSERT INTO `t_role_resource` VALUES (5481, 1, 1116);
INSERT INTO `t_role_resource` VALUES (5482, 1, 1117);
INSERT INTO `t_role_resource` VALUES (5483, 1, 1118);
INSERT INTO `t_role_resource` VALUES (5484, 1, 1119);
INSERT INTO `t_role_resource` VALUES (5485, 1, 1120);
INSERT INTO `t_role_resource` VALUES (5486, 1, 1063);
INSERT INTO `t_role_resource` VALUES (5487, 1, 1104);
INSERT INTO `t_role_resource` VALUES (5488, 1, 1105);
INSERT INTO `t_role_resource` VALUES (5489, 1, 1106);
INSERT INTO `t_role_resource` VALUES (5490, 1, 1107);
INSERT INTO `t_role_resource` VALUES (5491, 1, 1126);
INSERT INTO `t_role_resource` VALUES (5492, 1, 1139);
INSERT INTO `t_role_resource` VALUES (5493, 1, 1064);
INSERT INTO `t_role_resource` VALUES (5494, 1, 1125);
INSERT INTO `t_role_resource` VALUES (5495, 1, 1128);
INSERT INTO `t_role_resource` VALUES (5496, 1, 1129);
INSERT INTO `t_role_resource` VALUES (5497, 1, 1145);
INSERT INTO `t_role_resource` VALUES (5498, 1, 1065);
INSERT INTO `t_role_resource` VALUES (5499, 1, 1085);
INSERT INTO `t_role_resource` VALUES (5500, 1, 1086);
INSERT INTO `t_role_resource` VALUES (5501, 1, 1087);
INSERT INTO `t_role_resource` VALUES (5502, 1, 1167);
INSERT INTO `t_role_resource` VALUES (5503, 1, 1066);
INSERT INTO `t_role_resource` VALUES (5504, 1, 1134);
INSERT INTO `t_role_resource` VALUES (5505, 1, 1135);
INSERT INTO `t_role_resource` VALUES (5506, 1, 1136);
INSERT INTO `t_role_resource` VALUES (5507, 1, 1137);
INSERT INTO `t_role_resource` VALUES (5508, 1, 1067);
INSERT INTO `t_role_resource` VALUES (5509, 1, 1122);
INSERT INTO `t_role_resource` VALUES (5510, 1, 1123);
INSERT INTO `t_role_resource` VALUES (5511, 1, 1124);
INSERT INTO `t_role_resource` VALUES (5512, 1, 1127);
INSERT INTO `t_role_resource` VALUES (5513, 1, 1186);
INSERT INTO `t_role_resource` VALUES (5514, 1, 1187);
INSERT INTO `t_role_resource` VALUES (5515, 1, 1188);
INSERT INTO `t_role_resource` VALUES (5516, 14, 1070);
INSERT INTO `t_role_resource` VALUES (5517, 14, 1148);
INSERT INTO `t_role_resource` VALUES (5518, 14, 1081);
INSERT INTO `t_role_resource` VALUES (5519, 14, 1084);
INSERT INTO `t_role_resource` VALUES (5520, 14, 1101);
INSERT INTO `t_role_resource` VALUES (5521, 14, 1089);
INSERT INTO `t_role_resource` VALUES (5522, 14, 1092);
INSERT INTO `t_role_resource` VALUES (5523, 14, 1093);
INSERT INTO `t_role_resource` VALUES (5524, 14, 1097);
INSERT INTO `t_role_resource` VALUES (5525, 14, 1100);
INSERT INTO `t_role_resource` VALUES (5526, 14, 1108);
INSERT INTO `t_role_resource` VALUES (5527, 14, 1072);
INSERT INTO `t_role_resource` VALUES (5528, 14, 1080);
INSERT INTO `t_role_resource` VALUES (5529, 14, 1130);
INSERT INTO `t_role_resource` VALUES (5530, 14, 1133);
INSERT INTO `t_role_resource` VALUES (5531, 14, 1110);
INSERT INTO `t_role_resource` VALUES (5532, 14, 1143);
INSERT INTO `t_role_resource` VALUES (5533, 14, 1140);
INSERT INTO `t_role_resource` VALUES (5534, 14, 1141);
INSERT INTO `t_role_resource` VALUES (5535, 14, 1115);
INSERT INTO `t_role_resource` VALUES (5536, 14, 1118);
INSERT INTO `t_role_resource` VALUES (5537, 14, 1104);
INSERT INTO `t_role_resource` VALUES (5538, 14, 1126);
INSERT INTO `t_role_resource` VALUES (5539, 14, 1139);
INSERT INTO `t_role_resource` VALUES (5540, 14, 1128);
INSERT INTO `t_role_resource` VALUES (5541, 14, 1145);
INSERT INTO `t_role_resource` VALUES (5542, 14, 1085);
INSERT INTO `t_role_resource` VALUES (5543, 14, 1134);
INSERT INTO `t_role_resource` VALUES (5544, 14, 1122);
INSERT INTO `t_role_resource` VALUES (5545, 14, 1127);
INSERT INTO `t_role_resource` VALUES (5546, 14, 1187);

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_talk
-- ----------------------------
DROP TABLE IF EXISTS `t_talk`;
CREATE TABLE `t_talk`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '说说id',
  `user_id` int NOT NULL COMMENT '用户id',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '说说内容',
  `images` varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1.公开 2.私密',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_unique_view
-- ----------------------------
DROP TABLE IF EXISTS `t_unique_view`;
CREATE TABLE `t_unique_view`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `views_count` int NOT NULL COMMENT '访问量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1539 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_user_auth`;
CREATE TABLE `t_user_auth`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_info_id` int NOT NULL COMMENT '用户信息id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `login_type` tinyint(1) NOT NULL COMMENT '登录类型',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户登录ip',
  `ip_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '上次登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1014 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_auth
-- ----------------------------
INSERT INTO `t_user_auth` VALUES (1, 1, 'admin@163.com', '$2a$10$/Z90STxVyGOIfNhTfvzbEuJ9t1yHjrkN6pBMRAqd5g5SdNIrdt5Da', 1, '182.118.236.146', '中国|河南省|洛阳市|联通', '2022-08-19 21:43:46', '2022-08-24 20:33:46', '2022-08-24 20:33:46');

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱号',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户简介',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人网站',
  `is_subscribe` tinyint(1) NULL DEFAULT NULL COMMENT '是否订阅',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1024 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_info
-- ----------------------------
INSERT INTO `t_user_info` VALUES (1, 'admin@163.com', '演示账号', 'https://static.linhaojun.top/avatar/2af2e2db20740e712f0a011a6f8c9af5.jpg', '演示账号的用户简介', 'https://www.linhaojun.top', 0, 0, '2022-08-19 21:42:04', '2022-08-24 17:11:35');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1032 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for t_website_config
-- ----------------------------
DROP TABLE IF EXISTS `t_website_config`;
CREATE TABLE `t_website_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `config` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置信息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_website_config
-- ----------------------------
INSERT INTO `t_website_config` VALUES (1, '{\"alipayQRCode\":\"https://static.linhaojun.top/config/da4c6d8c13f66a8dd6716ddb48d73299.jpg\",\"author\":\"花未眠\",\"authorAvatar\":\"https://static.linhaojun.top/config/6e4f47f4bb66d2d30722d20e789b220e.jpg\",\"authorIntro\":\"一个疯狂的coder\",\"beianNumber\":\"苏ICP备2022012376号\",\"csdn\":\"\",\"englishName\":\"huaweimian\",\"gitee\":\"https://gitee.com/linhaojun\",\"github\":\"https://github.com/linhaojun857\",\"isCommentReview\":0,\"isEmailNotice\":1,\"isReward\":1,\"juejin\":\"\",\"logo\":\"https://static.linhaojun.top/config/cc36e9fa5aeb214e41495c1e2268f2db.png\",\"multiLanguage\":1,\"name\":\"花未眠\",\"notice\":\"代码已经开源，如果你感觉还行就给一个star吧。。。\",\"qq\":\"\",\"qqLogin\":1,\"stackoverflow\":\"\",\"touristAvatar\":\"https://static.linhaojun.top/config/2af2e2db20740e712f0a011a6f8c9af5.jpg\",\"twitter\":\"\",\"userAvatar\":\"https://static.linhaojun.top/config/0af1901da1e64dfb99bb61db21e716c4.jpeg\",\"weChat\":\"\",\"websiteCreateTime\":\"2022-08-19\",\"weiXinQRCode\":\"https://static.linhaojun.top/config/ed47edae605f74306f751c6fba9f14bd.png\",\"weibo\":\"\",\"zhihu\":\"\"}', '2022-07-24 12:05:33', '2022-08-20 12:48:28');

-- ----------------------------
-- Table structure for t_exception_log
-- ----------------------------
DROP TABLE IF EXISTS `t_exception_log`;
CREATE TABLE `t_exception_log`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `opt_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求接口',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `request_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `opt_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作描述',
  `exception_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常信息',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `create_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
