CREATE DATABASE  IF NOT EXISTS `hdbms` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `hdbms`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: hdbms
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `addresses` (
  `adrID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cID` int(10) unsigned NOT NULL,
  `street` varchar(45) NOT NULL,
  `building` varchar(45) NOT NULL,
  PRIMARY KEY (`adrID`),
  KEY `cID` (`cID`),
  CONSTRAINT `cID` FOREIGN KEY (`cID`) REFERENCES `cities` (`cID`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,13,'Street 51','Building 546'),(2,9,'Street 69','Building 525'),(3,9,'Street 39','Building 967'),(4,11,'Street 58','Building 615'),(5,2,'Street 84','Building 574'),(6,2,'Street 3','Building 341'),(7,8,'Street 8','Building 59'),(8,6,'Street 60','Building 579'),(9,12,'Street 47','Building 700'),(10,8,'Street 62','Building 305'),(11,11,'Street 41','Building 124'),(12,11,'Street 53','Building 871'),(13,3,'Street 50','Building 59'),(14,7,'Street 86','Building 118'),(15,14,'Street 71','Building 265'),(16,11,'Street 19','Building 921'),(17,10,'Street 48','Building 540'),(18,7,'Street 46','Building 346'),(19,8,'Street 32','Building 893'),(20,6,'Street 65','Building 454'),(21,5,'Street 14','Building 496'),(22,5,'Street 73','Building 777'),(23,13,'Street 21','Building 377'),(24,5,'Street 20','Building 996'),(25,5,'Street 84','Building 334'),(26,12,'Street 60','Building 39'),(27,5,'Street 1','Building 703'),(28,4,'Street 97','Building 310'),(29,3,'Street 20','Building 295'),(30,3,'Street 12','Building 321'),(31,8,'Street 72','Building 323'),(32,7,'Street 71','Building 568'),(33,2,'Street 19','Building 934'),(34,2,'Street 45','Building 108'),(35,13,'Street 17','Building 292'),(36,10,'Street 58','Building 919'),(37,1,'Street 7','Building 869'),(38,13,'Street 60','Building 705'),(39,4,'Street 17','Building 117'),(40,9,'Street 34','Building 447'),(41,11,'Street 8','Building 58'),(42,10,'Street 58','Building 353'),(43,2,'Street 48','Building 130'),(44,7,'Street 38','Building 287'),(45,10,'Street 55','Building 348'),(46,10,'Street 78','Building 577'),(47,13,'Street 19','Building 933'),(48,13,'Street 45','Building 962'),(49,3,'Street 6','Building 143'),(50,10,'Street 86','Building 331'),(51,8,'Street 20','Building 313'),(52,4,'Street 4','Building 702'),(53,4,'Street 44','Building 972'),(54,6,'Street 21','Building 296'),(55,6,'Street 22','Building 157'),(56,8,'Street 66','Building 722'),(57,6,'Street 87','Building 312'),(58,7,'Street 90','Building 527'),(59,3,'Street 37','Building 248'),(60,12,'Street 39','Building 119'),(61,2,'Street 3','Building 310'),(62,12,'Street 28','Building 430'),(63,5,'Street 37','Building 334'),(64,8,'Street 24','Building 139'),(65,9,'Street 59','Building 173'),(66,6,'Street 57','Building 765'),(67,5,'Street 79','Building 677'),(68,14,'Street 70','Building 245'),(69,4,'Street 45','Building 630'),(70,6,'Street 61','Building 112'),(71,1,'Street 79','Building 232'),(72,10,'Street 69','Building 410'),(73,9,'Street 94','Building 448'),(74,1,'Street 54','Building 917'),(75,4,'Street 8','Building 866'),(76,3,'Street 68','Building 670'),(77,8,'Street 40','Building 107'),(78,1,'Street 27','Building 896'),(79,9,'Street 71','Building 412'),(80,3,'Street 91','Building 946'),(81,2,'Street 29','Building 691'),(82,4,'Street 93','Building 156'),(83,1,'Street 76','Building 999'),(84,10,'Street 6','Building 637'),(85,6,'Street 37','Building 671'),(86,2,'Street 75','Building 52'),(87,9,'Street 52','Building 22'),(88,7,'Street 34','Building 444'),(89,11,'Street 67','Building 575'),(90,2,'Street 65','Building 300'),(91,4,'Street 27','Building 92'),(92,11,'Street 16','Building 285');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointments` (
  `apID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `docID` int(10) unsigned NOT NULL,
  `patID` int(10) unsigned NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  PRIMARY KEY (`apID`),
  KEY `docID` (`docID`),
  KEY `patID` (`patID`),
  CONSTRAINT `docID` FOREIGN KEY (`docID`) REFERENCES `doctors` (`docID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `patID` FOREIGN KEY (`patID`) REFERENCES `patients` (`patID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,3,3,'2017-03-26','13:19:00'),(2,1,2,'2017-06-29','17:44:00'),(3,2,1,'2017-05-14','10:44:00'),(4,3,1,'2017-12-24','15:10:00'),(5,1,1,'2017-10-30','11:00:00'),(6,1,4,'2017-05-19','17:19:00'),(7,2,4,'2017-07-31','17:12:00'),(8,1,4,'2017-05-23','09:46:00'),(9,2,5,'2017-12-23','12:42:00'),(10,2,2,'2017-01-09','18:49:00'),(11,4,2,'2017-07-31','14:24:00'),(12,3,6,'2017-03-04','17:56:00'),(13,1,1,'2017-05-14','15:57:00'),(14,4,3,'2017-09-10','15:47:00'),(15,2,6,'2017-05-29','12:03:00'),(16,1,5,'2017-05-24','14:43:00'),(17,4,6,'2017-02-28','15:30:00'),(18,1,1,'2017-05-20','12:53:00'),(19,4,5,'2017-07-09','10:47:00'),(20,4,5,'2017-01-21','09:16:00'),(21,3,2,'2017-04-16','13:35:00'),(22,4,6,'2017-11-23','12:29:00'),(23,1,6,'2017-01-26','16:10:00'),(24,3,4,'2017-06-04','16:27:00'),(25,2,4,'2017-01-11','13:43:00'),(26,1,1,'2017-05-01','09:22:00'),(27,4,4,'2017-08-07','12:55:00'),(28,4,5,'2017-02-28','17:49:00'),(29,3,6,'2017-07-19','11:29:00'),(30,1,4,'2017-08-25','09:33:00'),(31,3,6,'2017-11-22','13:49:00'),(32,3,4,'2017-08-24','14:21:00'),(33,1,5,'2017-10-17','13:05:00'),(34,1,4,'2017-05-04','17:11:00'),(35,3,6,'2017-02-05','17:57:00'),(36,2,4,'2017-11-15','09:38:00'),(37,4,4,'2017-08-19','18:04:00'),(38,2,3,'2017-12-25','10:00:00'),(39,4,2,'2017-10-29','10:25:00'),(40,2,5,'2017-07-12','11:06:00'),(41,2,4,'2017-10-11','14:20:00'),(42,2,1,'2017-07-19','14:39:00'),(43,4,5,'2017-11-25','18:30:00'),(44,4,2,'2017-07-29','16:51:00'),(45,3,3,'2017-07-05','15:22:00'),(46,3,4,'2017-06-25','09:59:00'),(47,3,1,'2017-12-03','12:15:00'),(48,3,6,'2017-01-08','15:36:00'),(49,2,1,'2017-03-14','12:03:00'),(50,4,2,'2017-05-04','11:57:00'),(51,2,3,'2017-03-23','15:19:00');
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cities` (
  `cID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`cID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
INSERT INTO `cities` VALUES (1,'Ramallah'),(2,'Jenin'),(3,'Gaza'),(4,'Hebron'),(5,'Al Bireh'),(6,'Beitunia'),(7,'Bethlehem'),(8,'Jericho'),(9,'Nablus'),(10,'Qalqilya'),(11,'Rafah'),(12,'Tulkarm'),(13,'Tubas'),(14,'Rafah');
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctors` (
  `docID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adrID` int(10) unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  `gender` varchar(45) NOT NULL,
  `salary` double unsigned NOT NULL,
  `phoneNum` varchar(45) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `username` varchar(45) NOT NULL,
  `hashedPass` varchar(128) NOT NULL,
  `ID` varchar(45) NOT NULL,
  `is_deleted` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`docID`) USING BTREE,
  KEY `adrID2` (`adrID`),
  CONSTRAINT `adrID2` FOREIGN KEY (`adrID`) REFERENCES `addresses` (`adrID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,12,'Ahmad','Male',20550,'108905697','1972-02-18','ahmad1972','3b32e85dc1741a5b4db8fae2128cf3a622793a627ca319cc909df3a9e31a28acf931b1f2dae2feb6a382d267ee57d90f','1011872397',0),(2,51,'Rana','Female',19480,'44629501','1989-12-26','rana1989','62ad99967691beadfdc03cde2553a7098e7285f3d4a0883200668ecef5511f1ea669132b2d70948ef450c6f5052bfa1b','506279138',0),(3,49,'Alex','Male',22000,'60981003','1983-09-03','alex1983','1c175c646b3844234de9ee7a33433b803d93801ccd64817f4babde2fcc1f77d9e40972dbbd82838be4634b3dbda6051b','955260481',0),(4,72,'Sama','Female',26800,'77967657','1990-01-17','sama1990','c89285a1c14a606e0b3c13b41605283cd7e7985a9a2f73e59cb3f915e985470b1a0b78e7022897968becf8c1465e2193','340249889',0),(5,37,'tes','Female',5165161,'5165555565651','2018-01-03','teste','3c3750dcab54d70934516aeef6da13eee297acb275f6d91104a2f60c7b14a39ea98ae2576d3015bfbb837ea57e85fa1c','5565561555156',1);
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `managers`
--

DROP TABLE IF EXISTS `managers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managers` (
  `manID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adrID` int(10) unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  `gender` varchar(45) NOT NULL,
  `salary` double unsigned NOT NULL,
  `phoneNum` varchar(45) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `username` varchar(45) NOT NULL,
  `hashedPass` varchar(128) NOT NULL,
  `ID` varchar(45) NOT NULL,
  `is_deleted` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`manID`),
  KEY `adrID4` (`adrID`),
  CONSTRAINT `adrID4` FOREIGN KEY (`adrID`) REFERENCES `addresses` (`adrID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `managers`
--

LOCK TABLES `managers` WRITE;
/*!40000 ALTER TABLE `managers` DISABLE KEYS */;
INSERT INTO `managers` VALUES (1,77,'Salem','Male',29000,'62579376','1970-06-02','manSalem','494d880e62641eacb90c9d5d06d9b3182c999b84076aede58f41d670e42b94c3c9d8e695a00321ed2a4555ac490c799a','965338565',0),(2,92,'Lamar','Female',30000,'66747694','1979-04-26','lamar1979','da12b736e6eaa993b62912b127649a8bd450d25353f61772cff7a78668fc56c18ca13a0e55c7f498c97f3a593d63a9bb','1027487853',0),(3,1,'Bob','Male',28500,'19399375','1985-12-03','bobino','18fc1d5d1766799ef02f1596cd527eef9b8356851ab824e19aea7830b6f758ad8b7757197471729c03f04ac2cda2bcfa','389365133',0);
/*!40000 ALTER TABLE `managers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patients` (
  `patID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adrID` int(10) unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  `gender` varchar(45) NOT NULL,
  `phoneNum` varchar(45) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `ID` varchar(45) NOT NULL,
  PRIMARY KEY (`patID`),
  KEY `adrID` (`adrID`),
  CONSTRAINT `adrID` FOREIGN KEY (`adrID`) REFERENCES `addresses` (`adrID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,23,'Patient1','Male','12844826','1991-06-02','730657939'),(2,68,'Patient2','Female','7764425','1992-04-26','877978788'),(3,32,'Patient3','Male','10990721','1980-04-26','558169814'),(4,85,'Patient4','Female','13444600','1950-12-16','235507410'),(5,57,'Patient5','Male','12937440','2012-05-23','1018834122'),(6,8,'Patient6','Female','11138299','2002-10-02','883969978');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prescriptions` (
  `preID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `docID` int(10) unsigned NOT NULL,
  `patID` int(10) unsigned NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `description` varchar(450) NOT NULL,
  `totalCost` double NOT NULL,
  PRIMARY KEY (`preID`),
  KEY `docID2` (`docID`),
  KEY `patID2` (`patID`),
  CONSTRAINT `docID2` FOREIGN KEY (`docID`) REFERENCES `doctors` (`docID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `patID2` FOREIGN KEY (`patID`) REFERENCES `patients` (`patID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,1,6,'2017-02-21','14:45:00','medicines, etc..',280),(2,3,4,'2017-09-02','11:59:00','medicines, etc..',191),(3,4,6,'2017-09-12','12:14:00','medicines, etc..',253),(4,4,3,'2017-06-17','14:34:00','medicines, etc..',217),(5,2,3,'2017-02-20','16:01:00','medicines, etc..',53),(6,2,4,'2017-06-08','16:27:00','medicines, etc..',348),(7,2,3,'2017-10-16','16:45:00','medicines, etc..',52),(8,4,5,'2017-05-11','12:28:00','medicines, etc..',312),(9,3,3,'2017-02-02','15:24:00','medicines, etc..',172),(10,1,1,'2017-04-03','15:59:00','medicines, etc..',126),(11,2,5,'2017-10-26','17:53:00','medicines, etc..',113),(12,3,5,'2017-04-14','09:01:00','medicines, etc..',248),(13,2,6,'2017-01-07','16:29:00','medicines, etc..',221),(14,3,1,'2017-08-18','18:19:00','medicines, etc..',118),(15,4,4,'2017-01-11','11:01:00','medicines, etc..',199),(16,3,2,'2017-03-16','10:41:00','medicines, etc..',260),(17,4,2,'2017-07-15','14:20:00','medicines, etc..',197),(18,4,2,'2017-09-13','18:17:00','medicines, etc..',57),(19,1,3,'2017-03-02','18:16:00','medicines, etc..',314),(20,3,1,'2017-10-07','12:46:00','medicines, etc..',319),(21,3,3,'2017-05-04','16:46:00','medicines, etc..',310),(22,2,5,'2017-04-14','14:35:00','medicines, etc..',335),(23,4,4,'2017-02-12','10:56:00','medicines, etc..',195),(24,4,4,'2018-01-04','09:42:00','medicines, etc..',199),(25,3,5,'2017-12-04','15:02:00','medicines, etc..',271),(26,2,2,'2017-12-22','15:31:00','medicines, etc..',299),(27,1,4,'2017-09-19','16:15:00','medicines, etc..',330),(28,4,6,'2017-02-24','17:12:00','medicines, etc..',348),(29,1,5,'2017-10-09','16:07:00','medicines, etc..',67),(30,1,4,'2017-08-19','12:18:00','medicines, etc..',228),(31,3,4,'2017-01-13','11:35:00','medicines, etc..',199),(32,2,6,'2017-11-23','09:48:00','medicines, etc..',50);
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secretaries`
--

DROP TABLE IF EXISTS `secretaries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `secretaries` (
  `secID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adrID` int(10) unsigned NOT NULL,
  `name` varchar(45) NOT NULL,
  `gender` varchar(45) NOT NULL,
  `salary` double unsigned NOT NULL,
  `phoneNum` varchar(45) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `username` varchar(45) NOT NULL,
  `hashedPass` varchar(128) NOT NULL,
  `ID` varchar(45) NOT NULL,
  `is_deleted` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`secID`) USING BTREE,
  KEY `adrID3` (`adrID`),
  CONSTRAINT `adrID3` FOREIGN KEY (`adrID`) REFERENCES `addresses` (`adrID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secretaries`
--

LOCK TABLES `secretaries` WRITE;
/*!40000 ALTER TABLE `secretaries` DISABLE KEYS */;
INSERT INTO `secretaries` VALUES (1,58,'Sari','Male',4000,'110672808','1991-06-02','secSari','490e0d34749904ef4e60804fa22d26e7ecc1409682ade2da41e31dd4c5140f09932d80bd30ab012a74744e23c2329f2b','1024784586',0),(2,56,'Luna','Female',4200,'66335431','1992-04-26','luna1992','bcc3f6ebd635b77302ea3eb318b878fd5bfc4523e9ba8431827d0f11b69247ad72a3c4d4aa3746ca3e1f99f7bf2b598a','393450811',0);
/*!40000 ALTER TABLE `secretaries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'hdbms'
--

--
-- Dumping routines for database 'hdbms'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-04  2:44:52
