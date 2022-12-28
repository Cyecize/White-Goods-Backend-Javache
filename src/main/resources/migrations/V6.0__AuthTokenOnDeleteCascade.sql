alter table auth_tokens drop foreign key FK_auth_tokens_users;

alter table auth_tokens
    add constraint FK_auth_tokens_users foreign key (user_id) references users (id) on delete cascade;
