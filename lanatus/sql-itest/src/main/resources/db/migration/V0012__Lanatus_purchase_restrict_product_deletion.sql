-- Restricts deletion of products if there are still purchases
-- associated with them. This makes code simpler.

ALTER TABLE lanatus_purchase
  DROP CONSTRAINT lanatus_purchases_lanatus_products_id_fk;
ALTER TABLE lanatus_purchase
  ADD CONSTRAINT lanatus_purchases_lanatus_products_id_fk
FOREIGN KEY (product_id) REFERENCES lanatus_product (id)
ON UPDATE CASCADE
ON DELETE RESTRICT;
