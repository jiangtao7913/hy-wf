-- app日志记录表
DROP TABLE IF EXISTS `app_log`;
CREATE TABLE `app_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `client_ip` varchar(255) NOT NULL COMMENT '客户端ip',
  `uri` varchar(255) NOT NULL COMMENT '请求路径',
  `require_method` varchar(255) NOT NULL COMMENT '请求方式',
  `param_data` longtext NOT NULL COMMENT '客户端参数',
  `require_time` datetime(3) NOT NULL COMMENT '请求时间',
  `end_time` datetime(3) NOT NULL COMMENT '返回时间',
  `response_time` bigint(20) NOT NULL COMMENT '响应时间',
  `response` longtext NOT NULL COMMENT '响应消息',
  `stack_message` longtext NOT NULL COMMENT '堆栈信息',
  `type` tinyint(1) NOT NULL COMMENT '堆栈信息',
  `uid` varchar(50) NOT NULL COMMENT 'uid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8 COMMENT='app日志表';
ALTER TABLE app_log ADD COLUMN  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间';
ALTER TABLE app_log ADD COLUMN  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间';
ALTER TABLE app_log ADD COLUMN  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态';




-- app配置表
DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `name` varchar(255) NOT NULL COMMENT 'key',
  `value` varchar(255) NOT NULL COMMENT 'value',
  `module` varchar(255) NOT NULL COMMENT '模块',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `app_type` varchar(255) NOT NULL COMMENT '应用名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='app系统配置表';

-- app安装记录表
DROP TABLE IF EXISTS `t_install_log`;
CREATE TABLE `t_install_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` int(11) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `hardware_key` varchar(255) NOT NULL COMMENT '硬件Key',
  `ip` varchar(255) NOT NULL COMMENT 'IP地址',
  `client_type` varchar(255) NOT NULL COMMENT '机型',
  `client_version` varchar(255) NOT NULL COMMENT 'apk版本',
  `memo` varchar(255) NOT NULL COMMENT '备注',
  `source` varchar(255) NOT NULL COMMENT '安装来源',
--   `free_time` int(10) NOT NULL DEFAULT '3' COMMENT '剩余免费次数',
--   `free_count` int(10) NOT NULL DEFAULT '5' COMMENT '检测限制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='安装记录';
alter table t_install_log add column app_type VARCHAR(255) default '' NOT NULL comment '包名';

--app广告表
DROP TABLE IF EXISTS `t_ad`;
CREATE TABLE `t_ad` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` int(11) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `orders` int(11) NOT NULL COMMENT '排序',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `type` int(11) NOT NULL COMMENT '类型',
  `content` longtext NOT NULL COMMENT '内容',
  `path` varchar(255) NOT NULL COMMENT '路径',
  `begin_date` datetime NOT NULL COMMENT '起始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `url` varchar(255) NOT NULL COMMENT '链接地址',
  `POSITION` char(30) NOT NULL COMMENT '位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='广告';
alter table t_ad add column app_type VARCHAR(255) default '' NOT NULL comment'APP类型';


--app 功能表
DROP TABLE IF EXISTS `t_function`;
CREATE TABLE `t_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` int(11) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `orders` int(11) NOT NULL COMMENT '排序',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `type` int(11) NOT NULL COMMENT '类型(0:一般功能;2:扩展功能)',
  `position` char(30) NOT NULL COMMENT '位置',
  `url`varchar(255) NOT NULL COMMENT 'url(用户扩展)',
  `price` decimal(8,2) NOT NULL COMMENT '价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='app功能列表';
alter table t_function add column meno VARCHAR(255) NOT NULL comment'描述';
alter table t_function add column icon VARCHAR(255) default '' NOT NULL comment'图标';
ALTER TABLE t_function ADD COLUMN status TINYINT(1) DEFAULT '0' NOT NULL COMMENT'是否启用';
alter table t_function add column app_type VARCHAR(255) default '' NOT NULL comment'APP类型';
alter table t_function add column target VARCHAR(255) default '' NOT NULL comment'跳转页面';


--app 用户功能表
DROP TABLE IF EXISTS `t_user_function`;
CREATE TABLE `t_user_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` int(11) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `function_id` bigint(20) NOT NULL COMMENT '功能id',
  `expire_time` bigint(20) NOT NULL COMMENT '过期时间',
  `type` int(11) NOT NULL COMMENT '功能类型(0:一般功能;1:vip功能;2:扩展功能)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='app功能列表';
alter table t_user_function add column meno VARCHAR(255) default '' NOT NULL comment'功能名称';

--app 用户表
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` BIGINT(20) COMMENT '主键ID' NOT NULL AUTO_INCREMENT,
  `create_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间' NOT NULL,
  `modify_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间' NOT NULL,
  `data_status` INT(11) DEFAULT 1 COMMENT '数据状态' NOT NULL,
  `uid` VARCHAR(50) COMMENT '用户uid' NOT NULL,
  `recharge_total` DECIMAL(8,2) COMMENT '充值总金额' NOT NULL,
  `source` VARCHAR(50) COMMENT '用户渠道来源' NOT NULL,
  `online` TINYINT(1) DEFAULT 0 COMMENT '是否在线' NOT NULL,
  `name` VARCHAR(255) COMMENT '名称' NOT NULL,
  `icon` VARCHAR(255) COMMENT '图标' NOT NULL,
  `hardware_key` VARCHAR(255) COMMENT '用户登录设备号' NOT NULL,
  `sign` VARCHAR(255) COMMENT '用户关键字' NOT NULL,
  `account_type` VARCHAR(255) COMMENT '账号类型(0：微信｜1：QQ | 2：手机)' NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='app用户表';
alter table t_user add column password VARCHAR(255) NOT NULL comment'用户密码';
alter table t_user add column sex VARCHAR(255) NOT NULL comment'性别';
alter table t_user add column balance DECIMAL(8,2) default 0 NOT NULL comment'余额';
alter table t_user add column app_type VARCHAR(255) default '' NOT NULL comment'APP类型';

--序列表
DROP TABLE IF EXISTS `t_sn`;
CREATE TABLE `t_sn` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `last_value` INT(11) NOT NULL DEFAULT '1' COMMENT '末值',
  `type` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '类型0:UID 1:Order 2:Payment 3:Refunds 4:GameQueue',
  PRIMARY KEY (`id`),
  KEY `IDX_t_sn_type` (`type`)
) ENGINE=INNODB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='序列号';

--验证码表
DROP TABLE IF EXISTS `t_verify_code`;
CREATE TABLE `t_verify_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `mobile` varchar(255) NOT NULL COMMENT '手机',
  `code` varchar(255) DEFAULT NULL COMMENT '验证码',
  `request` varchar(255) DEFAULT NULL COMMENT '请求信息',
  `expiry_time` bigint(20) NOT NULL COMMENT '有效期',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `IDX_t_verify_code_mobile` (`mobile`(191))
) ENGINE=InnoDB AUTO_INCREMENT=1980 DEFAULT CHARSET=utf8mb4 COMMENT='验证码';

--用户vip表
DROP TABLE IF EXISTS `t_vip`;
CREATE TABLE `t_vip` (
  `id` BIGINT(20) COMMENT '主键ID' NOT NULL AUTO_INCREMENT,
  `create_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间' NOT NULL,
  `modify_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间' NOT NULL,
  `data_status` INT(11) DEFAULT 1 COMMENT '数据状态' NOT NULL,
  `type` TINYINT(1) COMMENT 'vip类型' NOT NULL,
  `price` DECIMAL(8,2) COMMENT 'vip价格' NOT NULL,
  `meno` VARCHAR(255) COMMENT '描述' NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='vip配置表';
ALTER TABLE `t_vip` ADD COLUMN title VARCHAR(255) DEFAULT '' NOT NULL COMMENT'标题';
alter table t_vip add column app_type VARCHAR(255) default '' NOT NULL comment'APP类型';

-- 插件配置表
DROP TABLE IF EXISTS `t_plugin_config`;
CREATE TABLE `t_plugin_config` (
  `id` BIGINT(20) COMMENT '主键ID' NOT NULL AUTO_INCREMENT,
  `create_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间' NOT NULL,
  `modify_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间' NOT NULL,
  `data_status` TINYINT(1) DEFAULT 1 COMMENT '数据状态' NOT NULL,
  `orders` INT(11)  COMMENT '排序' NOT NULL,
  `is_enabled` TINYINT(1) COMMENT '是否启用' NOT NULL,
  `plugin_name` VARCHAR(255) COMMENT '插件名称' NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='插件配置表';
alter table t_plugin_config add column app_type VARCHAR(255) default '' NOT NULL comment'APP类型';


--插件属性表
DROP TABLE IF EXISTS `t_plugin_config_attribute`;
CREATE TABLE `t_plugin_config_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `plugin_config_id` bigint(20) NOT NULL COMMENT '插件ID',
  `name` varchar(255) NOT NULL COMMENT '属性名称',
  `value` varchar(5000) NOT NULL COMMENT '属性值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='插件配置属性';

-- 订单表
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` BIGINT(20) COMMENT '主键ID' NOT NULL AUTO_INCREMENT,
  `create_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间' NOT NULL,
  `modify_date` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间' NOT NULL,
  `data_status` INT(11) DEFAULT 1 COMMENT '数据状态' NOT NULL,
  `order_price` DECIMAL(8,2) COMMENT '订单价格' NOT NULL,
  `order_status` TINYINT(1) COMMENT '订单状态(0：已确认｜1：已完成 | 2：已取消)' NOT NULL,
  `pay_status` TINYINT(1) COMMENT '支付状态(0：未支付｜1：已支付 | 2：部分退款| 3：已退款)' NOT NULL,
  `type` TINYINT(1) COMMENT '订单类型(0：购买单个功能 |1：购买vip)' NOT NULL,
  `order_number` VARCHAR(255) COMMENT '订单编号' NOT NULL,
  `business_id` BIGINT(20) COMMENT '业务id' NOT NULL,
  `summary` VARCHAR(255) COMMENT '摘要' NOT NULL,
  `user_id` BIGINT(20) COMMENT '用户id' NOT NULL,
  `pay_type` TINYINT(1) COMMENT '支付方式' NOT NULL,
  `payer` VARCHAR(255) COMMENT '付款人' NOT NULL,
  `payment_date` DATETIME(3) COMMENT '付款日期' NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='订单表';
alter table t_order add column pay_number VARCHAR(255) default '' NOT NULL comment'收款号';
ALTER TABLE t_order ADD COLUMN payment_name VARCHAR(255) DEFAULT '' NOT NULL COMMENT'支付方法';
ALTER TABLE t_order ADD COLUMN plugin_name VARCHAR(255) DEFAULT '' NOT NULL COMMENT'插件名称';

-- 功能规则配置表
DROP TABLE IF EXISTS `t_function_rule`;
CREATE TABLE `t_function_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `function_id` bigint(20) NOT NULL COMMENT '功能id',
  `type` TINYINT(1) NOT NULL COMMENT '规则类型',
  `meno` varchar(255) NOT NULL COMMENT '功能描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='功能规则表';

-- 机器记录功能使用记录表
DROP TABLE IF EXISTS `t_function_use`;
CREATE TABLE `t_function_use` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `hardware_key` varchar(255) NOT NULL COMMENT '机器唯一识别码',
  `function_id` bigint(20) NOT NULL COMMENT '功能id',
  `use_count` INT(11) NOT NULL COMMENT '使用次数',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type` tinyint(1) NOT NULL  COMMENT '类型(0:免费使用|付费使用)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='机器记录功能使用记录表';

-- 邀请表
DROP TABLE IF EXISTS `t_invitation`;
CREATE TABLE `t_invitation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `master_id`   bigint(20) NOT NULL COMMENT '师傅id',
  `master_uid`   varchar(50) NOT NULL COMMENT '师傅uid',
  `income_total` DECIMAL(8,2) COMMENT '邀请总收益' NOT NULL,
  `stair_income` DECIMAL(8,2) COMMENT '一级收益' NOT NULL,
  `second_income` DECIMAL(8,2) COMMENT '二级收益' NOT NULL,
  `count` int(11) COMMENT '邀请总人数' NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='邀请表';

-- 邀请明细
DROP TABLE IF EXISTS `t_invitation_detail`;
CREATE TABLE `t_invitation_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `invitation_id`   bigint(20) NOT NULL COMMENT '邀请id',
  `pretice_id` bigint(20) NOT NULL COMMENT '徒弟id',
  `pretice_uid` varchar (50) NOT NULL COMMENT '徒弟uid',
  `name` varchar(255) NOT NULL COMMENT '徒弟名称',
  `meno` varchar(255) NOT NULL COMMENT '消费描述',
  `income_total` DECIMAL(8,2) COMMENT '徒弟带来的收益' NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='邀请明细';
ALTER TABLE t_invitation_detail ADD COLUMN type tinyint(1) DEFAULT '0' NOT NULL  COMMENT '类型(0:一级徒弟| 1:二级徒弟)';


-- 支出记录表
DROP TABLE IF EXISTS `t_money_record`;
CREATE TABLE `t_money_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `account` varchar(255) NOT NULL COMMENT '提现账号',
  `name` varchar(255) NOT NULL COMMENT '提现人姓名',
  `amount` DECIMAL(8,2) NOT NULL COMMENT '提现金额',
  `status` tinyint(1) NOT NULL COMMENT '状态(0:审核中;1成功;2:取消)',
  `user_id`   bigint(20) NOT NULL COMMENT '用户id',
  `meno`   varchar(255) NOT NULL COMMENT '描述',
  `type` tinyint(1) NOT NULL  COMMENT '类型(0:消费|1:提现|2:收入)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='支出记录表';
ALTER TABLE t_money_record ADD COLUMN `number` varchar(255) DEFAULT '' NOT NULL  COMMENT '编号';
ALTER TABLE t_money_record ADD COLUMN `reason` varchar(255) DEFAULT '' NOT NULL  COMMENT '原因';
ALTER TABLE t_money_record ADD COLUMN `other_user_name` varchar(255) DEFAULT '' NOT NULL  COMMENT '其他用户名称';
ALTER TABLE t_money_record ADD COLUMN `other_user_Id` bigint(20) DEFAULT 0 NOT NULL  COMMENT '其他用户Id';

-- 收入统计表
DROP TABLE IF EXISTS `t_record`;
CREATE TABLE `t_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `date` varchar(50) NOT NULL COMMENT '日期',
  `total_income` DECIMAL(8,2) NOT NULL COMMENT '总收入',
  `wx_income` DECIMAL(8,2) NOT NULL COMMENT '微信收入',
  `zfb_income` DECIMAL(8,2) NOT NULL COMMENT '支付宝收入',
  `balance_income` DECIMAL(8,2) NOT NULL COMMENT '余额收入',
  `total_deduct` DECIMAL(8,2) COMMENT '总提成' NOT NULL,
  `total_install` int(11) COMMENT '总安装人数' NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='收入统计表';
ALTER TABLE t_record ADD COLUMN `day_active_count` int(11) DEFAULT 0 NOT NULL  COMMENT '日活跃用户数';

-- 渠道统计记录
DROP TABLE IF EXISTS `t_channel_record`;
CREATE TABLE `t_channel_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `create_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `modify_date` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `data_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '数据状态',
  `date` varchar(50) NOT NULL COMMENT '日期',
  `total_income` DECIMAL(8,2) NOT NULL COMMENT '渠道收入',
  `channel_name` varchar(50) NOT NULL COMMENT '渠道名称',
  `channel_install_total` int(10) NOT NULL COMMENT '渠道安装人数',
  `channel_deduct_total` DECIMAL(8,2) NOT NULL COMMENT '渠道总分成',
  `app_type` varchar(8,2) NOT NULL COMMENT '包名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='渠道统计记录';

--笔记
-- -- --  修改 app_log.param_data 字段为utf8mb4
-- -- --  修改 t_user.name 字段为utf8mb4
-- -- --  修改 t_invitation_detail.name 字段为utf8mb4
-- -- --  修改 t_order.summary 字段为utf8mb4
-- -- --  修改 t_money_record.meno 字段为utf8mb4
---- ALTER TABLE `t_order` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
---- ALTER TABLE `t_order` CHANGE summary summary VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;