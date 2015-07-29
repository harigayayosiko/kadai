-- データベース作成
-- ==============================================================
drop database if exists record_writer;
GRANT USAGE ON *.* TO 'record_writer'@'localhost';
DROP USER 'record_writer'@'localhost';

create database record_writer character set utf8mb4;
create user 'record_writer'@'localhost' identified by 'record_writer';
grant all privileges on record_writer.* to 'record_writer'@'localhost';
-- ==============================================================