/* 
innodb_flush_log_at_trx_commit = 0
innodb_flush_method = O_DIRECT
innodb_buffer_pool_size = 200M
*/

use stock;

DROP TABLE IF EXISTS `stock_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `stock_data` (
  `date` datetime DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `openPrice` decimal(10,2) DEFAULT NULL,
  `closePrice` decimal(10,2) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `growPrice` decimal(10,2) DEFAULT NULL,
  `growPercent` decimal(10,2) DEFAULT NULL,
  `lowPrice` decimal(10,2) DEFAULT NULL,
  `highPrice` decimal(10,2) DEFAULT NULL,
  `amount` decimal(12,0) DEFAULT NULL,
  `amountMoney` decimal(16,2) DEFAULT NULL,
  `exchangePercent` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idxUniq` (`code`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=9061928 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*
SELECT * FROM stock_data INTO OUTFILE 'dump/data3.txt'
     FIELDS TERMINATED BY ',' 
    LINES TERMINATED BY ';';
	*/

/* copy file to D:\MySQLData\Data\dump */



LOAD DATA  INFILE 'dump/data_out.txt' IGNORE INTO TABLE stock_data
  FIELDS TERMINATED BY ',' 
  LINES TERMINATED BY ';' (`date`, `code`, openPrice, closePrice, id, growPrice, growPercent, lowPrice, highPrice, amount, amountMoney, exchangePercent);
