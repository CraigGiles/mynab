CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(120) NOT NULL,
  `email` varchar(120) NOT NULL,
  `password_hash` char(255) NOT NULL,
  `password_salt` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB;

INSERT INTO users (username, email, password_hash, password_salt)
VALUES ("testuser", "my@email.com", "passwordhash", "passwordsalt");
