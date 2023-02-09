create table warehouse_revisions
(
    id   bigint not null auto_increment,
    date datetime(6),

    primary key (id)
) engine = InnoDB;

alter table quantity_updates
    add revision_id bigint;

alter table quantity_updates
    add constraint FK_quantity_updates_warehouse_revisions foreign key (revision_id) references warehouse_revisions (id);
