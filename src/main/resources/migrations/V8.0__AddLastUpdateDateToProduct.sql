alter table products
    add last_updated datetime(6) not null default CURRENT_TIMESTAMP(6);
