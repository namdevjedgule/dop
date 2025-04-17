-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dop
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscription` (
  `subscription_id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `days` bigint DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `project` varchar(255) DEFAULT NULL,
  `row_count` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `sub_name_master_id` bigint DEFAULT NULL,
  PRIMARY KEY (`subscription_id`),
  KEY `FKjy6hnbis2ps2f5hqka8w1ysoy` (`sub_name_master_id`),
  CONSTRAINT `FKjy6hnbis2ps2f5hqka8w1ysoy` FOREIGN KEY (`sub_name_master_id`) REFERENCES `sub_name_master` (`subscription_name_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
INSERT INTO `subscription` VALUES (1,'namdev@gmail.com','2025-04-14 17:49:10.728000',17,5000,'5','500','Active','namdev@gmail.com','2025-04-14 17:53:50.391000',2),(2,'sachin123@gmail.com','2025-04-14 22:10:10.729000',18,60000,'3','700','Active',NULL,NULL,1),(4,'namdev@gmail.com','2025-04-16 16:03:09.790000',25,35000,'20','450','Active',NULL,NULL,3),(5,'namdev@gmail.com','2025-04-16 18:35:43.640000',13,65000,'5','300','Inactive',NULL,NULL,4),(6,'namdev@gmail.com','2025-04-16 18:42:45.523000',17,36000,'7','350','Active',NULL,NULL,3),(8,'prashant@gmail.com','2025-04-17 00:56:46.575757',25,56000,'13','550','Active','namdev@gmail.com','2025-04-17 08:24:14.647920',3),(9,'vaibhav@gmail.com','2025-04-17 01:03:34.666854',16,12000,'4','470','Active',NULL,NULL,2);
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-17  9:27:58
