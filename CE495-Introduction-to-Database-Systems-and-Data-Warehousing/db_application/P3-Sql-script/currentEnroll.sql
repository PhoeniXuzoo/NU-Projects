use `project3-nudb`;

DELIMITER //

drop procedure if exists currentEnroll;

create procedure currentEnroll(IN id int, IN currentYear int, IN currentSemester char(2))
begin
	select studid, uoscode, uosname, semester, year, grade
    from transcript natural join unitofstudy
    where studid = id and grade is null and ((year = currentYear and semester >= currentSemester) || year > currentYear);
end; //

DELIMITER ;