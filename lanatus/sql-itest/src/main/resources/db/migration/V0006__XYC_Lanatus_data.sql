-- base schema for tables related to XYC Lanatus, an API for a premium currency
-- called melons that provides purchases with a log and support for concurrent
-- modifications.

CREATE TABLE `lanatus_player` (
  `player_uuid` CHAR(36)    NOT NULL,
  `updated`     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP /*ON UPDATE CURRENT_TIMESTAMP*/,
  `created`     TIMESTAMP   NOT NULL DEFAULT '0000-00-00 00:00:00',
  `melons`      INT(11)     NOT NULL DEFAULT '0',
  `lastrank`    VARCHAR(20) NOT NULL DEFAULT 'default',
  PRIMARY KEY (`player_uuid`)
);

CREATE TABLE `lanatus_products` (
  `id`          CHAR(36)     NOT NULL,
  `module`      VARCHAR(20)  NOT NULL,
  `name`        VARCHAR(100) NOT NULL,
  `displayname` VARCHAR(255) NOT NULL,
  `description` TEXT         NOT NULL,
  `icon`        VARCHAR(100) NOT NULL DEFAULT 'dirt'
  COMMENT 'Minecraft Material name of the icon for this product',
  `melonscost`  INT(11)      NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `lanatus_purchases` (
  `id`          CHAR(36)  NOT NULL,
  `player_uuid` CHAR(36)  NOT NULL,
  `product_id`  CHAR(36)  NOT NULL,
  `updated`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP /*ON UPDATE CURRENT_TIMESTAMP*/,
  `created`     TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  `data`        TEXT      NOT NULL,
  `comment`     VARCHAR(255)       DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `lanatus_purchases_lanatus_player_player_uuid_fk` FOREIGN KEY (`player_uuid`) REFERENCES `lanatus_player` (`player_uuid`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  CONSTRAINT `lanatus_purchases_lanatus_products_id_fk` FOREIGN KEY (`product_id`) REFERENCES `lanatus_products` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

CREATE TABLE `lanatus_positions` (
  `purchase_id`  CHAR(36)  NOT NULL,
  `player_uuid`  CHAR(36)  NOT NULL,
  `product_id`   CHAR(36)  NOT NULL,
  `purchasedate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data`         TEXT      NOT NULL,
  PRIMARY KEY (`purchase_id`),
  CONSTRAINT `lanatus_positions_lanatus_player_player_uuid_fk` FOREIGN KEY (`player_uuid`) REFERENCES `lanatus_player` (`player_uuid`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  CONSTRAINT `lanatus_positions_lanatus_products_id_fk` FOREIGN KEY (`purchase_id`) REFERENCES `lanatus_products` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);
