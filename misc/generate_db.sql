-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema fleet_management_system
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema fleet_management_system
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `fleet_management_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `fleet_management_system` ;

-- -----------------------------------------------------
-- Table `fleet_management_system`.`airplanes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fleet_management_system`.`airplanes` (
  `planeID` VARCHAR(7) NOT NULL,
  `model` VARCHAR(45) NOT NULL,
  `dateAdded` DATE NOT NULL,
  `lastPosition` VARCHAR(45) NULL DEFAULT NULL,
  `lastHeading` DOUBLE NULL DEFAULT NULL,
  `lastAltitude` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`planeID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `fleet_management_system`.`flights`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `fleet_management_system`.`flights` (
  `flightID` INT NOT NULL AUTO_INCREMENT,
  `planeID` VARCHAR(7) NOT NULL,
  `csvFileName` VARCHAR(80) NOT NULL,
  PRIMARY KEY (`flightID`),
  INDEX `planeID_idx` (`planeID` ASC) VISIBLE,
  CONSTRAINT `planeID`
    FOREIGN KEY (`planeID`)
    REFERENCES `fleet_management_system`.`airplanes` (`planeID`))
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
