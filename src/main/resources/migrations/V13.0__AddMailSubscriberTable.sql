create table mail_subscribers
(
    id              varchar(255) not null,
    email           varchar(255) not null,
    subscriber_type varchar(50)  not null,
    primary key (id)
) engine = InnoDB;
