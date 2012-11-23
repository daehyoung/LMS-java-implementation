-- MySQL dump 10.13  Distrib 5.5.27, for osx10.6 (i386)
--
-- Host: 192.168.0.65    Database: librisDB
-- ------------------------------------------------------
-- Server version	5.5.24-0ubuntu0.12.04.1

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
-- Table structure for table `creator`
--

DROP TABLE IF EXISTS `creator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creator` (
  `creator_ID` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) NOT NULL,
  PRIMARY KEY (`creator_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creator`
--

LOCK TABLES `creator` WRITE;
/*!40000 ALTER TABLE `creator` DISABLE KEYS */;
INSERT INTO `creator` VALUES (1,'James','Liang'),(2,'Paul','Bailey'),(3,'Julian','Barnes'),(4,'Saul','Bellow');
/*!40000 ALTER TABLE `creator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creatorHasResource`
--

DROP TABLE IF EXISTS `creatorHasResource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creatorHasResource` (
  `creator_creator_ID` int(11) NOT NULL,
  `resource_resource_ID` int(11) NOT NULL,
  PRIMARY KEY (`creator_creator_ID`,`resource_resource_ID`),
  KEY `fk_creator_has_resource_resource1_idx` (`resource_resource_ID`),
  KEY `fk_creator_has_resource_creator1_idx` (`creator_creator_ID`),
  CONSTRAINT `fk_creator_has_resource_creator1` FOREIGN KEY (`creator_creator_ID`) REFERENCES `creator` (`creator_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_creator_has_resource_resource1` FOREIGN KEY (`resource_resource_ID`) REFERENCES `resource` (`resource_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creatorHasResource`
--

LOCK TABLES `creatorHasResource` WRITE;
/*!40000 ALTER TABLE `creatorHasResource` DISABLE KEYS */;
INSERT INTO `creatorHasResource` VALUES (1,1),(2,1),(3,2),(4,3);
/*!40000 ALTER TABLE `creatorHasResource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emp`
--

DROP TABLE IF EXISTS `emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `emp` (
  `id` int(20) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emp`
--

LOCK TABLES `emp` WRITE;
/*!40000 ALTER TABLE `emp` DISABLE KEYS */;
INSERT INTO `emp` VALUES (14,'James'),(15,'Mike'),(17,'Mee'),(18,'M4e'),(3,'ww');
/*!40000 ALTER TABLE `emp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loan` (
  `loan_ID` int(11) NOT NULL AUTO_INCREMENT,
  `check_out_date` datetime NOT NULL,
  `check_in_date` datetime DEFAULT NULL,
  `due_date` datetime DEFAULT NULL,
  `last_email_date` datetime DEFAULT NULL,
  `fine` decimal(5,2) DEFAULT NULL,
  `fine_paid` tinyint(1) DEFAULT NULL,
  `user_user_ID` int(11) NOT NULL,
  `resourceCopy_barcode` int(11) NOT NULL,
  PRIMARY KEY (`loan_ID`),
  KEY `fk_loan_user1_idx` (`user_user_ID`),
  KEY `fk_loan_resourceCopy1_idx` (`resourceCopy_barcode`),
  CONSTRAINT `fk_loan_resourceCopy1` FOREIGN KEY (`resourceCopy_barcode`) REFERENCES `resourceCopy` (`barcode`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_loan_user1` FOREIGN KEY (`user_user_ID`) REFERENCES `user` (`user_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan`
--

LOCK TABLES `loan` WRITE;
/*!40000 ALTER TABLE `loan` DISABLE KEYS */;
/*!40000 ALTER TABLE `loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `id` bigint(20) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reference`
--

DROP TABLE IF EXISTS `reference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference` (
  `reference_ID` int(11) NOT NULL AUTO_INCREMENT,
  `placed_date` date NOT NULL,
  `expire_date` date DEFAULT NULL,
  `user_user_ID` int(11) NOT NULL,
  `resourceCopy_barcode` int(11) NOT NULL,
  PRIMARY KEY (`reference_ID`),
  KEY `fk_reference_user1_idx` (`user_user_ID`),
  KEY `fk_reference_resourceCopy1_idx` (`resourceCopy_barcode`),
  CONSTRAINT `fk_reference_resourceCopy1` FOREIGN KEY (`resourceCopy_barcode`) REFERENCES `resourceCopy` (`barcode`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reference_user1` FOREIGN KEY (`user_user_ID`) REFERENCES `user` (`user_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reference`
--

LOCK TABLES `reference` WRITE;
/*!40000 ALTER TABLE `reference` DISABLE KEYS */;
/*!40000 ALTER TABLE `reference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reservation` (
  `reservation_ID` int(11) NOT NULL AUTO_INCREMENT,
  `reserved_date` datetime NOT NULL,
  `available_date` datetime DEFAULT NULL,
  `last_email_date` datetime DEFAULT NULL,
  `user_user_ID` int(11) NOT NULL,
  `resource_resource_ID` int(11) NOT NULL,
  PRIMARY KEY (`reservation_ID`),
  KEY `fk_reservation_user1_idx` (`user_user_ID`),
  KEY `fk_reservation_resource1_idx` (`resource_resource_ID`),
  CONSTRAINT `fk_reservation_resource1` FOREIGN KEY (`resource_resource_ID`) REFERENCES `resource` (`resource_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_reservation_user1` FOREIGN KEY (`user_user_ID`) REFERENCES `user` (`user_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `resource_ID` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `year` decimal(4,0) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `company` varchar(70) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `resourceType_type_ID` int(11) NOT NULL,
  PRIMARY KEY (`resource_ID`),
  KEY `fk_resource_resourceType1_idx` (`resourceType_type_ID`),
  CONSTRAINT `fk_resource_resourceType1` FOREIGN KEY (`resourceType_type_ID`) REFERENCES `resourceType` (`type_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` VALUES (1,'Big Java',2012,'Java Programming','Pearson',1,1),(2,'Big C++',2012,'c++ Programming','Pearson',1,1),(3,'Software Engineering',2009,'Computer software','Nelson Inc',1,2);
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER
before_resource_disable
BEFORE UPDATE ON
resource
FOR EACH ROW
IF (NEW.enabled != OLD.enabled AND NEW.enabled = 0) THEN
UPDATE
resourceCopy
SET
resourceCopy.enabled = 0
WHERE
resourceCopy.resource_resource_ID = OLD.resource_ID;
END IF */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `resourceCopy`
--

DROP TABLE IF EXISTS `resourceCopy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourceCopy` (
  `barcode` int(11) NOT NULL,
  `copy_ID` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `resource_resource_ID` int(11) NOT NULL,
  `user_user_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`barcode`),
  KEY `fk_resourceCopy_resource1_idx` (`resource_resource_ID`),
  KEY `fk_resourceCopy_user1_idx` (`user_user_ID`),
  CONSTRAINT `fk_resourceCopy_resource1` FOREIGN KEY (`resource_resource_ID`) REFERENCES `resource` (`resource_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_resourceCopy_user1` FOREIGN KEY (`user_user_ID`) REFERENCES `user` (`user_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourceCopy`
--

LOCK TABLES `resourceCopy` WRITE;
/*!40000 ALTER TABLE `resourceCopy` DISABLE KEYS */;
INSERT INTO `resourceCopy` VALUES (1199,22,1,2,NULL),(1200,22,1,2,NULL),(1201,22,1,2,NULL),(1300,33,1,3,NULL),(1301,33,1,3,NULL),(1302,33,1,3,NULL);
/*!40000 ALTER TABLE `resourceCopy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourceType`
--

DROP TABLE IF EXISTS `resourceType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourceType` (
  `type_ID` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(45) NOT NULL,
  `creator_heading` varchar(70) NOT NULL,
  `company_heading` varchar(70) NOT NULL,
  `fine_amount` decimal(4,2) NOT NULL,
  `student_loan` int(11) NOT NULL,
  `faculty_loan` int(11) NOT NULL,
  `staff_loan` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`type_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourceType`
--

LOCK TABLES `resourceType` WRITE;
/*!40000 ALTER TABLE `resourceType` DISABLE KEYS */;
INSERT INTO `resourceType` VALUES (1,'Science','Pearson','Pearson Publishing Inc',5.00,7,14,30,1),(2,'Art','Pinguin','Pinguin Inc',5.00,7,14,30,1),(5,'Fiction','Pinguin','Pinguin Inc',5.00,7,14,30,1),(6,'History','Tom','Pearson',5.00,7,14,30,1);
/*!40000 ALTER TABLE `resourceType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `subscription_ID` int(11) NOT NULL AUTO_INCREMENT,
  `subscription_name` varchar(70) NOT NULL,
  `start_date` date DEFAULT NULL,
  `expire_date` date DEFAULT NULL,
  `contact_phone` varchar(15) DEFAULT NULL,
  `contact_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`subscription_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `toDo`
--

DROP TABLE IF EXISTS `toDo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `toDo` (
  `todo_ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `due_date` datetime DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `user_user_ID` int(11) NOT NULL,
  `toDocol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`todo_ID`),
  KEY `fk_toDo_user_idx` (`user_user_ID`),
  CONSTRAINT `fk_toDo_user` FOREIGN KEY (`user_user_ID`) REFERENCES `user` (`user_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `toDo`
--

LOCK TABLES `toDo` WRITE;
/*!40000 ALTER TABLE `toDo` DISABLE KEYS */;
/*!40000 ALTER TABLE `toDo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_ID` int(11) NOT NULL DEFAULT '100000000',
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `password` char(32) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `type` varchar(10) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (100000000,'Mark','Simpson','1111','mark@gmail.com','6047253344','student',1),(100000001,'Abie','John','1111','abie@yahoo.com','7789990011','student',1),(100000002,'Akoya','Akosa','1111','akoya12@gmail.ca','6049992211','student',1),(100000003,'Amanda','Lianson','2222','amandalia@yahoo.com','6041123322','staff',1),(100000005,'John','Smith','1111','smith96@gmail.com','7781120099','student',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-20  8:51:58
