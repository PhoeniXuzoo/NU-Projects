use	 `project3-nudb`;

drop procedure if exists prerequisiteNum;

DELIMITER //
create procedure prerequisiteNum (IN courseCode char(8), IN currentDate Date)
begin
	select prerequoscode from requires where UoSCode = courseCode and currentDate > enforcedsince;
end; //

DELIMITER ;