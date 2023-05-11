create table daily_log_files
(
    id             bigint      not null auto_increment,
    filename       varchar(255),
    date_processed datetime(6) not null,
    uniqueVisitors integer     not null,
    primary key (id)
) engine = InnoDB;

create table visitor_hits_per_hour
(
    id                bigint  not null auto_increment,
    daily_log_file_id bigint  not null,
    hour              integer not null,
    hits              integer not null,
    primary key (id)
) engine = InnoDB;

create table visitor_hits_per_ip
(
    id                bigint       not null auto_increment,
    daily_log_file_id bigint       not null,
    ip                varchar(255) not null,
    hits              integer      not null,
    primary key (id)
) engine = InnoDB;

create table visitor_hits_per_page
(
    id                bigint       not null auto_increment,
    daily_log_file_id bigint       not null,
    url               varchar(255) not null,
    hits              integer      not null,
    primary key (id)
) engine = InnoDB;

alter table visitor_hits_per_hour
    add constraint FK_visitor_hits_per_hour_Daily_log_files foreign key (daily_log_file_id) references daily_log_files (id);

alter table visitor_hits_per_ip
    add constraint FK_visitor_hits_per_ip_Daily_log_files foreign key (daily_log_file_id) references daily_log_files (id);

alter table visitor_hits_per_page
    add constraint FK_visitor_hits_per_page_Daily_log_files foreign key (daily_log_file_id) references daily_log_files (id);
