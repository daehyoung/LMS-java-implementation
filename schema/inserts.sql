INSERT INTO librisDB.`user` (first_name, last_name, password, email, phone, `type`, enabled) 
	VALUES ('Mark', 'Simpson', '1111', 'mark@gmail.com', '6047253344', 'student', DEFAULT);
INSERT INTO librisDB.`user` (user_ID,first_name, last_name, password, email, phone, `type`, enabled) 
	VALUES (100000001, 'Abie', 'John', '1111', 'abie@yahoo.com', '7789990011', 'student', DEFAULT);
INSERT INTO librisDB.`user` (user_ID,first_name, last_name, password, email, phone, `type`, enabled) 
	VALUES (100000002,'Akoya', 'Akosa', '1111', 'akoya12@gmail.ca', '6049992211', 'student', DEFAULT);
INSERT INTO librisDB.`user` (user_ID,first_name, last_name, password, email, phone, `type`, enabled) 
	VALUES (100000003,'Amanda', 'Lianson', '2222', 'amandalia@yahoo.com', '6041123322', 'staff', DEFAULT);
INSERT INTO librisDB.`user` (user_ID,first_name, last_name, password, email, phone, `type`, enabled) 
	VALUES (100000005,'John', 'Smith', '1111', 'smith96@gmail.com', '7781120099', 'student', DEFAULT);
	INSERT INTO librisDB.`user` (user_ID,first_name, last_name, password, email, phone, `type`, enabled) 
	VALUES (100000006,'Baraka', 'Obama', '2222', 'obama@gov.us', '92922233323', 'faculty', DEFAULT);

	
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

	
CREATE VIEW MyView AS
SELECT first_name, user_id FROM user;

SELECT first_name FROM MyView WHERE user_id = 100000006;



	
	
	
	
