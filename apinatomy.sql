-- MySQL dump 10.13  Distrib 5.1.37, for Win32 (ia32)
--
-- Host: localhost    Database: apinatomy_db_test
-- ------------------------------------------------------
-- Server version	5.1.37-community

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
-- Table structure for table `arterial_network`
--

DROP TABLE IF EXISTS `arterial_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arterial_network` (
  `VESSEL_FROM` bigint(20) DEFAULT NULL,
  `VESSEL_TO` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arterial_network`
--

LOCK TABLES `arterial_network` WRITE;
/*!40000 ALTER TABLE `arterial_network` DISABLE KEYS */;
/*!40000 ALTER TABLE `arterial_network` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `body_compartment_contents`
--

DROP TABLE IF EXISTS `body_compartment_contents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `body_compartment_contents` (
  `COMPARTMENT_ID` int(11) DEFAULT NULL,
  `FMA_ID` bigint(20) DEFAULT NULL,
  UNIQUE KEY `COMPARTMENT_ID` (`COMPARTMENT_ID`,`FMA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `body_compartment_contents`
--

LOCK TABLES `body_compartment_contents` WRITE;
/*!40000 ALTER TABLE `body_compartment_contents` DISABLE KEYS */;
/*!40000 ALTER TABLE `body_compartment_contents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `body_compartment_description`
--

DROP TABLE IF EXISTS `body_compartment_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `body_compartment_description` (
  `COMPARTMENT_ID` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  UNIQUE KEY `COMPARTMENT_ID` (`COMPARTMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `body_compartment_description`
--

LOCK TABLES `body_compartment_description` WRITE;
/*!40000 ALTER TABLE `body_compartment_description` DISABLE KEYS */;
/*!40000 ALTER TABLE `body_compartment_description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branching_order`
--

DROP TABLE IF EXISTS `branching_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `branching_order` (
  `MAIN_VESSEL` bigint(20) DEFAULT NULL,
  `BRANCH` bigint(20) DEFAULT NULL,
  `SEQUENCE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branching_order`
--

LOCK TABLES `branching_order` WRITE;
/*!40000 ALTER TABLE `branching_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `branching_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `constitutional_parts`
--

DROP TABLE IF EXISTS `constitutional_parts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `constitutional_parts` (
  `MAIN_STRUCTURE_ID` bigint(20) DEFAULT NULL,
  `CONSTITUTIONAL_PART_ID` bigint(20) DEFAULT NULL,
  UNIQUE KEY `MAIN_STRUCTURE_ID` (`MAIN_STRUCTURE_ID`,`CONSTITUTIONAL_PART_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `constitutional_parts`
--

LOCK TABLES `constitutional_parts` WRITE;
/*!40000 ALTER TABLE `constitutional_parts` DISABLE KEYS */;
/*!40000 ALTER TABLE `constitutional_parts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `microcirculations`
--

DROP TABLE IF EXISTS `microcirculations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `microcirculations` (
  `VESSEL` bigint(20) DEFAULT NULL,
  `ORGAN` bigint(20) DEFAULT NULL,
  `TYPE` char(3) DEFAULT NULL,
  `ISFMA` char(1) DEFAULT NULL,
  UNIQUE KEY `VESSEL` (`VESSEL`,`ORGAN`,`TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `microcirculations`
--

LOCK TABLES `microcirculations` WRITE;
/*!40000 ALTER TABLE `microcirculations` DISABLE KEYS */;
/*!40000 ALTER TABLE `microcirculations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regional_parts`
--

DROP TABLE IF EXISTS `regional_parts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `regional_parts` (
  `MAIN_STRUCTURE_ID` bigint(20) DEFAULT NULL,
  `REGIONAL_PART_ID` bigint(20) DEFAULT NULL,
  UNIQUE KEY `MAIN_STRUCTURE_ID` (`MAIN_STRUCTURE_ID`,`REGIONAL_PART_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regional_parts`
--

LOCK TABLES `regional_parts` WRITE;
/*!40000 ALTER TABLE `regional_parts` DISABLE KEYS */;
/*!40000 ALTER TABLE `regional_parts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `structure_subclass`
--

DROP TABLE IF EXISTS `structure_subclass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `structure_subclass` (
  `MAIN_STRUCTURE_ID` bigint(20) DEFAULT NULL,
  `CHILD_STRUCTURE_ID` bigint(20) DEFAULT NULL,
  UNIQUE KEY `MAIN_STRUCTURE_ID` (`MAIN_STRUCTURE_ID`,`CHILD_STRUCTURE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `structure_subclass`
--

LOCK TABLES `structure_subclass` WRITE;
/*!40000 ALTER TABLE `structure_subclass` DISABLE KEYS */;
/*!40000 ALTER TABLE `structure_subclass` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `structures`
--

DROP TABLE IF EXISTS `structures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `structures` (
  `FMA_ID` bigint(20) NOT NULL DEFAULT '0',
  `STRUCTURE_NAME` varchar(740) DEFAULT NULL,
  `TYPE` varchar(300) DEFAULT NULL,
  `DEFINITION` blob,
  PRIMARY KEY (`FMA_ID`),
  UNIQUE KEY `FMA_ID_2` (`FMA_ID`,`STRUCTURE_NAME`),
  KEY `FMA_ID` (`FMA_ID`,`STRUCTURE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `structures`
--

LOCK TABLES `structures` WRITE;
/*!40000 ALTER TABLE `structures` DISABLE KEYS */;
/*!40000 ALTER TABLE `structures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vascular_circuits`
--

DROP TABLE IF EXISTS `vascular_circuits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vascular_circuits` (
  `VESSEL` bigint(20) DEFAULT NULL,
  `TYPE` char(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vascular_circuits`
--

LOCK TABLES `vascular_circuits` WRITE;
/*!40000 ALTER TABLE `vascular_circuits` DISABLE KEYS */;
/*!40000 ALTER TABLE `vascular_circuits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vascular_segments`
--

DROP TABLE IF EXISTS `vascular_segments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vascular_segments` (
  `ID` int(11) NOT NULL,
  `FMA_ID` int(11) DEFAULT NULL,
  `startNode` varchar(12) DEFAULT NULL,
  `endNode` varchar(12) DEFAULT NULL,
  `label` text,
  `type` int(11) DEFAULT NULL,
  `startFMAID` int(11) DEFAULT NULL,
  `endFMAID` int(11) DEFAULT NULL,
  `startFMAIDType` int(11) DEFAULT NULL,
  `endFMAIDType` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vascular_segments`
--

LOCK TABLES `vascular_segments` WRITE;
/*!40000 ALTER TABLE `vascular_segments` DISABLE KEYS */;
/*!40000 ALTER TABLE `vascular_segments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venous_network`
--

DROP TABLE IF EXISTS `venous_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `venous_network` (
  `VESSEL_FROM` bigint(20) DEFAULT NULL,
  `VESSEL_TO` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venous_network`
--

LOCK TABLES `venous_network` WRITE;
/*!40000 ALTER TABLE `venous_network` DISABLE KEYS */;
/*!40000 ALTER TABLE `venous_network` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-07-23 15:43:42
