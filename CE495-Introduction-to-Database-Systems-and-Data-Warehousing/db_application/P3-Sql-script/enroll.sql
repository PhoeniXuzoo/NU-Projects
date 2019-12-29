use	 `project3-nudb`;

drop procedure if exists enroll;

DELIMITER //

create procedure enroll(IN id int, IN courseCode char(8), IN courseSemester char(2), IN courseYear int)
begin
	insert into transcript values(id, courseCode, courseSemester, CourseYear, null);
    update uosoffering set enrollment = enrollment + 1 where uoscode = courseCode and semester = courseSemester and year = courseYear;
end; //

DELIMITER ;