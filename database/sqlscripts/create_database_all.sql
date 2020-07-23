CREATE DATABASE IF NOT EXISTS `dbcustomer`;

USE `dbcustomer`;

--
-- Table structure for table `tcustomer`
--

DROP TABLE IF EXISTS `tcustomer`;

CREATE TABLE `tcustomer` (
  `_id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `customername` varchar(50) NOT NULL DEFAULT 'customer_auto',
  `customeraddress` varchar(255) DEFAULT NULL,
  `customerphone` varchar(20) DEFAULT '88776655',
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifieddate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `createdby` varchar(255) DEFAULT 'SYS',
  `lastmodifiedby` varchar(255) DEFAULT 'SYS',
  PRIMARY KEY (`_id`,`customername`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `tevents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tevents` (
  `_id` smallint NOT NULL AUTO_INCREMENT,
  `eventsource` varchar(400) DEFAULT '',
  `eventdestination` varchar(400) DEFAULT '',
  `eventdata` json DEFAULT NULL,
  `eventstatus` tinyint DEFAULT '0',
  `eventdirection` tinyint DEFAULT '-1',
  `createdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `createdby` varchar(255) DEFAULT 'SYS',
  PRIMARY KEY (`_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
