-- Makes Lanatus product deletion more sane
-- For purchases, the record is kept with a stale id, and
-- for positions, the deletion is restricted to prevent
-- accidental deletion of products which are still owned by people

ALTER TABLE lanatus_purchase
  DROP CONSTRAINT lanatus_purchases_lanatus_products_id_fk;
ALTER TABLE lanatus_purchase
  ADD CONSTRAINT lanatus_purchases_lanatus_products_id_fk
FOREIGN KEY (product_id) REFERENCES lanatus_product (id)
ON UPDATE CASCADE
ON DELETE NO ACTION;

ALTER TABLE lanatus_position
  DROP CONSTRAINT lanatus_positions_lanatus_products_id_fk;
ALTER TABLE lanatus_position
  ADD CONSTRAINT lanatus_positions_lanatus_products_id_fk
FOREIGN KEY (purchase_id) REFERENCES lanatus_product (id)
ON UPDATE CASCADE
ON DELETE RESTRICT;
