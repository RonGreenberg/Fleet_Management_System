-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: fleet_management_system
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `airplanes`
--

DROP TABLE IF EXISTS `airplanes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `airplanes` (
  `planeID` varchar(7) NOT NULL,
  `model` varchar(45) NOT NULL,
  `dateAdded` date NOT NULL,
  `lastPosition` varchar(45) DEFAULT NULL,
  `lastHeading` double DEFAULT NULL,
  `lastAltitude` double DEFAULT NULL,
  PRIMARY KEY (`planeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `airplanes`
--

LOCK TABLES `airplanes` WRITE;
/*!40000 ALTER TABLE `airplanes` DISABLE KEYS */;
INSERT INTO `airplanes` VALUES ('AF1','Cessna 172P','2022-06-21','30.772717;34.65305',63.99183139,905.52351),('CALSIGN','Cessna 172P','2022-06-11','29.553703;34.9555',13,141.817459),('TEST','Cessna 172P','2022-06-15','32.01013548;35.01374632',97.48068007,2883.555284),('ZION1','Cessna 172P','2022-06-19','32.816444;35.041694',32.816444,1000);
/*!40000 ALTER TABLE `airplanes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flights`
--

DROP TABLE IF EXISTS `flights`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flights` (
  `flightID` int NOT NULL AUTO_INCREMENT,
  `planeID` varchar(7) NOT NULL,
  `csvFileName` varchar(80) NOT NULL,
  PRIMARY KEY (`flightID`),
  KEY `planeID_idx` (`planeID`),
  CONSTRAINT `planeID` FOREIGN KEY (`planeID`) REFERENCES `airplanes` (`planeID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flights`
--

LOCK TABLES `flights` WRITE;
/*!40000 ALTER TABLE `flights` DISABLE KEYS */;
INSERT INTO `flights` VALUES (1,'CALSIGN','D:\\Fleet_Management_System\\backend\\flight_files\\CALSIGN_17.06.2022_12.29.csv'),(2,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_13.05.csv'),(3,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_13.32.csv'),(4,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_14.19.csv'),(5,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_14.40.csv'),(6,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_14.46.csv'),(7,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_14.58.csv'),(8,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_15.05.csv'),(9,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_15.19.csv'),(10,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_15.53.csv'),(11,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_16.34.csv'),(12,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_18.02.csv'),(13,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_18.16.csv'),(14,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_19.28.csv'),(15,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_17.06.2022_19.42.csv'),(16,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_18.06.2022_01.43.csv'),(17,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_18.06.2022_02.05.csv'),(18,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_18.06.2022_02.12.csv'),(19,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_18.06.2022_02.31.csv'),(20,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_18.06.2022_02.37.csv'),(21,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_18.06.2022_02.54.csv'),(22,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_18.06.2022_03.00.csv'),(23,'TEST','D:\\Fleet_Management_System\\backend\\flight_files\\TEST_19.06.2022_20.35.csv');
/*!40000 ALTER TABLE `flights` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-21 12:25:00
