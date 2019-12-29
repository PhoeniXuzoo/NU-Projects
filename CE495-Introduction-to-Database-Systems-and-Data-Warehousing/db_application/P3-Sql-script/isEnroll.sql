use	 `project3-nudb`;

drop procedure if exists isEnroll;

DELIMITER //

create procedure isEnroll(IN id int, IN courseCode char(8), IN currentYear int, IN currentSemester char(2), OUT result int)
begin
	DECLARE notPass int;
    
    set notPass = (select count(*) from transcript where studid = id and uoscode = courseCode and grade is null and ((year < currentYear) or (year = currentYear and semester < currentSemester)));
	
    if notPass > 0 Then
		set result = -1;
	else
		set result = (select count(*) from transcript where studid = id and uoscode = courseCode);
	end if;
end; //

DELIMITER ;