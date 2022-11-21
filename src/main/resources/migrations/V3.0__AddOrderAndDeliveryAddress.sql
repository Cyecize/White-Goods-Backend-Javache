create table user_addresses
(
    id                bigint       not null auto_increment,
    user_id           bigint       not null,
    full_name         varchar(255) not null,
    phone_number      varchar(50)  not null,
    email             varchar(255) not null,
    country           varchar(50)  not null,
    city              varchar(50)  not null,
    address_line      varchar(255) not null,
    notes             varchar(255),
    preferred_address bit          not null,

    primary key (id)
) engine = InnoDB;

alter table user_addresses
    add constraint FK_user_addresses_users foreign key (user_id) references users (id) on delete cascade;

create table delivery_addresses
(
    id           bigint       not null auto_increment,
    full_name    varchar(255) not null,
    phone_number varchar(50)  not null,
    email        varchar(255) not null,
    country      varchar(50)  not null,
    city         varchar(50)  not null,
    address_line varchar(255) not null,
    notes        varchar(255),

    primary key (id)
) engine = InnoDB;


create table orders
(
    id             bigint           not null auto_increment,
    user_id        bigint,
    delivery_price double precision not null,
    status         varchar(50)      not null,
    date           datetime(6)      not null,
    address_id     bigint           not null,

    primary key (id)
) engine = InnoDB;

alter table orders
    add constraint FK_orders_users foreign key (user_id) references users (id) on delete set null;

alter table orders
    add constraint FK_orders_delivery_addresses foreign key (address_id) references delivery_addresses (id);

create table order_items
(
    order_id       bigint           not null,
    product_id     bigint           not null,
    quantity       integer          not null,
    price_snapshot double precision not null,

    primary key (order_id, product_id)
) engine = InnoDB;

alter table order_items
    add constraint FK_order_items_orders foreign key (order_id) references orders (id) on delete cascade;

alter table order_items
    add constraint FK_order_items_products foreign key (product_id) references products (id);
