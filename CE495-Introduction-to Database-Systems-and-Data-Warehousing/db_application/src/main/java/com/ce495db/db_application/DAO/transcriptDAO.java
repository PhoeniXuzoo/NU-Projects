package com.ce495db.db_application.DAO;

import com.ce495db.db_application.Domain.courseDetails;
import com.ce495db.db_application.Domain.courseGrade;
import com.ce495db.db_application.Domain.currentCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class transcriptDAO {

    @Autowired
    JdbcTemplate template;

    public List<courseGrade> getTranscriptById(String username, int currentYear, String currentSemester) {
        List<courseGrade> list = template.query("select * from transcript natural join unitofstudy where StudId = " + username, (rs, rowNum) -> new courseGrade(
                rs.getInt("StudId"),
                rs.getString("transcript.UoSCode"),
                rs.getString("unitofstudy.uosname"),
                rs.getString("Semester"),
                rs.getInt("Year"),
                rs.getString("Grade")
        ));

        for (courseGrade cg : list) {
            if (cg.getGrade() == null && ((cg.getYear() < currentYear) || (cg.getYear() == currentYear && (cg.getSemester().compareTo(currentSemester) < 0 ? Boolean.TRUE : Boolean.FALSE))))
                cg.setGrade("Not Pass");
        }

        return list;
    }

    public courseDetails courseDetails(String username, String uoSCode, String courseYear, String courseSemester, int currentYear, String currentSemester) {
        String sql = "select transcript.UoSCode, transcript.semester, transcript.year, transcript.grade, uosoffering.enrollment, uosoffering.maxenrollment, unitofstudy.uosname, faculty.name " +
                "from transcript, uosoffering, unitofstudy, faculty " +
                "where transcript.UoSCode = uosoffering.uoscode and transcript.year = uosoffering.year " +
                "and transcript.semester = uosoffering.semester and transcript.uoscode = unitofstudy.uoscode " +
                "and uosoffering.uoscode = unitofstudy.uoscode and uosoffering.instructorid = faculty.id " +
                "and transcript.uoscode = ? and transcript.studid = ? and transcript.semester = ? and transcript.year = ?";



        courseDetails result =  template.queryForObject(sql, new Object[] {uoSCode, username, courseSemester, courseYear}, (resultSet, i) -> new courseDetails(
                        resultSet.getString("transcript.UoSCode"),
                        resultSet.getString("transcript.semester"),
                        resultSet.getInt("transcript.year"),
                        resultSet.getString("transcript.grade"),
                        (Integer) resultSet.getObject("uosoffering.enrollment"),
                        (Integer) resultSet.getObject("uosoffering.maxenrollment"),
                        resultSet.getString("unitofstudy.uosname"),
                        resultSet.getString("faculty.name")
        ));

        if (result.getGrade() == null && ((result.getYear() < currentYear) || (result.getYear() == currentYear && (result.getSemester().compareTo(currentSemester) < 0 ? Boolean.TRUE : Boolean.FALSE))))
            result.setGrade("Not Pass");

        return result;
    }

    public List<currentCourse> getCurrentCourseById(String username, int currentYear, String currentQuarter) {
        String sql = "select UoSCode, UoSName, Semester, Year " +
                " from transcript natural join unitofstudy " +
                " where studid = ? and semester = ? and Year = ? and Grade is null ";

        return template.query(sql, new Object[] {username, currentQuarter, currentYear}, (rs, rowNum) -> new currentCourse(
                rs.getString("UoSCode"),
                rs.getString("UoSName"),
                rs.getString("Semester"),
                rs.getInt("Year")
        ));
    }
}
