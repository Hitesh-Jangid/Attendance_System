package com.hiteshjangid.attendance.model;

import java.util.List;

public class Attendance_Reports {

    String date;
    String monthOnly;
    String dateOnly;
    String classId;
    String date_and_classID;
    String classname;
    String subjName;
    List<Attendance_Students_List> attendance_students_lists;
    private int month;

    public Attendance_Reports() {
    }

    public Attendance_Reports(String date, String monthOnly, String dateOnly, String classId, String date_and_classID, String classname, String subjName, List<Attendance_Students_List> attendance_students_lists) {
        this.date = date;
        this.monthOnly = monthOnly;
        this.dateOnly = dateOnly;
        this.classId = classId;
        this.date_and_classID = date_and_classID;
        this.classname = classname;
        this.subjName = subjName;
        this.attendance_students_lists = attendance_students_lists;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonthOnly() {
        return monthOnly;
    }

    public void setMonthOnly(String monthOnly) {
        this.monthOnly = monthOnly;
    }

    public String getDateOnly() {
        return dateOnly;
    }

    public void setDateOnly(String dateOnly) {
        this.dateOnly = dateOnly;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDate_and_classID() {
        return date_and_classID;
    }

    public void setDate_and_classID(String date_and_classID) {
        this.date_and_classID = date_and_classID;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjName() {
        return subjName;
    }

    public void setSubjName(String subjName) {
        this.subjName = subjName;
    }

    public List<Attendance_Students_List> getAttendance_students_lists() {
        return attendance_students_lists;
    }

    public void setAttendance_students_lists(List<Attendance_Students_List> attendance_students_lists) {
        this.attendance_students_lists = attendance_students_lists;
    }

    private String reportMessage;

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    private String studentName;
    private String rollNumber;
    private String status;

    // Existing constructors, getters, and setters

    // Add the following methods

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


}
