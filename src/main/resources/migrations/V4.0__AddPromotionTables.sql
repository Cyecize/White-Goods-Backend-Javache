create table promotions
(
    id             bigint       not null auto_increment,
    name_bg        varchar(255) not null,
    name_en        varchar(255) not null,
    promotion_type varchar(50)  not null,
    discount_type  varchar(50)  not null,
    category_id    bigint,
    discount       double precision,
    min_subtotal   double precision,

    primary key (id)
) engine = InnoDB;

alter table promotions
    add constraint FK_promotions_categories foreign key (category_id) references product_categories (id) on delete cascade;


create table promotion_product_items
(
    product_id   bigint  not null,
    promotion_id bigint  not null,
    min_quantity integer not null,

    primary key (product_id, promotion_id)
) engine = InnoDB;


alter table promotion_product_items
    add constraint FK_promotion_product_items_products foreign key (product_id) references products (id) on delete cascade;

alter table promotion_product_items
    add constraint FK_promotion_product_items_promotions foreign key (promotion_id) references promotions (id) on delete cascade;
