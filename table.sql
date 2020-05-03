create database ficss;
use ficss;

CREATE TABLE user (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50) NOT NULL,
    cn_name VARCHAR(255) NOT NULL,
    en_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE conference (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    conference_id VARCHAR(50) NOT NULL,
    conference_name VARCHAR(255) NOT NULL,
    creation_time VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE submission (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    conference_id VARCHAR(50) NOT NULL,
    submission_id VARCHAR(50) NOT NULL,
    submitter_id VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    abstract_text TEXT NOT NULL,
    keyword VARCHAR(255) NOT NULL,
    topic VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    paper_file_id VARCHAR(50) NOT NULL,
    slide_file_id VARCHAR(50) NOT NULL,
    commit_time VARCHAR(32) NOT NULL,
    last_modified VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE paper (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    paper_file_id VARCHAR(255) NOT NULL,
    submitter_id varchar(255) not null,
    paper_title VARCHAR(255) NOT NULL,
    paper_file_path VARCHAR(255) NOT NULL,
    commit_time VARCHAR(255) NOT NULL,
    last_modified VARCHAR(255) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE slide (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    slide_file_id VARCHAR(255) NOT NULL,
    submitter_id VARCHAR(255) NOT NULL,
    slide_title VARCHAR(255) NOT NULL,
    slide_file_path VARCHAR(255) NOT NULL,
    commit_time VARCHAR(255) NOT NULL,
    last_modified VARCHAR(255) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE topic (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    conference_id VARCHAR(50) NOT NULL,
    topic_id VARCHAR(50) NOT NULL,
    topic_name VARCHAR(255) NOT NULL,
    topic_size INT(5) NOT NULL,
    creation_time VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE agenda (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    conference_id VARCHAR(50) NOT NULL,
    agenda_id VARCHAR(50) NOT NULL,
    agenda_name VARCHAR(50) NOT NULL,
    agenda_date VARCHAR(32) NOT NULL,
    creation_time VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE event (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    agenda_id VARCHAR(50) NOT NULL,
    event_id VARCHAR(50) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    event_start_time VARCHAR(32) NOT NULL,
    event_end_time VARCHAR(32) NOT NULL,
    room VARCHAR(32),
    description VARCHAR(255),
    status int(2) not null,
    creation_time VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE session (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(50) NOT NULL,
    session_id VARCHAR(50) NOT NULL,
    session_name VARCHAR(255) NOT NULL,
    session_room VARCHAR(255) NOT NULL,
    session_reviewer TEXT,
    session_chair TEXT,
    session_index INT(10) NOT NULL,
    creation_time VARCHAR(32) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE session_paper (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(50) NOT NULL,
    paper_file_id VARCHAR(50) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;

CREATE TABLE auth_code (
    id INT(32) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL
)  ENGINE=INNODB , AUTO_INCREMENT=1 , CHARSET=UTF8;


