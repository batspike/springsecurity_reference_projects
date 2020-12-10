drop table if exists `users`;
drop table if exists `authorities`;

CREATE TABLE `users` (
  `USERNAME` VARCHAR(50) NOT NULL PRIMARY KEY,
  `PASSWORD` VARCHAR(500) NOT NULL,
  `ENABLED` boolean not null
) 
;
CREATE TABLE `authorities` (
  `USERNAME` VARCHAR(50) NOT NULL, 
  `AUTHORITY` VARCHAR(500) NOT NULL,
    KEY `username` (`username`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`)
  REFERENCES `users` (`username`)
)
;
