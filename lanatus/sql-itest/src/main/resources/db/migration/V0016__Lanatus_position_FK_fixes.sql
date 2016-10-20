-- lanatus_position has a wrong foreign key on the purchase field
-- to the products table and is missing the FK for products
-- This patch fixes that

ALTER TABLE mt_main.lanatus_position
  ADD CONSTRAINT lanatus_position_lanatus_product_id_fk
FOREIGN KEY (product_id) REFERENCES lanatus_product (id);
ALTER TABLE mt_main.lanatus_position
  DROP CONSTRAINT lanatus_positions_lanatus_products_id_fk;
ALTER TABLE mt_main.lanatus_position
  ADD CONSTRAINT lanatus_positions_lanatus_purchase_id_fk
FOREIGN KEY (purchase_id) REFERENCES lanatus_purchase (id);
