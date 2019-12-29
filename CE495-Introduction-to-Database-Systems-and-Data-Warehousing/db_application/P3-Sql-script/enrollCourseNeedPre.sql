use	 `project3-nudb`;

drop procedure if exists enrollCourseNeedPre;

DELIMITER //

create procedure enrollCourseNeedPre(IN id int, IN courseCode char(8), IN courseYear int, IN courseQuarter char(2), IN currentDate Date, OUT isFull int)
begin
	set isFull = (select count(*) from uosoffering where uoscode = courseCode and semester = courseQuarter and year = courseYear and enrollment < maxenrollment);
    
    IF isFull > 0 Then
		select prerequoscode from requires as R
		where courseCode = UoSCode and enforcedsince < currentDate and exists (select * from transcript
		where transcript.StudId = id and R.PrereqUoSCode = transcript.UoSCode and transcript.Grade is not null) and exists (select * from uosoffering
		where uosoffering.UoSCode = courseCode and uosoffering.Semester = courseQuarter and uosoffering.year = courseYear);
    end if;
end; //

DELIMITER ;