create table coupon_codes
(
    id             bigint      not null auto_increment,
    promotion_id   bigint      not null,
    code           varchar(50) not null,
    max_usages     integer     not null,
    current_usages integer     not null,
    create_date    datetime(6) not null,
    expiry_date    datetime(6) not null,
    enabled        bit         not null,
    primary key (id)
) engine = InnoDB;

alter table coupon_codes
    add constraint FK_coupon_codes_promotions foreign key (promotion_id) references promotions (id) on delete cascade;

alter table coupon_codes
    add constraint UK_coupon_codes_code unique (code);

create index idx_coupon_codes_code_enabled on coupon_codes (code, enabled);
