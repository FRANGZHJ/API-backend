# 数据库初始化

-- 创建库
create database if not exists open_api_db;

-- 切换库
use open_api_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                               null comment '用户名称',
    userAvator   varchar(1024)                              null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    accessKey    varchar(512)                           not null comment 'ak',
    secretKey    varchar(512)                           not null comment 'sk',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;

create table if not exists interface_info
(
    id          bigint auto_increment                   primary key comment 'id',
    name        varchar(256)                            not null    comment '名称',
    description varchar(512)                            null        comment '描述',
    url         varchar(512)                            not null    comment '接口地址',
    requestHeader    text                                  null        comment '请求头',
    responseHeader   text                                  null        comment '响应体',
    status           int            default 0           not null    comment '接口状态（0-关闭，1-开启)',
    method           varchar(512)                       not null    comment '请求类型',
    userId           long                      not null    comment '创建人',
    createTime      datetime default CURRENT_TIMESTAMP  not null    comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP  not null on update CURRENT_TIMESTAMP  comment '修改时间',
    isDelete        tinyint         default 0           not null    comment '逻辑删除'
)comment '接口信息' collate = utf8mb4_unicode_ci;


create table if not exists user_interface_info
(
    id          bigint auto_increment                   primary key comment 'id',
    userId      bigint                           		not null    comment '用户id',
    interfaceId bigint                           		not null    comment '接口id',
    url         varchar(512)                            not null    comment '接口地址',
    status      int                 default 0           not null    comment '接口状态（0-关闭，1-开启)',
    totalNum	int					default 0			not null    comment '总调用次数',
    leftNum 	int				    default 0			not null	comment '剩余调用次数',
    createTime      datetime default CURRENT_TIMESTAMP  not null    comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP  not null on update CURRENT_TIMESTAMP  comment '修改时间',
    isDelete        tinyint         default 0           not null    comment '逻辑删除'
)comment '用户接口关系表' collate = utf8mb4_unicode_ci;