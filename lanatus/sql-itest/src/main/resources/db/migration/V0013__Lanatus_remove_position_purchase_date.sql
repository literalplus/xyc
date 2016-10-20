-- Removes position's purchase date, which essentially duplicated purchase.created

ALTER TABLE lanatus_position
  DROP COLUMN purchasedate;
