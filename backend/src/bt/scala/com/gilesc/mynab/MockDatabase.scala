package com.gilesc
package mynab

import scalikejdbc._
import scalikejdbc.jsr310._

/**
 * TODO: Eventually I would really like a system that reads in the *.sql
 * files from the flyway directory and creates migrations based on that.
 */
object MockDatabase {
  implicit val session = AutoSession

  def migrate(): Unit = {

    createBudgetSystemTables()
  }

  def createBudgetSystemTables(): Unit = {
    sql"""
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(120) NOT NULL,
  `email` varchar(120) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`),
UNIQUE KEY `username` (`username`),
UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB;
    """.execute().apply()

    sql"""
CREATE TABLE `account_groups` (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) unsigned NOT NULL,
  `name` VARCHAR(120) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`user_id`) REFERENCES users(id)
) ENGINE=InnoDB;
    """.execute().apply()

    sql"""
CREATE TABLE `account_types` (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(120) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB;
    """.execute().apply()

    sql"""
CREATE TABLE `accounts` (
  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) unsigned NOT NULL,
  `account_group_id` BIGINT(20) unsigned NOT NULL,
  `account_type_id` BIGINT(20) unsigned NOT NULL,
  `name` VARCHAR(120) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`user_id`) REFERENCES users(id),
FOREIGN KEY (`account_group_id`) REFERENCES account_groups(id),
FOREIGN KEY (`account_type_id`) REFERENCES account_types(id)
) ENGINE=InnoDB;
    """.execute().apply()

    sql"""
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
    """.execute().apply()

    // seed with a fake user
    sql"""
INSERT INTO users (username, email)
VALUES
  ('testuser', 'test@email.com');
      """.updateAndReturnGeneratedKey.apply()
    ()
  }
}
