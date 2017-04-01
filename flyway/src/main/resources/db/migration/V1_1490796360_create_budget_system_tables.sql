CREATE TABLE `account_groups` (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(120) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `account_types` (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(120) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `accounts` (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `account_group_id` BIGINT(20) unsigned NOT NULL,
  `type_id` BIGINT(20) unsigned NOT NULL,
  `name` VARCHAR(120) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`account_group_id`) REFERENCES account_groups(id),
FOREIGN KEY (`type_id`) REFERENCES account_types(id)
) ENGINE=InnoDB;

CREATE TABLE `transactions` (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) unsigned NOT NULL,
  `date` DATE NOT NULL,
  `payee` VARCHAR(120) NOT NULL,
  `minor_category` VARCHAR(120) NOT NULL,
  `major_category` VARCHAR(120) NOT NULL,
  `memo` VARCHAR(255) NOT NULL,
  `withdrawal` DECIMAL NOT NULL,
  `deposit` DECIMAL NOT NULL,
  `cleared` BIT NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`account_id`) REFERENCES accounts(id)
) ENGINE=InnoDB;
