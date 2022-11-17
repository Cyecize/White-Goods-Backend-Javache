create table auth_tokens
(
    id               varchar(255) not null,
    last_access_date datetime(6),
    user_id          bigint,
    primary key (id)
) engine=InnoDB;
create table home_carousel
(
    id                    bigint not null auto_increment,
    custom_link           varchar(255),
    custom_link_same_page bit,
    enabled               bit,
    image_url             varchar(255),
    order_number          integer,
    product_id            bigint,
    text_bg               varchar(255),
    text_en               varchar(255),
    primary key (id)
) engine=InnoDB;
create table images
(
    id         bigint not null auto_increment,
    image_url  varchar(255),
    product_id bigint,
    primary key (id)
) engine=InnoDB;
create table product_categories
(
    id        bigint not null auto_increment,
    image_url varchar(255),
    name_bg   varchar(255),
    name_en   varchar(255),
    primary key (id)
) engine=InnoDB;
create table product_category_tags
(
    category_id bigint not null,
    tag_id      bigint not null,
    primary key (category_id, tag_id)
) engine=InnoDB;
create table products
(
    id             bigint  not null auto_increment,
    category_id    bigint,
    description_bg TEXT,
    description_en TEXT,
    enabled        bit,
    image_url      varchar(255),
    price          double precision,
    product_name   varchar(255),
    quantity       integer not null,
    primary key (id)
) engine=InnoDB;
create table products_specifications
(
    product_id       bigint not null,
    specification_id bigint not null,
    primary key (product_id, specification_id)
) engine=InnoDB;
create table products_tags
(
    product_id bigint not null,
    tag_id     bigint not null,
    primary key (product_id, tag_id)
) engine=InnoDB;
create table questions
(
    id           bigint not null auto_increment,
    date         datetime(6),
    email        varchar(255),
    full_name    varchar(255),
    message      varchar(255),
    phone_number varchar(255),
    primary key (id)
) engine=InnoDB;
create table roles
(
    id   bigint       not null,
    role varchar(255) not null,
    primary key (id)
) engine=InnoDB;
create table shopping_cart_items
(
    quantity   integer not null,
    product_id bigint  not null,
    cart_id    bigint  not null,
    primary key (product_id, cart_id)
) engine=InnoDB;
create table shopping_carts
(
    id            bigint not null,
    last_modified datetime(6),
    user_id       bigint not null,
    primary key (id)
) engine=InnoDB;
create table specification_types
(
    id                 bigint       not null auto_increment,
    specification_type varchar(255) not null,
    title_bg           varchar(255),
    title_en           varchar(255),
    primary key (id)
) engine=InnoDB;
create table specification_types_categories
(
    specification_type_id bigint not null,
    category_id           bigint not null
) engine=InnoDB;
create table specifications
(
    id                    bigint not null auto_increment,
    specification_type_id bigint,
    value_bg              varchar(255),
    value_en              varchar(255),
    primary key (id)
) engine=InnoDB;
create table tags
(
    id   bigint not null auto_increment,
    name varchar(255),
    primary key (id)
) engine=InnoDB;
create table users
(
    id              bigint not null auto_increment,
    date_registered datetime(6) not null,
    email           varchar(255),
    password        varchar(255),
    username        varchar(255),
    primary key (id)
) engine=InnoDB;
create table users_roles
(
    user_id bigint not null,
    role_id bigint not null
) engine=InnoDB;

alter table roles
    add constraint UK_roles_role unique (role);

alter table specification_types
    add constraint UK_specification_types_specification_type unique (specification_type);

alter table users
    add constraint UK_users_email unique (email);

alter table users
    add constraint UK_users_username unique (username);
alter table auth_tokens
    add constraint FK_auth_tokens_users foreign key (user_id) references users (id);
alter table images
    add constraint FK_images_products foreign key (product_id) references products (id);
alter table product_category_tags
    add constraint FK_product_category_tags_tags foreign key (tag_id) references tags (id);
alter table product_category_tags
    add constraint FK_product_category_tags_product_categories foreign key (category_id) references product_categories (id);
alter table products
    add constraint FK_products_product_categories foreign key (category_id) references product_categories (id);
alter table products_specifications
    add constraint FK_product_specifications_specifications foreign key (specification_id) references specifications (id);
alter table products_specifications
    add constraint FK_products_specifications_products foreign key (product_id) references products (id);
alter table products_tags
    add constraint FK_products_tags_tags foreign key (tag_id) references tags (id);
alter table products_tags
    add constraint FK_products_tags_products foreign key (product_id) references products (id);
alter table shopping_cart_items
    add constraint FK_shopping_cart_items_products foreign key (product_id) references products (id) on delete cascade;
alter table shopping_cart_items
    add constraint FK_shopping_cart_items_shopping_carts foreign key (cart_id) references shopping_carts (id) on delete cascade;
alter table shopping_carts
    add constraint FK_shopping_carts_users foreign key (user_id) references users (id) on delete cascade;
alter table specification_types_categories
    add constraint FK_specification_types_categories_product_categories foreign key (category_id) references product_categories (id);
alter table specification_types_categories
    add constraint FK_specification_types_categories_specification_types foreign key (specification_type_id) references specification_types (id);
alter table specifications
    add constraint FK_specifications_specification_types foreign key (specification_type_id) references specification_types (id);
alter table users_roles
    add constraint FK_users_roles_roles foreign key (role_id) references roles (id);
alter table users_roles
    add constraint FK_users_roles_users foreign key (user_id) references users (id);

