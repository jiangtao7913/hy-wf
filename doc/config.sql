--vip 配置SQL
INSERT INTO `t_vip`(TYPE,price,meno,title,app_type)VALUES(0,48.80,'时限1月，所有功能无限使用','1月VIP会员','wechatqthand1');
INSERT INTO `t_vip`(TYPE,price,meno,title,app_type)VALUES(1,199.80,'时限1年，所有功能无限使用','1年VIP会员','wechatqthand1');

-- app配置信息
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('version.android','1.0','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('version.ios','1.0','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('package.download.url','http://download.hongyunapp.com','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('force.update.andorid','false','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('force.update.ios','false','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('force.feature.andorid','1、解决bug','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('force.feature.ios','1、IOS更新了。。。,2、更新了。。','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('package.url.andorid','/wsks_anzhi_1.0.apk','version','app版本信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('package.url.ios','/upload/package/ios.plist','version','app版本信息','wechatqthand1');

INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('site.url','http://api.wf.hongyunapp.com/wf-api','pay','支付回调地址','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('invitation_config','45,5','invitation','邀请配置信息','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('user.agreement','http://api.wf.hongyunapp.com/wf-api/user_agreement.html','agreement','用户协议','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('user.invation','http://api.wf.hongyunapp.com/html/download.html','share','分享邀请页面','wechatqthand1');
INSERT INTO `app_config`(NAME,VALUE,model,remark,app_type)VALUE('customer.service','http://admin.wf.hongyunapp.com/images/html/customer.jpg','customer','客服','wechatqthand1');

-- 广告
INSERT INTO `t_ad`(orders,title,TYPE,content,path,begin_date,end_date,url,POSITION,app_type)VALUE('1','邀请banner',0,'邀请banner','/images/ad/800f6f60-d3d5-4fc3-8e99-e776442fc729.png','1970-01-01 00:00:00',
'2030-12-01 10:40:10','.activity.MakeMoneyActivity','index','wechatqthand1');

-- 功能
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(1,'检测僵尸粉',0,'index','',49.80,'','/images/function/home_icon_jiangshifen.png',0,'wechatqthand1',1,'.activity.functionactivity.ChechNotFriendActivity');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(7,'快速转发',0,'index','',30.50,'','/images/function/home_icon_qunfa.png',0,'wechatqthand1',1,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(4,'群内加好友',0,'index','',30.50,'','/images/function/home_icon_jiahaoyou.png',0,'wechatqthand1',1,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(3,'附近的人',0,'index','',30.50,'','/images/function/home_icon_fujinderen.png',0,'wechatqthand1',1,'.activity.functionactivity.AddNearByPeopleActivity');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(5,'通讯录加人',0,'index','',30.50,'','/images/function/home_icon_tongxunlu.png',0,'wechatqthand1',1,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(6,'朋友圈评论',0,'index','',30.50,'','/images/function/home_icon_dianzan.png',0,'wechatqthand1',1,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(2,'清除未读',0,'index','',30.50,'','/images/function/home_icon_weidu.png',0,'wechatqthand1',1,'.activity.functionactivity.ClearUnReadActivity');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(8,'更多',2,'index','',30.50,'','/images/function/home_icon_gengduo.png',0,'wechatqthand1',1,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(1,'微信清理',1,'index','',0,'快速清理微信内存','/images/function/home_icon_wxql.png',1,'wechatqthand1',2,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(2,'一键快速加水印',1,'index','',0,'一键快速加水印','/images/function/home_icon_plqsy.png',1,'wechatqthand1',2,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(3,'相册恢复',1,'index','',0,'恢复删除照片','/images/function/home_icon_xchf.png',1,'wechatqthand1',2,'');
INSERT INTO `t_function`(orders,title,TYPE,POSITION,url,price,meno,icon,STATUS,app_type,location,target)VALUE(4,'快速挣钱',1,'index','',0,'邀请获得现金奖励','/images/function/home_icon_kszq.png',1,'wechatqthand1',2,'.activity.MakeMoneyActivity');

-- 功能规则

-- 插件
INSERT INTO `t_plugin_config`(orders,is_enabled,plugin_name,app_type)VALUES('1','1','ftpPlugin','wechatqthand1');
INSERT INTO `t_plugin_config`(orders,is_enabled,plugin_name,app_type)VALUES('2','1','alipayPlugin','wechatqthand1');
INSERT INTO `t_plugin_config`(orders,is_enabled,plugin_name,app_type)VALUES('3','1','weixinPayPlugin','wechatqthand1');
INSERT INTO `t_plugin_config`(orders,is_enabled,plugin_name,app_type)VALUES('4','1','balancePayPlugin','wechatqthand1');
-- 插件配置