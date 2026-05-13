package com.hyfacademy.model;

import com.hyfacademy.exception.EnrolmentException;

public class Mentor extends User {
    private final String expertise;
    private final Course [] assignedCourses ;
    private  int courseCount;
    private static int counter = 1;

    private static String generateId () {
        return String.format("COA-%03d", counter++);
    }

    public Mentor (String name, String email, String expertise) {
        super(name,email,generateId());
        this.expertise = expertise;
        this.assignedCourses = new Course[3];
        this.courseCount = 0 ;

    }
    public void assignToCourse(Course course) {
        if (courseCount >= assignedCourses.length)
        {
            throw new EnrolmentException(getName() + " cannot be assigned to more than 3 courses");
        }
        assignedCourses[courseCount] = course ;
        courseCount ++;
    }

    public Course[] getAssignedCourses () {
        return assignedCourses;
    }

    public String getExpertise() {
        return expertise;
    }
    @Override
    public String getRole() {
        return "MENTOR";
    }
}
