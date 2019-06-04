--创建数据库
CREATE DATABASE seckill;
--使用数据库
USE seckill;
--创建秒杀库存表
CREATE TABLE seckill(
'seckill_id' bigint NOT NULL  AUTO_INCREMENT COMMENT '商品库存id',
'name' varchar(120) NOT NULL COMMENT '商品名称',
'number' int  not null comment '库存数量',
'start_time' timestamp not null comment '秒杀开始时间',
'end_time' timestamp not null comment '秒杀结束时间',
'create_time' timestamp not null DEFAULT current_timestamp comment '创建时间',
primary key (seckill_id),
key idx_start_time(start_time), --创建索引
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT DEFAULT CHARSET=utf-8 COMMENT='秒杀库存表';

--初始化数据
insert into
  seckill(name,number,start_time,end_time)
values
  ('1000元秒杀iphoneX',100,'2018-11-01 00:00:00','2018-11-02 00:00:00'),
  ('500元秒杀ipad3',200,'2018-11-01 00:00:00','2018-11-02 00:00:00'),
  ('300元秒杀小米',300,'2018-11-01 00:00:00','2018-11-02 00:00:00'),
  ('400元秒杀华为',400,'2018-11-01 00:00:00','2018-11-02 00:00:00');


--秒杀成功明细表
--用户登录认证相关的信息
create table success_killed(
'seckill_id' bigint NOT NULL  COMMENT '秒杀商品id',
'user_phone' bigint not null '用户手机号',
'state' tinyint not  null  default -1 comment '状态标识 -1：无效 0：成功 1：已付款  2：已发货 '
'create_time' timestamp not null comment '创建时间',
primary key (seckill_id,user_phone), --联合主键
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf-8 COMMENT='秒杀成功明细表';



