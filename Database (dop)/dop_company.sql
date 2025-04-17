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
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `company_id` bigint NOT NULL AUTO_INCREMENT,
  `cemail` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `cpassword` varchar(255) DEFAULT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `activity_id` bigint DEFAULT NULL,
  PRIMARY KEY (`company_id`),
  KEY `FK1ux0jrpgp2dcd3w06jjfamgmj` (`activity_id`),
  CONSTRAINT `FK1ux0jrpgp2dcd3w06jjfamgmj` FOREIGN KEY (`activity_id`) REFERENCES `activity_master` (`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'in2in@gmail.com','In2in global','In2in@2020','2025-03-29 00:00:00.000000','Active','namdev@gmail.com',NULL,NULL,2),(2,NULL,'Infosys',NULL,NULL,'Active',NULL,NULL,NULL,NULL),(3,NULL,'TCS',NULL,NULL,'Active','tejas123@gmail.com',NULL,NULL,NULL),(4,NULL,'Abc',NULL,NULL,'Inactive','sachin123@gmail.com',NULL,NULL,NULL),(5,NULL,'Capgemini',NULL,NULL,'Active',NULL,NULL,NULL,NULL),(6,'capgemini@gmail.com','Capg','$2a$10$ZHdC951Kg0RSTj1FORGiPuWC7HN0r29JffSzlypX6pYB6.y8H1adK','2025-04-14 00:38:49.029000','Inactive','vaibhav@gmail.com','namdev@gmail.com','2025-04-16 07:49:02.811327',2),(8,'hightech@gmail.com','HighTech Arai','$2a$10$EOFD501trxEMaJ.ouiDhCOTjV0xj1Z8JSf1NkE5dX37Neivor4SfO','2025-04-15 00:38:49.029000','Inactive','nitin@gmail.com','namdev@gmail.com','2025-04-16 23:26:36.526733',1),(12,'auras@gmail.com','Auras Tech','$2a$10$viEDFrW4PjPaKWu36NvI4.Zu49KhBvMT2jmbqmHASpVpMhcvPrQa6','2025-04-16 00:42:29.458556','Active','namdev@gmail.com','namdev@gmail.com','2025-04-16 08:06:22.162009',2),(22,'bajaj@gmail.com','Bajaj Finance','$2a$10$FB0xG5HLOWOAyLprkFjytegGC5tRZgprPLcwmwmGaNs6BXQ/U1r6G','2025-04-16 01:11:02.796194','Active','namdev@gmail.com','namdev@gmail.com','2025-04-16 08:05:59.510844',1),(24,'saama123@gmail.com','Saama Tech','$2a$10$R1xo0uDEp6Ipf329tTAr.uBzfOrhk7BuDFpssc9Mvvr9fNzv9mEcO','2025-04-16 08:07:19.326138','Active','namdev@gmail.com','namdev@gmail.com','2025-04-16 23:26:57.335659',2),(25,'cognizant@gmail.com','Cognizant','Cognizant@123','2025-04-16 08:08:16.351774','Active','namdev@gmail.com',NULL,NULL,2),(26,'kpit@gmail.com','KPIT','Kpit@123','2025-04-16 18:51:12.690318','Active','namdev@gmail.com',NULL,NULL,1),(27,'qiosk@gmail.com','Qiosk','Qiosk@123','2025-04-17 00:55:59.221703','Active','prashant@gmail.com',NULL,NULL,2);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
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
