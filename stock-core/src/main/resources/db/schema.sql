use stock;

-- Table "stock_meta" DDL
CREATE TABLE IF NOT EXISTS `stock_meta` (
  `code` varchar(10) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `zone` varchar(20) DEFAULT '',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `stock_data` (
  `date` DATETIME NULL,
  `code` VARCHAR(10) NULL,
  `openPrice` DECIMAL(10,2) NULL,
  `closePrice` DECIMAL(10,2) NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `growPrice` DECIMAL(10,2) NULL,
  `growPercent` DECIMAL(10,2) NULL,
  `lowPrice` DECIMAL(10,2) NULL,
  `highPrice` DECIMAL(10,2) NULL,
  `amount` DECIMAL(12,0) NULL,
  `amountMoney` DECIMAL(16,2) NULL,
  `exchangePercent` DECIMAL(10,2) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idxUniq` (`code` ASC, `date` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `stock_validate_result` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `strategy` VARCHAR(20) NULL,
  `date` DATETIME NULL,
  `total` DECIMAL(10,0) NULL,
  `passed` DECIMAL(10,0) NULL,
  `profitRate` DECIMAL(10,2) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idxUniq` (`strategy` ASC, `date` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `stock_data_update` (
  `code` varchar(10) NOT NULL,
  `updateDate` DATETIME NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
