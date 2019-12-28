package com.ce495db.db_application.Domain;


public class courseDetails {
    private String uoSCode;
    private String semester;
    private int year;
    private String grade;
    private Integer enrollment;
    private Integer maxEnrollment;
    private String uosname;
    private String lecturerName;

    public courseDetails(String uoSCode, String semester, int year, String grade, Integer enrollment, Integer maxEnrollment, String uosname, String lecturerName) {
        this.uoSCode = uoSCode;
        this.semester = semester;
        this.year = year;
        this.grade = grade;
        this.enrollment = enrollment;
        this.maxEnrollment = maxEnrollment;
        this.uosname = uosname;
        this.lecturerName = lecturerName;
    }

    public String getUoSCode() {
        return uoSCode;
    }

    public void setUoSCode(String uoSCode) {
        this.uoSCode = uoSCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public Integer getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Integer enrollment) {
        this.enrollment = enrollment;
    }

    public Integer getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(Integer maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getUosname() {
        return uosname;
    }

    public void setUosname(String uosname) {
        this.uosname = uosname;
    }
}
