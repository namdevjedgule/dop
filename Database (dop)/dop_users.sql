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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `profile_photo` varchar(512) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updatedby` varchar(255) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `member_since` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `designation_id` bigint DEFAULT NULL,
  `about_us_id` int DEFAULT NULL,
  `country_id` bigint DEFAULT NULL,
  `phone_no` varchar(255) DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_users_role` (`role_id`),
  KEY `FKb7m9fimqqt96i85ufhfn4gx2k` (`designation_id`),
  KEY `FK5umucqo009p1e2hmmfahsgmx4` (`about_us_id`),
  KEY `FK5t4yyo3f0ctxayh7voqv51fmg` (`country_id`),
  KEY `FKbwv4uspmyi7xqjwcrgxow361t` (`company_id`),
  CONSTRAINT `FK5t4yyo3f0ctxayh7voqv51fmg` FOREIGN KEY (`country_id`) REFERENCES `country` (`country_id`),
  CONSTRAINT `FK5umucqo009p1e2hmmfahsgmx4` FOREIGN KEY (`about_us_id`) REFERENCES `about_us` (`Id`),
  CONSTRAINT `FK_users_role` FOREIGN KEY (`role_id`) REFERENCES `role_master` (`role_id`),
  CONSTRAINT `FKb7m9fimqqt96i85ufhfn4gx2k` FOREIGN KEY (`designation_id`) REFERENCES `designation_master` (`designation_id`),
  CONSTRAINT `FKbwv4uspmyi7xqjwcrgxow361t` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (15,3,'Namdev','Jedgule','namdev@gmail.com','$2a$10$K9QMfiLY60NzIQUWvSOlnOILRS/7c/hDK.14DqfTFtTM2KeebGl6m','/uploads/user/1744092638811_20210305171825_IMG_4096.jpg',_binary '','','2025-04-08 11:40:39',NULL,NULL,'2025-04-08 11:40:39','2025-04-17 09:04:37','Active',NULL,NULL,NULL,NULL,NULL),(30,2,'Gaurav','Gore','gaurav@gmail.com','$2a$10$u9yw.dp6Qxh0UN/OV8UuUOjTseiijO5n1enesa5Eom5NKkQNbTZGK','/uploads/user/1744254401999_IMG_0175_imresizer.jpg',_binary '','sachin123@gmail.com','2025-04-10 08:36:43',NULL,NULL,'2025-04-10 08:36:43','2025-04-10 08:36:57','Active',NULL,NULL,NULL,NULL,NULL),(36,2,'Ketan','Chandile','ketan@gmail.com','$2a$10$/5ky95acB3BgsV7ODrgPIObqzkC7/z1SvK0omYV.h9IsRi23DTIt.','/uploads/user/1744267722161_0cdd0d87-3872-4dc3-8f0e-3c33b891bcb3.jfif',_binary '\0','namdev@gmail.com','2025-04-10 12:18:43',NULL,'2025-04-10 12:18:51','2025-04-10 12:18:43','2025-04-10 12:26:02','Inactive',NULL,NULL,NULL,NULL,NULL),(37,1,'Prashant','Mazire','prashant@gmail.com','$2a$10$5Gl3zA4GllpSvDAdb/qnouwCQFF3pDIsFuNF5oiyMFFIJsVNkAEjy','/uploads/user/1744268018768_20201012084659_IMG_0097.JPG',_binary '\0','namdev@gmail.com','2025-04-10 12:23:39',NULL,'2025-04-11 20:18:49','2025-04-10 12:23:39','2025-04-17 00:54:58','Inactive',NULL,NULL,NULL,NULL,NULL),(38,2,'Sushant','Mazire','sushant@gmail.com','$2a$10$cEIIJCuAkDzXGjRQmQ7LJ.SejJdjZ3IH4EQX3hFZ/.UvtkIsaa6N2','/uploads/user/1744268072760_IMG_0159_imresizer.jpg',_binary '','namdev@gmail.com','2025-04-10 12:24:33',NULL,NULL,'2025-04-10 12:24:33','2025-04-12 14:24:13','Active',NULL,NULL,NULL,NULL,NULL),(39,2,'Bhushan','Gadhave','bhushan@gmail.com','$2a$10$uPKLqSaszxvzBO6GtaexdOHfH1TarbRz1xavCgEUEWeU3f.haLJg2','/uploads/user/1744598953959_IMG_0175_imresizer.jpg',_binary '',NULL,'2025-04-11 02:12:31',NULL,NULL,'2025-04-11 02:12:31','2025-04-14 08:16:44','Active',5,3,1,'8669586311',2),(40,2,'Tejas','Bhosale','tejas123@gmail.com','$2a$10$h88ZbWOsbAhKrqf3Qbr0UODNnWo4jXqU5CLg.EoT9e.NBQlAk4UxO',NULL,_binary '',NULL,'2025-04-11 02:19:41',NULL,NULL,'2025-04-11 02:19:41','2025-04-15 00:31:47','Inactive',4,2,1,'9192939495',3),(41,2,'Vivek','Sutar','vivek@gmail.com','$2a$10$YKQx0CuTshSifTXAR/OKM.GJO5ty/ncxG1P3ooffgKKTCbKbfmiCe',NULL,_binary '',NULL,'2025-04-11 08:29:50',NULL,NULL,'2025-04-11 08:29:50',NULL,'Active',6,3,1,'9091909190',4),(42,2,'Yogesh','Pawar','yogesh@gmail.com','$2a$10$cxQv5XcZ9W.ipkgE6ZsC/ODSz/ViOsudVhGtiCBjKBgs/3y/KuM3e','/uploads/user/1744707330152_0cdd0d87-3872-4dc3-8f0e-3c33b891bcb3.jfif',_binary '\0',NULL,'2025-04-11 09:17:57','namdev@gmail.com','2025-04-15 14:25:45','2025-04-11 09:17:57',NULL,'Inactive',5,3,1,'8182838485',5),(44,2,'Ajinkya','Tambe','ajinkya@gmail.com','$2a$10$9hoW00MJEVbCn8dHi7Jfp.XI9UJb6uzH0ILTIY6gXK8c67zwYPYo.','/uploads/user/1744450161486_IMG_0175_imresizer.jpg',_binary '','prashant@gmail.com','2025-04-12 14:59:22',NULL,NULL,'2025-04-12 14:59:22',NULL,'Active',NULL,NULL,NULL,NULL,NULL),(45,2,'Vaibhav','Choudhari','vaibhav@gmail.com','$2a$10$rPa7oyQkHiVWUV6Eg9YtqefLfqaHsP1y5SzmG2Q4OmhHS5k6AVKiG','/uploads/user/1744685704990_IMG_9341_imresizer (1).jpg',_binary '',NULL,'2025-04-14 08:21:14',NULL,NULL,'2025-04-14 08:21:14','2025-04-17 01:02:13','Active',4,5,1,'8989898989',6),(51,2,'Ravi','Gunjal','ravi@gmail.com','$2a$10$3KyxPHO4Qg9FuIC4J7./6OMblUBPKaNcgsPMrPdcQxWUffpuJMUje','/uploads/user/1744858521603_IMG_0159_imresizer.jpg',_binary '','namdev@gmail.com','2025-04-17 08:25:22',NULL,NULL,'2025-04-17 08:25:22',NULL,'Active',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-17  9:27:57
