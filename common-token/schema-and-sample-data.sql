/* standard Schema for PostgreSQL to use with JdbcTokenStore (Spring Security OAuth2) */
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
);

create table oauth_code (
  code VARCHAR(256), authentication bytea
);

create table oauth_approvals (
  userId VARCHAR(256),
  clientId VARCHAR(256),
  scope VARCHAR(256),
  status VARCHAR(10),
  expiresAt TIMESTAMP,
  lastModifiedAt TIMESTAMP
);

/* 本例子增加的用户、组及权限部分 */
create table users(
    id serial primary key,
    username varchar(50) not null,
    name varchar(50) null,
    password varchar(256) null,
    head_image varchar(50) null,
    email varchar(100) null,
    phone varchar(20) null,
    position varchar(20) null,
    department varchar(20) null,
    enabled boolean default true,
    expireDate date null,
    create_date timestamp not null default now(),
    last_modify_at timestamp not null default now()
);

create table groups (
    id serial primary key,
    group_name varchar(50) not null
);

create table group_authorities (
    group_id int not null,
    authority varchar(50) not null,
    constraint fk_group_authorities_group foreign key(group_id) references groups(id)
);

create table group_members (
    id serial primary key,
    user_id int not null,
    group_id int not null,
    constraint fk_group_members_group foreign key(group_id) references groups(id),
    constraint fk_group_members_user foreign key(user_id) references users(id)
);

/* ------- sample date ------*/
/** 应用， 自动授权，授权后重定向到http://127.0.0.1?key=rs1key  rs1无密码 rs2密码是secret   **/
insert into oauth_client_details 
        (client_id, resource_ids, client_secret, scope, 
            authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, 
            refresh_token_validity, additional_information, autoapprove) 
values(
    'rs1', 'oauth2-resource', '', 'read,trust',
    'password,authorization_code,refresh_token', 'http://127.0.0.1', '', null,
    null, '{}', 'read,trust'
);
insert into oauth_client_details 
        (client_id, resource_ids, client_secret, scope, 
            authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, 
            refresh_token_validity, additional_information, autoapprove) 
values(
    'rs2', 'oauth2-resource', '$2a$10$Y2xtu4vSndEX.TZBfMgbau1G7jO6Nlprdpot1./mDQDB3OBeUJI6q', 'read,trust',
    'authorization_code,refresh_token', 'http://127.0.0.1:8080', '', null,
    null, '{}', 'read,trust'
);
/** 用户 用户名 admin, 密码admpwd */
insert into users(username, name, password) values('admin', 'System Administrator', '$2a$10$H2z/oHZgviqC8srzsCZq/ObzTNRTfygC4N0NEZlm5trYPFrvdNHeS');
insert into groups(group_name) values('Admin');
insert into group_authorities (group_id, authority) select id, 'all' from groups where group_name='Admin';
insert into group_authorities (group_id, authority) select id, 'everything' from groups where group_name='Admin';
insert into group_members(user_id, group_id) select g.id, u.id from groups g, users u where u.username='admin' and g.group_name='Admin';
    