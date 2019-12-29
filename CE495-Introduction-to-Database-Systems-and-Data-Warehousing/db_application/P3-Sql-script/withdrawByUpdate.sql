use `project3-nudb`;

DELIMITER //

drop procedure if exists withdrawByUpdate;

create procedure withdrawByUpdate(IN id int, IN courseCode char(8), IN courseYear int, IN courseSemester char(2), OUT isBelow int)
begin 
	set isBelow = 0;
    set @isBelowHalf = 0;
	
	update uosoffering
    set enrollment = enrollment - 1
    where uoscode = courseCode and year = courseYear and semester = courseSemester;
    
    delete from transcript where studid = id and uoscode = courseCode and year = courseYear and semester = courseSemester;
    
    set isBelow = (select @isBelowHalf);
    
end; //

DELIMITER ;