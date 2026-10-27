UPDATE orders
SET total_price = ROUND(total_price / 1.95583, 2), subtotal = ROUND(subtotal / 1.95583, 2);
