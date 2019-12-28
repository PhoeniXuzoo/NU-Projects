package com.ce495db.db_application.Domain;

public class courseOffering {
    private String uoSCode;
    private String uoSName;
    private String semester;
    private int year;
    private Integer enrollment;
    private Integer maxEnrollment;

    public courseOffering(String uoSCode, String uoSName, String semester, int year, Integer enrollment, Integer maxEnrollment) {
        this.uoSCode = uoSCode;
        this.uoSName = uoSName;
        this.semester = semester;
        this.year = year;
        this.enrollment = enrollment;
        this.maxEnrollment = maxEnrollment;
    }

    public void setUoSCode(String uoSCode) {
        this.uoSCode = uoSCode;
    }

    public String getUoSCode() {
        return uoSCode;
    }

    public void setUoSName(String uoSName) {
        this.uoSName = uoSName;
    }

    public String getUoSName() {
        return uoSName;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSemester() {
        return semester;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setEnrollment(Integer enrollment) {
        this.enrollment = enrollment;
    }

    public Integer getEnrollment() {
        return enrollment;
    }

    public void setMaxEnrollment(Integer maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public Integer getMaxEnrollment() {
        return maxEnrollment;
    }
}
