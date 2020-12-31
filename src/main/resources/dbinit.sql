DROP DATABASE IF EXISTS twitchclipscraper;
DROP USER IF EXISTS `tcvuser`@`%`;
CREATE DATABASE IF NOT EXISTS twitchclipscraper CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `tcvuser`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `twitchclipscraper`.* TO `twitchclipscraper`@`%`;
FLUSH PRIVILEGES;