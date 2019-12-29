use `project3-nudb`;
DELIMITER //

DROP TRIGGER IF EXISTS belowHalf;
CREATE TRIGGER belowHalf
AFTER UPDATE 
ON uosoffering FOR EACH ROW
BEGIN
	DECLARE emt int;
    DECLARE twoEmt int;
    DECLARE mEmt int;
    DECLARE msg char(100);
    
    set twoEmt = (select enrollment * 2 from uosoffering where uoscode = new.uoscode and year = new.year and semester = new.semester);
    set mEmt = (select maxenrollment from uosoffering where uoscode = new.uoscode and year = new.year and semester = new.semester);
	
    if twoEmt < mEmt THEN
		set @isBelowHalf = 1;
 -- 		set msg = 'Enrollment goes below half of max enrollment.';
-- 		SIGNAL SQLSTATE '19495' SET MESSAGE_TEXT=msg;
 	end if;
END; //

DELIMITER ;
