ALTER TABLE `pay`.`t_package_stat` DROP COLUMN `retention_count` , DROP COLUMN `pay_earning` , DROP COLUMN `pay_count` , DROP COLUMN `activate_count` , CHANGE COLUMN `channel_id` `channel_id` BIGINT(21) NULL  AFTER `stat_date` , CHANGE COLUMN `product_id` `product_id` BIGINT(21) NULL DEFAULT NULL  AFTER `channel_id`;
ALTER TABLE `pay`.`t_package_stat` DROP COLUMN `sync_earning` ;
