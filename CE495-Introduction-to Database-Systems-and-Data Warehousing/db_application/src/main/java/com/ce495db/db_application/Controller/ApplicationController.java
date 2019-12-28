package com.ce495db.db_application.Controller;


import com.ce495db.db_application.Domain.*;
import com.ce495db.db_application.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    ApplicationService applicationService;

    private static String computeCurrentQuarter() {
        int currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        if (currentMonth <= 6) return "Q1";
        else return "Q2";
    }

    public final static int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    public final static String currentQuarter = computeCurrentQuarter();

    private static String computeCurrentDate() {
        int currentDay = Calendar.getInstance().get(Calendar.DATE);
        int currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        String currentDay_S = (currentDay < 10) ? "0" + String.valueOf(currentDay) : String.valueOf(currentDay);
        String currentMonth_S = (currentMonth < 10) ? "0" + String.valueOf(currentMonth) : String.valueOf(currentMonth);

        return String.valueOf(currentYear) + "-" + currentMonth_S + "-" + currentDay_S;
    }

    public final static String currentDate = computeCurrentDate();

    @RequestMapping(value = "/haha", method = RequestMethod.GET)
    public List<student> haha() {return applicationService.getAllstudents();}

    /*
    * Login
    */
    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.POST)
    public ResponseEntity login(@PathVariable String username, @PathVariable String password) {
        if (applicationService.login(username, password))
            return new ResponseEntity(HttpStatus.ACCEPTED);
        else return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    /*
    * StudentMenu
    */
    @RequestMapping(value = "/currentCourse/{username}", method = RequestMethod.GET)
    public List<currentCourse> getCurrentCourseById(@PathVariable String username) {
        return applicationService.getCurrentCourseById(username, currentYear, currentQuarter);
    }

    /*
    * PersonalDetials
    */
    @RequestMapping(value = "/personalDetails/{username}", method = RequestMethod.GET)
    public student findbyId(@PathVariable int username) {
        return applicationService.findById(username);
    }

    @RequestMapping(value = "/personalDetails/updateAddress/{username}", method = RequestMethod.POST)
    public ResponseEntity updateAddress(@PathVariable String username, @RequestBody newAddr newAddr) {
        if (applicationService.updateAddress(username, newAddr.getNewAddress()))
            return new ResponseEntity(HttpStatus.ACCEPTED);
        else return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping(value = "/personalDetails/updatePassword/{username}/{newPassword}", method = RequestMethod.POST)
    public ResponseEntity updatePassword(@PathVariable String username, @PathVariable String newPassword) {
        if (applicationService.updatePassword(username, newPassword))
            return new ResponseEntity(HttpStatus.ACCEPTED);
        else return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    /*
    * Transcript
    */
    @RequestMapping(value = "/transcript/{username}", method = RequestMethod.GET)
    public List<courseGrade> findTranscriptById(@PathVariable String username) {
        return applicationService.getTranscriptById(username, currentYear, currentQuarter);
    }

    @RequestMapping(value = "/transcript/courseDetails/{username}/{uoSCode}/{courseSemester}/{courseYear}", method = RequestMethod.GET)
    public courseDetails courseDetails(@PathVariable String username, @PathVariable String uoSCode, @PathVariable String courseSemester, @PathVariable String courseYear) {
        return applicationService.courseDetails(username, uoSCode, courseYear, courseSemester, currentYear, currentQuarter);
    }

    /*
    * Enroll
    */
    @RequestMapping(value = "/enroll/courseOffering/{username}", method = RequestMethod.GET)
    public List<courseOffering> getCourseOfferingById(@PathVariable String username) {
        return applicationService.getCourseOfferingById(username, currentYear, currentQuarter);
    }

    @RequestMapping(value = "/enroll/enroll/{username}/{courseCode}/{courseSemester}/{courseYear}", method = RequestMethod.POST)
    public List<String> enrollCourse(@PathVariable String username, @PathVariable String courseCode, @PathVariable String courseSemester, @PathVariable int courseYear) {
        return applicationService.enrollCourse(username, courseCode, courseYear, courseSemester, currentYear, currentQuarter, currentDate);
    }

    /*
    * Withdraw
    */
    @RequestMapping(value = "/withdraw/currentEnroll/{username}", method = RequestMethod.GET)
    public List<courseGrade> currentEnroll(@PathVariable String username) {
        return applicationService.currentEnroll(username, currentYear, currentQuarter);
    }

    @RequestMapping(value = "withdraw/withdrawCourse/{username}/{courseCode}/{courseYear}/{courseSemester}", method = RequestMethod.POST)
    public List<String> withdrawCourse(@PathVariable String username, @PathVariable String courseCode, @PathVariable String courseYear, @PathVariable String courseSemester) {
        return applicationService.withdrawCourse(username, courseCode, courseYear, courseSemester);
    }

    /*test Transactianl*/
    @RequestMapping(value = "/transaction/{username}/{newAddress1}/{newAddress2}", method = RequestMethod.POST)
    public String updateAddresses(@PathVariable String username, @PathVariable String newAddress1, @PathVariable String newAddress2) {
        Boolean i = applicationService.updateAddresses(username, newAddress1, newAddress2);

        if (i == Boolean.TRUE) return "suc";
        else return "false";
    }
}
