package com.ce495db.db_application.Service;

import com.ce495db.db_application.Domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApplicationService {
    List<student> getAllstudents();

    Boolean login(String username, String password);

    student findById(int id);

    Boolean updateAddress(String username, String newAddress);

    Boolean updatePassword(String username, String newPassword);

    List<courseGrade> getTranscriptById(String username, int currentYear, String currentQuarter);

    courseDetails courseDetails(String username, String uoSCode, String courseYear, String courseSemester, int currentYear, String currentSemester);

    List<currentCourse> getCurrentCourseById(String username, int currentYear, String currentQuarter);

    List<courseOffering> getCourseOfferingById(String username, int currentYear, String currentQuarter);

    List<String> enrollCourse(String username, String courseCode, int courseYear, String courseSemester, int currentYear, String currentSemester, String currentDate);

    List<String> withdrawCourse(String username, String courseCode, String courseYear, String courseSemester);

    List<courseGrade> currentEnroll(String username, int currentYear, String currentQuarter);

    /*test transaction*/
    Boolean updateAddresses(String username, String newAddress1, String newAddress2);
}
