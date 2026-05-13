package com.hyfacademy.service;
import com.hyfacademy.model.Student;

public interface Enrollable {
    void enrol(Student student);
    boolean isFull();
    int getEnrolledCount();
    int getMaxStudents();

    default String capacityStatus() {
        return getEnrolledCount() + "/" + getMaxStudents() +
                (isFull() ? " — FULL" : " — Open");
    }
}