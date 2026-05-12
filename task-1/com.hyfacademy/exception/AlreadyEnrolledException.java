package com.hyfacademy.exception;

public class  AlreadyEnrolledException extends  EnrolmentException {
    private final String studentName;
    private final String courseName;

    public AlreadyEnrolledException(String studentName, String courseName) {
        super(studentName + " is already enrolled in '" + courseName + "'");
        this.studentName = studentName;
        this.courseName = courseName;

    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseName() {
        return courseName;
    }
}
