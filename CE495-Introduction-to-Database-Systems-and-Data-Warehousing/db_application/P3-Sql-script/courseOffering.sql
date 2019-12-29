use	 `project3-nudb`;

drop procedure if exists courseOffering;

DELIMITER //
create procedure courseOffering (IN studid int, IN currentYear int, IN currentQuarter char(2))
begin
-- 	select uoscode, uosname, semester, year, enrollment, maxenrollment
--    from uosoffering as A natural join unitofstudy
--    where ((semester >= currentQuarter and Year = currentYear) or (Year > currentYear)) 
--    and not exists (select * from transcript as B 
-- 	where B.studid = studid and A.semester = B.semester and A.year = B.year and A.uoscode = B.uoscode and B.grade is null and B.semester = currentQuarter and B.year = currentYear);

	DECLARE nextYear int;
    
    set nextYear = currentYear + 1;
    if currentQuarter = "Q1" then
		select uoscode, uosname, semester, year, enrollment, maxenrollment
		from uosoffering as A natural join unitofstudy
		where semester >= currentQuarter and Year = currentYear;
	ELSE 
		select uoscode, uosname, semester, year, enrollment, maxenrollment
		from uosoffering as A natural join unitofstudy
		where ((semester = currentQuarter and Year = currentYear) or (Year = nextYear and semester = "Q1"));
	end if;
end; //

DELIMITER ;