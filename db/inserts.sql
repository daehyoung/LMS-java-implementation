INSERT INTO librisDB.resourceType (type_name, creator_heading, company_heading, fine_amount, student_loan, faculty_loan, staff_loan, enabled)  
	VALUES('Science', 'Pearson', 'Pearson Publishing Inc',5.00, 7, 14, 30, true);
	
INSERT INTO librisDB.resourceType (type_name, creator_heading, company_heading, fine_amount, student_loan, faculty_loan, staff_loan, enabled) 
	VALUES('Non-fiction', 'Pinguin', 'Pinguin Inc',5.00, 7, 14, 30, true);
	
INSERT INTO librisDB.resourceType (type_name, creator_heading, company_heading, fine_amount, student_loan, faculty_loan, staff_loan, enabled) 
	VALUES ('History', 'Tom', 'Pearson', 5.00, 7, 14, 30, true);
	
	
	
	
INSERT INTO librisDB.resource (title, `year`, description, company, enabled, resourceType_type_ID) 
	VALUES ('Big Java', 2012, 'Java Programming', 'Pearson', true, 1);
	
INSERT INTO librisDB.resource (title, `year`, description, company, enabled, resourceType_type_ID) 
	VALUES ('Software Engineering', 2009, 'Computer software', 'Nelson Inc', true, 2);
	
	

	
	
INSERT INTO librisDB.resourceCopy (barcode, copy_ID, enabled, resource_resource_ID, user_user_ID) 
	VALUES (1199, 22, true, 2, NULL);
INSERT INTO librisDB.resourceCopy (barcode, copy_ID, enabled, resource_resource_ID, user_user_ID) 
	VALUES (1200, 22, true, 2, NULL);
INSERT INTO librisDB.resourceCopy (barcode, copy_ID, enabled, resource_resource_ID, user_user_ID) 
	VALUES (1201, 22, true, 2, NULL);
INSERT INTO librisDB.resourceCopy (barcode, copy_ID, enabled, resource_resource_ID, user_user_ID) 
	VALUES (1300, 33, true, 3, NULL);
INSERT INTO librisDB.resourceCopy (barcode, copy_ID, enabled, resource_resource_ID, user_user_ID) 
	VALUES (1301, 33, true, 3, NULL);
INSERT INTO librisDB.resourceCopy (barcode, copy_ID, enabled, resource_resource_ID, user_user_ID) 
	VALUES (1302, 33, true, 3, NULL);
	
	
	
	
INSERT INTO librisDB.creator (first_name, last_name) 
	VALUES ('James', 'Liang');
INSERT INTO librisDB.creator (first_name, last_name) 
	VALUES ('Paul', 'Bailey');
INSERT INTO librisDB.creator (first_name, last_name) 
	VALUES ('Julian', 'Barnes');
INSERT INTO librisDB.creator (first_name, last_name) 
	VALUES ('Saul', 'Bellow');
	
	
	
INSERT INTO librisDB.creatorHasResource (creator_creator_ID, resource_resource_ID) 
	VALUES (1, 1);
INSERT INTO librisDB.creatorHasResource (creator_creator_ID, resource_resource_ID) 
	VALUES (2, 1);
INSERT INTO librisDB.creatorHasResource (creator_creator_ID, resource_resource_ID) 
	VALUES (3, 2);
INSERT INTO librisDB.creatorHasResource (creator_creator_ID, resource_resource_ID) 
	VALUES (4, 3);





	
	
	
	
