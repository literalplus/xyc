-- Adds an extra column for the actual amount of melons spent on
-- a purchase to account for changing prices and melon giving,
-- which represents a negative cost.

ALTER TABLE lanatus_purchase
  ADD melonscost INT NOT NULL
COMMENT 'the amount of melons that was actually spent on this purchase, may differ from the cost of the product or be negative';
