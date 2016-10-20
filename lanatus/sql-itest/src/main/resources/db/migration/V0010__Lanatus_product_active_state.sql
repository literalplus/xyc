-- Adds a switch to products to mark them as inactive, causing
-- them to be no longer available for purchase.

ALTER TABLE lanatus_product
  ADD active BOOLEAN DEFAULT 1 NOT NULL;
