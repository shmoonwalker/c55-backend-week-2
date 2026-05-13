package com.hyfacademy.model;

import com.hyfacademy.exception.AlreadyEnrolledException;
import com.hyfacademy.exception.CourseFullException;
import com.hyfacademy.exception.EnrolmentException;
import com.hyfacademy.exception.InvalidProgressException;
import com.hyfacademy.service.Enrollable;

public abstract class Course implements Enrollable {
    private final String courseName;
    private final String courseId;
    private final int maxStudents;
    private int enrolledCount;
    private final int[] studentProgress;
    private final Student[] students;
    private static int counter = 1;

    private static String generateId() {
        return String.format("CRS-%03d", counter++);
    }

    public Course(String courseName, int maxStudents) {
        this.courseName = courseName;
        this.maxStudents = maxStudents;
        this.courseId = generateId();
        this.students = new Student[maxStudents];
        this.studentProgress = new int[maxStudents];
        this.enrolledCount = 0;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public int getEnrolledCount() {
        return enrolledCount;
    }

    public int[] getStudentProgress() {
        return studentProgress;
    }

    public Student[] getStudents() {
        return students;
    }

    public void enrol(Student student) {
        if (enrolledCount >= maxStudents) {
            throw new CourseFullException(courseName, maxStudents);
        }

        for (int i = 0; i < enrolledCount; i++) {
            if (students[i].getUserId().equals(student.getUserId())) {
                throw new AlreadyEnrolledException(student.getName(), courseName);
            }
        }

        students[enrolledCount] = student;
        studentProgress[enrolledCount] = 0;
        enrolledCount++;
    }

    public void updateProgress(Student student, int progress) {
        if (progress < 0 || progress > 100) {
            throw new InvalidProgressException(progress);
        }

        for (int i = 0; i < enrolledCount; i++) {
            if (students[i].getUserId().equals(student.getUserId())) {
                studentProgress[i] = progress;
                return;
            }
        }

        throw new EnrolmentException(student.getName() + " is not enrolled in '" + courseName + "'");
    }

    public int getStudentProgress(Student student) {
        for (int i = 0; i < enrolledCount; i++) {
            if (students[i].getUserId().equals(student.getUserId())) {
                return studentProgress[i];
            }
        }

        throw new EnrolmentException(student.getName() + " is not enrolled in '" + courseName + "'");
    }

    public boolean isFull() {
        return enrolledCount >= maxStudents;
    }

    public abstract String getCourseType();

    public abstract String getScheduleInfo();

    @Override
    public String toString() {
        return "[" + courseId + "] "
                + courseName
                + " ("
                + getCourseType()
                + ") | Enrolled: "
                + enrolledCount
                + "/"
                + maxStudents;
    }
}