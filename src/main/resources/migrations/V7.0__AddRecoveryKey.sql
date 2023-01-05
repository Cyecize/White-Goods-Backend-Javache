create table recovery_keys
(
    id           varchar(255) not null,
    user_id      bigint,
    date_created datetime(6),
    primary key (id)
) engine = InnoDB;

alter table recovery_keys
    add constraint FK_recovery_keys_users foreign key (user_id) references users (id) on delete cascade;
