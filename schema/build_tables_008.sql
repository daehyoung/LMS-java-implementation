-- -----------------------------------------------------
-- @author Peter Abelseth
-- @version 8
-- -----------------------------------------------------


SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `librisDB` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `librisDB` ;

-- -----------------------------------------------------
-- Table `librisDB`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`user` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`user` (
  `user_ID` INT NOT NULL AUTO_INCREMENT ,
  `first_name` VARCHAR(45) NOT NULL ,
  `last_name` VARCHAR(45) NOT NULL ,
  `password` CHAR(32) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `phone` VARCHAR(15) NULL ,
  `type` TINYINT(3) NOT NULL ,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 ,
  PRIMARY KEY (`user_ID`) )
ENGINE = InnoDB
AUTO_INCREMENT = 100000000;


-- -----------------------------------------------------
-- Table `librisDB`.`toDo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`toDo` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`toDo` (
  `todo_ID` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(500) NULL ,
  `due_date` BIGINT(14) NULL ,
  `start_date` BIGINT(14) NULL ,
  PRIMARY KEY (`todo_ID`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `librisDB`.`resourceType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`resourceType` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`resourceType` (
  `type_ID` INT NOT NULL AUTO_INCREMENT ,
  `type_name` VARCHAR(45) NOT NULL ,
  `creator_heading` VARCHAR(70) NOT NULL ,
  `serial_heading` VARCHAR(70) NOT NULL ,
  `company_heading` VARCHAR(70) NOT NULL ,
  `fine_amount` DECIMAL(4,2) NOT NULL ,
  `student_loan` INT NOT NULL ,
  `faculty_loan` INT NOT NULL ,
  `staff_loan` INT NOT NULL ,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 ,
  PRIMARY KEY (`type_ID`) ,
  UNIQUE INDEX `type_name_UNIQUE` (`type_name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `librisDB`.`resource`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`resource` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`resource` (
  `resource_ID` INT NOT NULL AUTO_INCREMENT ,
  `serial` VARCHAR(20) NULL ,
  `title` VARCHAR(45) NOT NULL ,
  `creator` VARCHAR(300) NULL ,
  `year` DECIMAL(4,0) NULL ,
  `description` VARCHAR(500) NULL ,
  `company` VARCHAR(70) NULL ,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 ,
  `resourceType_type_ID` INT NOT NULL ,
  PRIMARY KEY (`resource_ID`) ,
  INDEX `fk_resource_resourceType1_idx` (`resourceType_type_ID` ASC) ,
  CONSTRAINT `fk_resource_resourceType1`
    FOREIGN KEY (`resourceType_type_ID` )
    REFERENCES `librisDB`.`resourceType` (`type_ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `librisDB`.`resourceCopy`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`resourceCopy` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`resourceCopy` (
  `barcode` INT NOT NULL ,
  `copy_ID` INT NOT NULL ,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1 ,
  `resource_resource_ID` INT NOT NULL ,
  `user_user_ID` INT NULL ,
  `resourceCopycol` VARCHAR(45) NULL ,
  PRIMARY KEY (`barcode`) ,
  INDEX `fk_resourceCopy_resource1_idx` (`resource_resource_ID` ASC) ,
  INDEX `fk_resourceCopy_user1_idx` (`user_user_ID` ASC) ,
  CONSTRAINT `fk_resourceCopy_resource1`
    FOREIGN KEY (`resource_resource_ID` )
    REFERENCES `librisDB`.`resource` (`resource_ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_resourceCopy_user1`
    FOREIGN KEY (`user_user_ID` )
    REFERENCES `librisDB`.`user` (`user_ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `librisDB`.`loan`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`loan` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`loan` (
  `loan_ID` INT NOT NULL AUTO_INCREMENT ,
  `check_out_date` BIGINT(14) NOT NULL ,
  `check_in_date` BIGINT(14) NULL ,
  `due_date` BIGINT(14) NOT NULL ,
  `last_email_date` BIGINT(14) NULL ,
  `fine` DECIMAL(5,2) NOT NULL DEFAULT 0 ,
  `fine_paid` TINYINT(1) NOT NULL DEFAULT 0 ,
  `user_user_ID` INT NOT NULL ,
  `resourceCopy_barcode` INT NOT NULL ,
  PRIMARY KEY (`loan_ID`) ,
  INDEX `fk_loan_user1_idx` (`user_user_ID` ASC) ,
  INDEX `fk_loan_resourceCopy1_idx` (`resourceCopy_barcode` ASC) ,
  CONSTRAINT `fk_loan_user1`
    FOREIGN KEY (`user_user_ID` )
    REFERENCES `librisDB`.`user` (`user_ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_loan_resourceCopy1`
    FOREIGN KEY (`resourceCopy_barcode` )
    REFERENCES `librisDB`.`resourceCopy` (`barcode` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `librisDB`.`reference`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`reference` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`reference` (
  `reference_ID` INT NOT NULL AUTO_INCREMENT ,
  `placed_date` BIGINT(14) NOT NULL ,
  `expire_date` BIGINT(14) NULL ,
  `user_user_ID` INT NOT NULL ,
  `resourceCopy_barcode` INT NOT NULL ,
  PRIMARY KEY (`reference_ID`) ,
  INDEX `fk_reference_user1_idx` (`user_user_ID` ASC) ,
  INDEX `fk_reference_resourceCopy1_idx` (`resourceCopy_barcode` ASC) ,
  CONSTRAINT `fk_reference_user1`
    FOREIGN KEY (`user_user_ID` )
    REFERENCES `librisDB`.`user` (`user_ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_reference_resourceCopy1`
    FOREIGN KEY (`resourceCopy_barcode` )
    REFERENCES `librisDB`.`resourceCopy` (`barcode` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `librisDB`.`reservation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`reservation` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`reservation` (
  `reservation_ID` INT NOT NULL AUTO_INCREMENT ,
  `reserved_date` BIGINT(14) NOT NULL ,
  `available_date` BIGINT(14) NULL ,
  `expire_date` BIGINT(14) NOT NULL ,
  `last_email_date` BIGINT(14) NULL ,
  `user_user_ID` INT NOT NULL ,
  `resource_resource_ID` INT NOT NULL ,
  PRIMARY KEY (`reservation_ID`) ,
  INDEX `fk_reservation_user1_idx` (`user_user_ID` ASC) ,
  INDEX `fk_reservation_resource1_idx` (`resource_resource_ID` ASC) ,
  CONSTRAINT `fk_reservation_user1`
    FOREIGN KEY (`user_user_ID` )
    REFERENCES `librisDB`.`user` (`user_ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_reservation_resource1`
    FOREIGN KEY (`resource_resource_ID` )
    REFERENCES `librisDB`.`resource` (`resource_ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `librisDB`.`subscription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `librisDB`.`subscription` ;

CREATE  TABLE IF NOT EXISTS `librisDB`.`subscription` (
  `subscription_ID` INT NOT NULL AUTO_INCREMENT ,
  `subscription_name` VARCHAR(70) NOT NULL ,
  `start_date` BIGINT(14) NULL ,
  `expire_date` BIGINT(14) NULL ,
  `contact_phone` VARCHAR(15) NULL ,
  `contact_email` VARCHAR(255) NULL ,
  PRIMARY KEY (`subscription_ID`) )
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
