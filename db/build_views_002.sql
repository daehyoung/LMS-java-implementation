-- -----------------------------------------------------
-- @author Peter Abelseth
-- @version 2
-- TODO Remove the count of the number of reservations as well
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Creates a view of a resource with number of available resourceCopies and no disabled resources
-- -----------------------------------------------------
DROP VIEW `librisDB`.`resourceSearch`;
CREATE VIEW `librisDB`.`resourceSearch` AS
	SELECT
		resource.resource_ID,title,year,description,company,resourceType_type_ID,COUNT(resourceCopy.copy_ID)'available'
	FROM
		resource,resourceCopy 
			LEFT JOIN loan 
				ON resourceCopy.barcode = loan.resourceCopy_barcode
			LEFT JOIN reference
				ON resourceCopy.barcode = reference.resourceCopy_barcode
	WHERE
		resource.enabled = TRUE AND
		resourceCopy.enabled = TRUE AND
		resource.resource_ID = resourceCopy.resource_resource_ID AND
		loan.check_in_date IS NULL AND
		reference.reference_ID IS NULL
	GROUP BY resource.resource_ID;
	
