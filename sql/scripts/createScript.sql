-- --------------------------------------------------------
-- Host:                         10.191.39.1
-- Server version:               5.7.13 - MySQL Community Server (GPL)
-- Server OS:                    Linux
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for repcar
CREATE DATABASE IF NOT EXISTS `repcar` CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `repcar`;

-- Dumping structure for table repcar.category
CREATE TABLE IF NOT EXISTS `category` (
  `category_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_name` varchar(45) NOT NULL,
  `company_id` int(11) unsigned NOT NULL,
  `category_description` varchar(255) DEFAULT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`),
  KEY `FK_category_company` (`company_id`),
  CONSTRAINT `FK_category_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.category_zone
CREATE TABLE IF NOT EXISTS `category_zone` (
  `category_zone_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` int(11) unsigned NOT NULL,
  `zone_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`category_zone_id`),
  KEY `FK_CATEGORY_ZONE_CATEGORY` (`category_id`),
  KEY `FK_CATEGORY_ZONE_ZONE` (`zone_id`),
  CONSTRAINT `FK_CATEGORY_ZONE_CATEGORY` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_CATEGORY_ZONE_ZONE` FOREIGN KEY (`zone_id`) REFERENCES `zone` (`zone_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB COMMENT='Many-to-many mapping between categories and zones';

-- Data exporting was unselected.
-- Dumping structure for table repcar.category_shop
CREATE TABLE IF NOT EXISTS `category_shop` (
	`category_shop_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`category_id` INT(11) UNSIGNED NOT NULL,
	`shop_id` INT(11) UNSIGNED NOT NULL,
	PRIMARY KEY (`category_shop_id`),
	INDEX `FK_CATEGORY_SHOP_CATEGORY` (`category_id`),
	INDEX `FK_CATEGORY_SHOP_SHOP` (`shop_id`),
	CONSTRAINT `FK_CATEGORY_SHOP_CATEGORY` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT `FK_CATEGORY_SHOP_SHOP` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB COMMENT='Many-to-many mapping between categories and shops';

-- Data exporting was unselected.
-- Dumping structure for table repcar.client
CREATE TABLE IF NOT EXISTS `client` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` varchar(50) NOT NULL DEFAULT '0',
  `client_secret` varchar(256) NOT NULL DEFAULT '0',
  `scopes` varchar(250) NOT NULL DEFAULT '0',
  `grant_types` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.company
CREATE TABLE IF NOT EXISTS `company` (
  `company_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `company_name` varchar(45) NOT NULL,
  `company_address` varchar(255) NOT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.content
CREATE TABLE IF NOT EXISTS `content` (
    `content_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `content_url` VARCHAR(1000) NOT NULL,
    `content_type` ENUM('AVI','MPEG','MP3') NULL DEFAULT NULL,
    `product_id` INT(11) UNSIGNED NULL DEFAULT NULL,
    `category_id` INT(11) UNSIGNED NULL DEFAULT NULL,
    `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`content_id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.device
CREATE TABLE IF NOT EXISTS `device` (
  `device_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `device_name` varchar(45) NOT NULL,
  `device_manifacturer` varchar(45) DEFAULT NULL,
  `device_type_id` smallint(5) unsigned NOT NULL,
  `device_properties` varchar(500) DEFAULT NULL,
  `shop_id` int(11) unsigned NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.id_map
CREATE TABLE IF NOT EXISTS `id_map` (
  `map_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned DEFAULT NULL,
  `ssid` varchar(128) DEFAULT NULL,
  `weak_id` char(64) NOT NULL,
  `mac_address` char(128) DEFAULT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `company_id` int(11) unsigned DEFAULT NULL,
  `accepted` BIT(1) NOT NULL DEFAULT b'0',
  `email` VARCHAR(80) NULL DEFAULT NULL,
  PRIMARY KEY (`map_id`),
  UNIQUE KEY `WEAK_ID` (`weak_id`),
  KEY `FK_id_map_company` (`company_id`),
  CONSTRAINT `FK_id_map_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.jabber_settings
CREATE TABLE IF NOT EXISTS `jabber_settings` (
  `jabber_settings_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `operation_mode` ENUM('DISABLED','AUTO','PROMPT') NOT NULL DEFAULT 'AUTO' ,
  `cucm_url` VARCHAR(255) NOT NULL,
  `device_name` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `crm_profile_url` VARCHAR(255) NOT NULL,
  `user_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`jabber_settings_id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `FK__user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
)ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.meeting_info
CREATE TABLE IF NOT EXISTS `meeting_info` (
  `meeting_info_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `notes` varchar(1024) DEFAULT NULL,
  `user_id` int(11) unsigned DEFAULT NULL,
  `product_id` int(11) unsigned DEFAULT NULL,
  `operator_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`meeting_info_id`),
  KEY `FK_MEETING_INFO_USER` (`user_id`),
  KEY `FK_MEETING_INFO_PRODUCT` (`product_id`),
  KEY `FK_MEETING_INFO_USER_2` (`operator_id`),
  CONSTRAINT `FK_MEETING_INFO_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `FK_MEETING_INFO_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `FK_MEETING_INFO_USER_2` FOREIGN KEY (`operator_id`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.notification
CREATE TABLE IF NOT EXISTS `notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) unsigned DEFAULT NULL,
  `notification_url` varchar(50) NOT NULL DEFAULT '0',
  `event_type` enum('INOUT','MOVEMENT') NOT NULL,
  PRIMARY KEY (`notification_id`),
  UNIQUE KEY `notification_url` (`notification_url`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `COMPANY_ID` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB COMMENT='Notification URL for receiving events from cmx';

-- Data exporting was unselected.
-- Dumping structure for table repcar.oauth_access_token
CREATE TABLE IF NOT EXISTS `oauth_access_token` (
  `token_id` varchar(256) NOT NULL,
  `token` blob,
  `authentication_id` varchar(256) DEFAULT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) NOT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `authentication_id` (`authentication_id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.oauth_refresh_token
CREATE TABLE IF NOT EXISTS `oauth_refresh_token` (
  `token_id` varchar(256) NOT NULL,
  `token` blob NOT NULL,
  `authentication` blob NOT NULL,
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.payment
CREATE TABLE IF NOT EXISTS `payment` (
  `payment_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `transaction_id` CHAR(64) NOT NULL,
  `weak_id` CHAR(64) NOT NULL,
  `event_id` CHAR(64) NOT NULL,
  `event_status` ENUM('check','buy') NOT NULL,
  `device_id` INT(10) UNSIGNED NULL DEFAULT NULL,
  `product_id` INT(10) UNSIGNED NULL DEFAULT NULL,
  `shop_id` INT(10) UNSIGNED NULL DEFAULT NULL,
  `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`payment_id`),
  UNIQUE INDEX `event_id` (`event_id`)
)ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.product
CREATE TABLE IF NOT EXISTS `product` (
  `product_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_name` varchar(45) DEFAULT NULL,
  `product_price` varchar(45) DEFAULT NULL,
  `product_image` varchar(45) DEFAULT NULL,
  `product_description` varchar(4555) DEFAULT NULL,
  `product_rfid` VARCHAR(255) DEFAULT NULL,
  `product_nfc` VARCHAR(255) DEFAULT NULL,
  `company_id` int(11) unsigned DEFAULT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `product_ecp` (`product_rfid`),
  UNIQUE KEY `product_nfc` (`product_nfc`),
  KEY `COMPANY_ID` (`company_id`),
  CONSTRAINT `FK_PRODUCT_COMPANY` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.product_category
CREATE TABLE IF NOT EXISTS `product_category` (
  `product_category_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `category_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`product_category_id`),
  KEY `CATEGORY_ID` (`category_id`),
  KEY `PRODUCT_ID` (`product_id`),
  CONSTRAINT `FK_PRODUCT_CATEGORY_CATEGORY` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
  CONSTRAINT `FK_PRODUCT_CATEGORY_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.product_shop
CREATE TABLE IF NOT EXISTS `product_shop` (
  `product_shop_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) unsigned DEFAULT NULL,
  `product_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`product_shop_id`),
  KEY `SHOP_ID` (`shop_id`),
  KEY `PRODUCT_ID` (`product_id`),
  CONSTRAINT `FK_PRODUCT_SHOP_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PRODUCT_SHOP_SHOP` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `recommendation_details` (
    `recommendation_details_id` INT(11) NOT NULL AUTO_INCREMENT,
    `application_name` VARCHAR(50) NULL DEFAULT NULL,
    `application_access_key` VARCHAR(100) NULL DEFAULT NULL,
    `product_recommender_url` VARCHAR(50) NULL DEFAULT NULL,
    `category_recommender_url` VARCHAR(50) NULL DEFAULT NULL,
    `company_id` INT(11) UNSIGNED NOT NULL,
    PRIMARY KEY (`recommendation_details_id`),
    UNIQUE INDEX `company_id` (`company_id`),
    CONSTRAINT `FK_recommendation_details_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
)
ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.recommendation_metadata
CREATE TABLE IF NOT EXISTS `recommendation_metadata` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `last_imported_product_time` timestamp NULL DEFAULT NULL,
  `last_imported_category_time` timestamp NULL DEFAULT NULL,
  `company_id` INT(11) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.shop
CREATE TABLE IF NOT EXISTS `shop` (
  `shop_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`shop_name` VARCHAR(45) NOT NULL,
	`shop_country` VARCHAR(45) NOT NULL,
	`shop_city` VARCHAR(45) NOT NULL,
	`shop_address` VARCHAR(255) NOT NULL,
	`shop_url` VARCHAR(255) NULL DEFAULT NULL,
	`shop_type` VARCHAR(45) NULL DEFAULT NULL,
	`company_id` INT(11) UNSIGNED NOT NULL,
	PRIMARY KEY (`shop_id`),
	UNIQUE INDEX `shop_name_shop_country_shop_city_shop_address` (`shop_name`, `shop_country`, `shop_city`, `shop_address`),
    INDEX `FK_SHOP_COMPANY` (`company_id`),
	CONSTRAINT `FK_SHOP_COMPANY` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.user
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(80) UNIQUE DEFAULT NULL,
  `user_email` varchar(80) UNIQUE DEFAULT NULL,
  `user_password` varchar(45) DEFAULT NULL,
  `user_first_name` varchar(45) DEFAULT NULL,
  `user_last_name` varchar(45) DEFAULT NULL,
  `user_image` varchar(45) DEFAULT NULL,
  `user_role` ENUM ('ROLE_OPERATOR','ROLE_ADMIN','ROLE_USER','ROLE_ANONYMOUS') DEFAULT 'ROLE_ANONYMOUS',
  `company_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_name_company_id` (`user_name`, `company_id`),
  UNIQUE INDEX `user_email_company_id` (`user_email`, `company_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `FK_COMPANY` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Data exporting was unselected.
-- Dumping structure for table repcar.zone
CREATE TABLE IF NOT EXISTS `zone` (
  `zone_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `zone_name` varchar(100) NOT NULL,
  `location_map_hierarchy` VARCHAR(300) NOT NULL,
  `shop_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`zone_id`),
  UNIQUE INDEX `location_map_hierarchy` (`location_map_hierarchy`),
  KEY `FK_zone_company` (`company_id`),
  CONSTRAINT `FK_zone_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE `oauth_client_details` (
 `client_id` VARCHAR(256) NOT NULL,
 `resource_ids` VARCHAR(256) NULL DEFAULT NULL,
 `client_secret` VARCHAR(256) NULL DEFAULT NULL,
 `scope` VARCHAR(256) NULL DEFAULT NULL,
 `authorized_grant_types` VARCHAR(256) NULL DEFAULT NULL,
 `web_server_redirect_uri` VARCHAR(256) NULL DEFAULT NULL,
 `authorities` VARCHAR(256) NULL DEFAULT NULL,
 `access_token_validity` INT(11) NULL DEFAULT NULL,
 `refresh_token_validity` INT(11) NULL DEFAULT NULL,
 `additional_information` VARCHAR(4096) NULL DEFAULT NULL,
 `autoapprove` VARCHAR(256) NULL DEFAULT NULL,
 PRIMARY KEY (`client_id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
CREATE TABLE `oauth_refresh_token` (
 `token_id` VARCHAR(150) NOT NULL COLLATE 'utf8_unicode_ci',
 `token` BLOB NOT NULL,
 `authentication` BLOB NOT NULL,
 PRIMARY KEY (`token_id`)
)
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
