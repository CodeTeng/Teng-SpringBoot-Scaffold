create table user
(
    id             bigint auto_increment comment '主键id'
        primary key,
    username       varchar(256)                           not null comment '用户账号',
    user_password  varchar(512)                           not null comment '用户密码',
    user_role      varchar(100) default 'user'            not null comment '用户角色 user/admin/ban',
    user_phone     varchar(11)                            null comment '用户手机号',
    user_real_name varchar(60)                            null comment '用户真实姓名',
    user_gender    tinyint      default 0                 not null comment '性别：0-男性，1-女性',
    user_age       tinyint unsigned                       null comment '用户年龄',
    user_email     varchar(50)                            null comment '用户邮箱',
    union_id       varchar(256)                           null comment '微信开放平台id',
    mp_open_id     varchar(256)                           null comment '公众号openId',
    user_avatar    varchar(1024)                          null comment '用户头像',
    user_birthday  date                                   null comment '用户生日',
    user_profile   varchar(512)                           null comment '用户简介',
    status         tinyint      default 1                 not null comment '账户状态：0-禁用 1-正常',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    creater        bigint                                 null comment '创建者id',
    updater        bigint       default 0                 null comment '更新者id',
    is_delete      tinyint      default 0                 not null comment '是否删除 0-未删除 1-已删除',
    constraint user_pk
        unique (username)
)
    comment '用户表' collate = utf8mb4_unicode_ci;

create table sys_log
(
    id          bigint                             not null comment '主键id'
        primary key,
    user_id     bigint                             not null comment '用户id',
    username    varchar(256)                       null comment '用户名',
    value       varchar(256)                       null comment '用户操作描述',
    operation   varchar(50)                        null comment '用户操作:DELETE ADD GET UPDATE',
    cost_time   bigint                             null comment '响应时间,单位毫秒',
    url         varchar(256)                       null comment '请求路径',
    method_name varchar(256)                       null comment '请求方法（控制层方法全限定名）',
    params      varchar(1024)                      null comment '请求参数',
    ip          varchar(64)                        null comment 'IP地址',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    is_delete   tinyint  default 0                 not null comment '是否删除 0-未删除 1-已删除'
)
    comment '系统日志表' collate = utf8mb4_unicode_ci;