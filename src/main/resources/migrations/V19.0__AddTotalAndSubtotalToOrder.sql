alter table orders
    add total_price double precision not null default 0.0;

alter table orders
    add subtotal double precision not null default 0.0;
