create table sys_dict_item
(
    id              int auto_increment primary key,
    dict_code       varchar(30) not null,
    dict_item_value varchar(30) not null,
    dict_item_name  varchar(100),
    del_flg         tinyint,
    create_time     datetime,
    update_time     datetime,
    index idx_dict_code_dict_item_value (dict_code, dict_item_value)
);