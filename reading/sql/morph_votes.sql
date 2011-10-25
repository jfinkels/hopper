-- MySQL dump 10.10
--
-- Host: localhost    Database: sor
-- ------------------------------------------------------
-- Server version	5.0.24a

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `morph_votes`
--

DROP TABLE IF EXISTS `morph_votes`;
CREATE TABLE `morph_votes` (
  `document_id` varchar(30) NOT NULL default '',
  `subquery` varchar(200) NOT NULL default '',
  `form` varchar(60) NOT NULL default '',
  `occurrence` smallint(6) NOT NULL default '1',
  `lang_id` tinyint(4) NOT NULL default '0',
  `lemma` varchar(65) default NULL,
  `sequence_number` tinyint(4) default NULL,
  `morph_code` varchar(30) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `identity` varchar(30) default NULL,
  KEY `document_id` (`document_id`,`subquery`,`form`,`occurrence`,`identity`),
  UNIQUE `all` (`document_id`,`subquery`(145),`form`,`occurrence`,`lang_id`,`lemma`,`sequence_number`,`morph_code`,`time`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

