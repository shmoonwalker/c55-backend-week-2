package com.hyfacademy.model;
import com.hyfacademy.exception.AlreadyEnrolledException;
import com.hyfacademy.exception.EnrolmentException;

public class Student extends User {
    private static int counter = 1;
    private final Course [] enrolledCourses;
    private  int courseCount;

    private static String generateId() {
        return String.format("STU-%03d", counter++);
    }


    public Student (String name,String email) {
        super(name,email,generateId());
        this.enrolledCourses = new Course[5];
        this.courseCount = 0;

    }

    public void enrol (Course course) {
        for(int i =0 ; i < courseCount ; i++)
            if(enrolledCourses[i].getCourseName().equals(course.getCourseName())) {
                throw new AlreadyEnrolledException(getName(),course.getCourseName());
            }

        if (courseCount >= enrolledCourses.length){
            throw new EnrolmentException("Student " + getName() + " cannot enrol in more than 5 courses");
        }

        enrolledCourses[courseCount] =course;
        courseCount ++ ;
    }
    public Course[] getCourses() {
        return enrolledCourses;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public int getProgress(String courseName) {

        for (int i = 0; i < courseCount; i++) {
            if (enrolledCourses[i].getCourseName().equals(courseName)) {
                return enrolledCourses[i].getStudentProgress(this);
            }
        }

        throw new EnrolmentException(
                getName() + " is not enrolled in '" + courseName + "'"
        );
    }
    @Override

    public String getRole() {
        return "STUDENT" ;

    }
}
