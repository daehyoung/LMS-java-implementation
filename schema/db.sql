
insert into resource
values(1121, "Java",11,77,"2010","Describetion goes here", "Person");

insert into subject
values(11, "Computer Science");

insert into resource_type
values(77,"Book", "Creater heading blah", "Lehigh-Phoenix", "NULL","NULL");

insert into creator
values(999,"Liang", "Sang");
insert into resource_by_creator
values(999, 1121);

insert into resource_copy
values(100112, 97801370351, "copy 1");
insert into resource_copy
values(100113, 97801370351, "copy 2");
insert into resource_copy
values(100114, 97801370351, "copy 3");
insert into resource_copy
values(100116, 97801370351, "copy 4");

insert into resource_copy
values(100132, 1121, "copy 1");
insert into resource_copy
values(100133, 1121, "copy 2");
insert into resource_copy
values(100134, 1121, "copy 3");
insert into resource_copy
values(100135, 1121, "copy 4");



select barcode, title, subject_name, type_name, year, resource.company, creator.first_name, creator.last_name
from resource, resource_type, subject, creator, resource_by_creator, resource_copy
where resource.subject_ID = subject.subject_ID
and resource.type_ID = resource_type.type_ID
and resource.resource_ID = resource_by_creator.resource_ID
and resource_by_creator.creator_ID = creator.creator_ID
and resource_copy.resource_id = resource.resource_id
and resource_copy.barcode = 100132;

select barcode, resource.resource_id, title, copy_num
from resource_copy, resource
where resource_copy.resource_id = resource.resource_id
and resource.resource_id = 1121;