
create table users(
    id serial primary key,
    username varchar(50) not null,
    name varchar(50) null,
    password varchar(50) null,
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
/** 应用， 自动授权，授权后重定向到http://127.0.0.1?key=rs1    **/
insert into oauth_client_details 
        (client_id, resource_ids, client_secret, scope, 
            authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, 
            refresh_token_validity, additional_information, autoapprove) 
values(
    'rs1', 'oauth2-resource', 'read, trust', 'authorization_code,refresh_token',
    'http://127.0.0.1?key=rs1', 'ROLE_CLIENT', '', null,
    null, '{}', 'read,trust'
);
/** 用户 用户名 admin, 密码admpwd */
insert into users(username, name, password) values('admin', 'System Administrator', 'admpwd');
insert into groups(group_name) values('Admin');
insert into group_authorities (group_id, authority) select id, 'all' from groups where group_name='Admin';
insert into group_authorities (group_id, authority) select id, 'everything' from groups where group_name='Admin';
insert into group_members(user_id, group_id) select g.id, u.id from groups g, users u where u.username='admin' and g.group_name='Admin';
    