-- -----------------------------------------------------
-- @author Peter Abelseth
-- @version 1
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Trigger to update all resourceCopies when a resource is disabled
-- -----------------------------------------------------
DELIMITER $$
USE librisDB$$

DROP TRIGGER IF EXISTS `librisDB`.`before_resource_disable` ;
CREATE TRIGGER
	before_resource_disable
	BEFORE UPDATE ON
		resource
	FOR EACH ROW
		IF (NEW.enabled != OLD.enabled AND NEW.enabled = 0) THEN
			UPDATE
				resourceCopy
			SET
				resourceCopy.enabled = 0
			WHERE
				resourceCopy.resource_resource_ID = OLD.resource_ID;
		END IF$$
			
DELIMITER ;