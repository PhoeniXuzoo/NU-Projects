package com.ce495db.db_application.Domain;

public class currentCourse {
    private String uoSCode;
    private String uoSName;
    private String semester;
    private int year;

    public currentCourse(String uoSCode, String uoSName, String semester, int year) {
        this.uoSCode = uoSCode;
        this.uoSName = uoSName;
        this.semester = semester;
        this.year = year;
    }

    public String getUoSCode() {
        return uoSCode;
    }

    public void setUoSCode(String uoSCode) {
        this.uoSCode = uoSCode;
    }

    public void setUoSName(String uoSName) {
        this.uoSName = uoSName;
    }

    public String getUoSName() {
        return uoSName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
