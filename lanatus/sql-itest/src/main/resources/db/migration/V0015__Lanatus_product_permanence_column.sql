-- This specified whether a product will be represented by a position after
-- being purchased. Non-permanent products are a one-time thing.

ALTER TABLE lanatus_product
  ADD permanent BOOLEAN DEFAULT 1 NOT NULL;
