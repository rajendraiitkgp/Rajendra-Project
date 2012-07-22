delete database if exists ias;
create database ias;

CREATE TABLE `user` (                  
          `userid` VARCHAR(255) NOT NULL,              
          `password` VARCHAR(255) NOT NULL,         
          `email` VARCHAR(255) NOT NULL,          
          PRIMARY KEY (`userid`)                    
        ) ENGINE=INNODB DEFAULT CHARSET=utf8;

        
CREATE TABLE `client` (                  
          `userid` VARCHAR(255) NOT NULL,              
          `clientid` VARCHAR(255) NOT NULL                         
        ) ENGINE=INNODB DEFAULT CHARSET=utf8;