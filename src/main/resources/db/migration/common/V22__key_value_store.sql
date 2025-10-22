create table if not exists key_value_store (
    key varchar(255) primary key,
    value text not null
);