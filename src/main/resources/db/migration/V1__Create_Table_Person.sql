CREATE DATABASE  IF NOT EXISTS `spring_rest_example`;
USE `spring_rest_example`;

DROP TABLE IF EXISTS `person`;

CREATE TABLE `person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);


