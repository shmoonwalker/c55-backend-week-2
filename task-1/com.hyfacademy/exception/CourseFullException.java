package com.hyfacademy.exception;

public class CourseFullException extends  EnrolmentException{
    private final String courseName;
    private final int maxCapacity;

    public CourseFullException(String courseName, int maxCapacity){
        super("Course '" + courseName + "' is full (max: " + maxCapacity + " students)");
        this.courseName = courseName;
        this.maxCapacity = maxCapacity;

    }

    public String getCourseName () {
        return courseName;
    }

    public int getMaxCapacity () {
        return maxCapacity;
    }
}
