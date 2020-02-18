create database ficss;
use ficss;

CREATE TABLE administrator (
	id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id VARCHAR(32) NOT NULL,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(32) NOT NULL,
    access_code VARCHAR(32) NOT NULL
)ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;
drop table slide;
CREATE TABLE submission (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    submission_id VARCHAR(32) NOT NULL,
    title TEXT NOT NULL,
    abstract_text TEXT NOT NULL,
    keyword VARCHAR(255) NOT NULL,
    topic VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    paper_file_id VARCHAR(32) NOT NULL,
    slide_file_id VARCHAR(32) NOT NULL,
    commit_time VARCHAR(32) NOT NULL,
    last_modified VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE paper (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    paper_file_id VARCHAR(32) NOT NULL,
    author varchar(32) not null,
    paper_title VARCHAR(255) NOT NULL,
    paper_file_path VARCHAR(255) NOT NULL,
    commit_time VARCHAR(32) NOT NULL,
    last_modified VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE slide (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    slide_file_id VARCHAR(32) NOT NULL,
    author VARCHAR(32) NOT NULL,
    slide_title VARCHAR(255) NOT NULL,
    slide_file_path VARCHAR(255) NOT NULL,
    commit_time VARCHAR(32) NOT NULL,
    last_modified VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE session (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(32) NOT NULL,
    session_name varchar(255) not null,
    session_room varchar(32) not null,
    session_date varchar(32) not null
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE session_reviewer (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(32) NOT NULL,
    reviewer_name varchar(32) not null
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE session_chair (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(32) NOT NULL,
    chair_name varchar(32) not null
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE session_paper (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(32) NOT NULL,
    paper_file_id VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE topic (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    topic_id VARCHAR(32) NOT NULL,
    content VARCHAR(255) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE agenda (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    agenda_id VARCHAR(32) NOT NULL,
    event VARCHAR(255) NOT NULL,
    event_date VARCHAR(32) NOT NULL,
    event_start_time VARCHAR(32) NOT NULL,
    event_end_time VARCHAR(32) NOT NULL,
    room VARCHAR(32),
    description VARCHAR(255)
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE session_agenda (
	id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    agenda_id VARCHAR(32) NOT NULL,
    session_id VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8

#CREATE TABLE  ()  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;