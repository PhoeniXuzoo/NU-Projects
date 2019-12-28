package com.ce495db.db_application.Service;

import com.ce495db.db_application.DAO.enrollDAO;
import com.ce495db.db_application.DAO.studentDAO;
import com.ce495db.db_application.DAO.transcriptDAO;
import com.ce495db.db_application.DAO.withdrawDAO;
import com.ce495db.db_application.Domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService{

    @Autowired
    studentDAO studentDAO;

    @Autowired
    transcriptDAO transcriptDAO;

    @Autowired
    enrollDAO enrollDAO;

    @Autowired
    withdrawDAO withdrawDAO;

    @Override
    public List<student> getAllstudents() { return studentDAO.getAllStudents(); }

    @Override
    public Boolean login(String username, String password) {
        return studentDAO.login(username, password);
    }

    @Override
    public student findById(int id) { return studentDAO.findById(id); }
    
    @Override
    public Boolean updateAddress(String username, String newAddress) { return studentDAO.updateAddress(username, newAddress); }

    @Override
    public Boolean updatePassword(String username, String newPassword) { return studentDAO.updatePassword(username, newPassword); }

    @Override
    public List<courseGrade> getTranscriptById(String username, int currentYear, String currentSemester) { return transcriptDAO.getTranscriptById(username, currentYear, currentSemester); }

    @Override
    public courseDetails courseDetails(String username, String uoSCode, String courseYear, String courseSemester, int currentYear, String currentSemester) {
        try {
            return transcriptDAO.courseDetails(username, uoSCode, courseYear, courseSemester, currentYear, currentSemester);
        }
        catch (EmptyResultDataAccessException e) {
            return new courseDetails(null, null, 0, null, null, null, null, null);
        }
    }

    @Override
    public List<currentCourse> getCurrentCourseById(String username, int currentYear, String currentQuarter) { return transcriptDAO.getCurrentCourseById(username, currentYear, currentQuarter); }

    @Override
    public List<courseOffering> getCourseOfferingById(String username, int currentYear, String currentQuarrer) { return enrollDAO.getCourseOfferingById(username, currentYear, currentQuarrer); }

    @Override
    public List<String> enrollCourse(String username, String courseCode, int courseYear, String courseSemester, int currentYear, String currentSemester, String currentDate) { return enrollDAO.enrollCourse(username, courseCode, courseYear, courseSemester, currentYear, currentSemester, currentDate); }

    @Override
    public List<String> withdrawCourse(String username, String courseCode, String courseYear, String courseSemester) { return withdrawDAO.withdrawCourse(username, courseCode, courseYear, courseSemester); }

    @Override
    public List<courseGrade> currentEnroll(String username, int currentYear, String currentQuarter) { return withdrawDAO.currentEnroll(username, currentYear, currentQuarter); }

    /*test transactional*/
    @Override
    public Boolean updateAddresses(String username, String newAddress1, String newAddress2) {
        try {
            return studentDAO.updateAddresses(username, newAddress1, newAddress2);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }
    }
}
