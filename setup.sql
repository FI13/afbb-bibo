-- MySQL dump 10.16  Distrib 10.1.10-MariaDB, for Linux (i686)
--
-- Host: localhost    Database: afbbbibo
-- ------------------------------------------------------
-- Server version	10.1.10-MariaDB-log

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
-- Table structure for table `ausleiher`
--

DROP TABLE IF EXISTS `ausleiher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ausleiher` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Vorname` varchar(100) DEFAULT NULL,
  `Nachname` varchar(100) NOT NULL,
  `Info` varchar(50) DEFAULT NULL,
  `EMail` varchar(255) DEFAULT NULL,
  `Telefon` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ausleiher`
--

LOCK TABLES `ausleiher` WRITE;
/*!40000 ALTER TABLE `ausleiher` DISABLE KEYS */;
/*!40000 ALTER TABLE `ausleiher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `benutzer`
--

DROP TABLE IF EXISTS `benutzer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `benutzer` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Hash` varchar(255) NOT NULL,
  `Salt` varchar(255) NOT NULL,
  `Willkommen` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `benutzer`
--

LOCK TABLES `benutzer` WRITE;
/*!40000 ALTER TABLE `benutzer` DISABLE KEYS */;
INSERT INTO `benutzer` VALUES (1,'admin','54840728b1d0d3584c9b29f4e03fcdc021e4df4e','896dde685a6fab586537f3f8ffbdfa26f92e0ff3',1);
/*!40000 ALTER TABLE `benutzer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exemplar`
--

DROP TABLE IF EXISTS `exemplar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exemplar` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Edition` varchar(45) DEFAULT NULL,
  `Barcode` varchar(45) NOT NULL,
  `Inventarisiert` datetime NOT NULL,
  `Zustand` text,
  `AusleihDatum` datetime DEFAULT NULL,
  `LetztesAusleihDatum` datetime DEFAULT NULL,
  `AusleihBenutzerId` int(11) DEFAULT NULL,
  `LetzterAusleihBenutzerId` int(11) DEFAULT NULL,
  `AusleiherId` int(11) DEFAULT NULL,
  `LetzterAusleiherId` int(11) DEFAULT NULL,
  `GruppenId` int(11) DEFAULT NULL,
  `MedienId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Barcode` (`Barcode`),
  KEY `MedienId_idx` (`MedienId`),
  KEY `GruppenId_idx` (`GruppenId`),
  KEY `AusleihBenutzerId_idx` (`AusleihBenutzerId`),
  KEY `LetzterAusleihBenutzerId_idx` (`LetzterAusleihBenutzerId`),
  KEY `AusleiherId_idx` (`AusleiherId`),
  KEY `LetzterAusleiherId_idx` (`LetzterAusleiherId`),
  CONSTRAINT `AusleihBenutzerId` FOREIGN KEY (`AusleihBenutzerId`) REFERENCES `benutzer` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `AusleiherId` FOREIGN KEY (`AusleiherId`) REFERENCES `ausleiher` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `GruppenId` FOREIGN KEY (`GruppenId`) REFERENCES `gruppe` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `LetzterAusleihBenutzerId` FOREIGN KEY (`LetzterAusleihBenutzerId`) REFERENCES `benutzer` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `LetzterAusleiherId` FOREIGN KEY (`LetzterAusleiherId`) REFERENCES `ausleiher` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `MedienId` FOREIGN KEY (`MedienId`) REFERENCES `medium` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5123 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exemplar`
--

LOCK TABLES `exemplar` WRITE;
/*!40000 ALTER TABLE `exemplar` DISABLE KEYS */;
/*!40000 ALTER TABLE `exemplar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gruppe`
--

DROP TABLE IF EXISTS `gruppe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gruppe` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gruppe`
--

LOCK TABLES `gruppe` WRITE;
/*!40000 ALTER TABLE `gruppe` DISABLE KEYS */;
/*!40000 ALTER TABLE `gruppe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medium`
--

DROP TABLE IF EXISTS `medium`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `medium` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `ISBN` varchar(20) DEFAULT NULL,
  `Titel` varchar(255) NOT NULL,
  `Autor` varchar(255) DEFAULT NULL,
  `Herausgeber` varchar(255) DEFAULT NULL,
  `Sprache` varchar(10) DEFAULT NULL,
  `TypId` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `ISBN_UNIQUE` (`ISBN`),
  KEY `TypId_idx` (`TypId`),
  CONSTRAINT `TypId` FOREIGN KEY (`TypId`) REFERENCES `typ` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medium`
--

LOCK TABLES `medium` WRITE;
/*!40000 ALTER TABLE `medium` DISABLE KEYS */;
/*!40000 ALTER TABLE `medium` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typ`
--

DROP TABLE IF EXISTS `typ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typ` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `TypName` varchar(45) NOT NULL,
  `Icon` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `TypName_UNIQUE` (`TypName`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typ`
--

LOCK TABLES `typ` WRITE;
/*!40000 ALTER TABLE `typ` DISABLE KEYS */;
INSERT INTO `typ` VALUES (1,'Buch','BOOK'),(2,'Cd','CD');
/*!40000 ALTER TABLE `typ` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-15  9:03:49
