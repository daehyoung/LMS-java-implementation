-- -----------------------------------------------------
-- Sample queries, created for the SDD but can still be used in implementation
-- 
-- @author Peter Abelseth
-- @version 1
-- -----------------------------------------------------

-- Used for a general search of a Resource
SELECT
	IF(title LIKE '%SEARCHTERM%', 2,
		IF(description LIKE '%SEARCHTERM%', 1,0)
	) AS title_first,
	resource.resource_ID, resource.title, resource.year, resource.description, resource.company, resource.resourceType_type_ID, creatorHasResource.creator_creator_ID
FROM
	resource, creatorHasResource
WHERE
	resource.enabled = TRUE AND
	(resource.title LIKE '%SEACHTERM%' OR
	resource.description LIKE '%SEARCHTERM%') AND
	creatorHasResource.resource_resource_ID = resource.resource_ID
ORDER BY
	title_first;

-- Used for a specific search of a resource
SELECT 
	resource.resource_ID, resource.title, resource.year, resource.description, resource.company, resource.resourceType_type_ID, creatorHasResource.creator_creator_ID
FROM
	resource, creatorHasResource, creator, resourceType
WHERE
	resource.enabled = TRUE AND
	resource.resource_ID = creatorHasResource.resource_resource_ID AND
	resource.title LIKE '%TITLESEARCH%' AND
	resource.description LIKE '%DESCSEARCH%' AND
	resource.resourceType_type_ID = 999 AND
	creator.creator_ID = creatorHasResource.creator_creator_ID AND
	creator.first_name LIKE '%FIRSTNSEARCH%' AND
	creator.last_name LIKE '%LASTNSEARCH%';

-- Used for logging a user in
SELECT 
	user_ID, first_name, last_name, email, phone, type
FROM 
	user
WHERE
	enabled = TRUE AND
	user_ID = 999999999
	password = MD5('MyPassword');

-- Used for getting all current loans of a user
SELECT
	loan.loan_ID, loan.check_out_date, loan.due_date, loan.check_in_date, loan.fine, loan.fine_paid, resource.title
FROM
	user, loan, resourceCopy, resource
WHERE
	user.user_ID = 999999999 AND
	user.user_ID = loan.user_user_ID AND
	loan.resourceCopy_barcode = resourceCopy.barcode AND
	resourceCopy.resource_resource_ID = resource.resource_ID;

-- Used for getting the total fines from a user
SELECT 
	user.user_ID, SUM(loan.fine)
FROM 
	user, loan
WHERE
	user.user_ID = 999999999 AND
	user.user_ID = loan.user_user_ID AND
	loan.fine_paid = false;