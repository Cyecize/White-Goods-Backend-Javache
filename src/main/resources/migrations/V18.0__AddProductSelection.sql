create table product_selections
(
    id           bigint  not null,
    order_number integer not null,
    constraint PK_product_selections primary key (id),
    constraint FK_product_selections_products foreign key (id) references products (id) on delete cascade
) engine = InnoDB;
