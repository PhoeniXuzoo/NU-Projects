package com.ce495db.db_application.Domain;

public class courseGrade {
    private int StudId;
    private String UoSCode;
    private String uoSName;
    private String Semester;
    private int Year;
    private String Grade;

    public courseGrade(int StudentId, String UoSCode, String uoSName, String Semester, int Year, String Grade) {
        this.StudId = StudentId;
        this.UoSCode = UoSCode;
        this.uoSName = uoSName;
        this.Semester = Semester;
        this.Year = Year;
        this.Grade = Grade;
    }

    public int getStudId() {
        return StudId;
    }

    public void setStudId(int studentId) {
        StudId = studentId;
    }

    public String getUoSCode() {
        return UoSCode;
    }

    public void setUoSCode(String uoSCode) {
        UoSCode = uoSCode;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getUoSName() {
        return uoSName;
    }

    public void setUoSName(String uoSName) {
        this.uoSName = uoSName;
    }
}
