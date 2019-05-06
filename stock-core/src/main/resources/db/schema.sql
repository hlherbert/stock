use stock;

-- Table "stock_meta" DDL

CREATE TABLE IF NOT EXISTS `stock_meta` (
  `code` varchar(10) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `zone` varchar(20) DEFAULT '',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
