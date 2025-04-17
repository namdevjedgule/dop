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
-- Table structure for table `steps`
--

DROP TABLE IF EXISTS `steps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `steps` (
  `step_id` bigint NOT NULL AUTO_INCREMENT,
  `alt_title` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `step_date` datetime(6) DEFAULT NULL,
  `step_title` varchar(255) DEFAULT NULL,
  `file_rows` bigint DEFAULT NULL,
  `plan_title` varchar(255) DEFAULT NULL,
  `plan_price` bigint DEFAULT NULL,
  `project_count` bigint DEFAULT NULL,
  PRIMARY KEY (`step_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `steps`
--

LOCK TABLES `steps` WRITE;
/*!40000 ALTER TABLE `steps` DISABLE KEYS */;
INSERT INTO `steps` VALUES (1,NULL,'This is for data profiling','DP','d964659f-3928-4a91-9de8-92b76b875f8c_download.jpg','Active','2025-03-25 19:30:01.655333','Data Profiling',NULL,NULL,NULL,NULL),(2,NULL,'This is for data modeling','DM','5b230029-4014-467b-ab20-03500be7835e_Data-Modeling.png','Inactive','2025-03-25 20:23:39.528397','Data Modeling',NULL,NULL,NULL,NULL),(3,NULL,'For data cleaning','DC','72448a50-05bf-4b6a-91fb-35fb578902cc_OIP2.jpg','Active','2025-03-26 02:17:30.542955','Data Cleansing',NULL,NULL,NULL,NULL),(4,NULL,'For data classification','DC','a42bb647-08be-43d8-8fe8-bc26fa4b1e45_classification-of-data.jpg','Active','2025-03-26 02:20:34.777859','Data Classification',NULL,NULL,NULL,NULL),(5,NULL,'This is for data analytics','DA','8cdf2dbe-2f4c-4666-bc2b-d13b0ab25eed_data_analytics.jpg','Active','2025-03-27 07:48:02.052179','Data Analytics',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `steps` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-17  9:27:56
