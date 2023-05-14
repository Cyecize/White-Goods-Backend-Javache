alter table visitor_hits_per_hour
    drop foreign key FK_visitor_hits_per_hour_Daily_log_files;

alter table visitor_hits_per_ip
    drop foreign key FK_visitor_hits_per_ip_Daily_log_files;

alter table visitor_hits_per_page
    drop foreign key FK_visitor_hits_per_page_Daily_log_files;

drop table if exists daily_log_files;

create table daily_log_files
(
    id              bigint      not null auto_increment,
    filename        varchar(255),
    date_processed  datetime(6) not null,
    unique_visitors integer     not null,
    primary key (id)
) engine = InnoDB;

alter table visitor_hits_per_hour
    add constraint FK_visitor_hits_per_hour_Daily_log_files foreign key (daily_log_file_id) references daily_log_files (id);

alter table visitor_hits_per_ip
    add constraint FK_visitor_hits_per_ip_Daily_log_files foreign key (daily_log_file_id) references daily_log_files (id);

alter table visitor_hits_per_page
    add constraint FK_visitor_hits_per_page_Daily_log_files foreign key (daily_log_file_id) references daily_log_files (id);
