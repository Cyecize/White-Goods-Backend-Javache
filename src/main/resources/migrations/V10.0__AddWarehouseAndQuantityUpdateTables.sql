create table warehouse_deliveries
(
    id   bigint not null auto_increment,
    date datetime(6),

    primary key (id)
) engine = InnoDB;

create table quantity_updates
(
    id             bigint      not null auto_increment,
    product_id     bigint      not null,
    delivery_id    bigint,
    order_id       bigint,

    update_type    varchar(50) not null,
    date           datetime(6) not null,
    quantity_value integer     not null,

    primary key (id)
) engine = InnoDB;

alter table quantity_updates
    add constraint FK_quantity_updates_products foreign key (product_id) references products (id);

alter table quantity_updates
    add constraint FK_quantity_updates_warehouse_deliveries foreign key (delivery_id) references warehouse_deliveries (id);

alter table quantity_updates
    add constraint FK_quantity_updates_orders foreign key (order_id) references orders (id);
