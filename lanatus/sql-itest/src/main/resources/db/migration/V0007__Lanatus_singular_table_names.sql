-- Lanatus tables have been erroneously created with plural names in 0006.
-- This snippet renames them to singular.

ALTER TABLE lanatus_positions
  RENAME TO lanatus_position;

ALTER TABLE lanatus_products
  RENAME TO lanatus_product;

ALTER TABLE lanatus_purchases
  RENAME TO lanatus_purchase;
