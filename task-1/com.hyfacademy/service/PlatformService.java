package com.hyfacademy.service;

import com.hyfacademy.exception.AlreadyEnrolledException;
import com.hyfacademy.exception.CourseFullException;
import com.hyfacademy.exception.EnrolmentException;
import com.hyfacademy.exception.InvalidProgressException;
import com.hyfacademy.model.Course;
import com.hyfacademy.model.LiveCohortCourse;
import com.hyfacademy.model.Mentor;
import com.hyfacademy.model.SelfPacedCourse;
import com.hyfacademy.model.Student;

import java.util.Scanner;

public class PlatformService {

    private final Course[] courses = new Course[10];
    private int courseCount = 0;

    private final Student[] students = new Student[20];
    private int studentCount = 0;

    private final Mentor[] mentors = new Mentor[5];
    private int mentorCount = 0;

    private final Scanner scanner = new Scanner(System.in);

    public void addStudent() {
        if (studentCount >= students.length) {
            System.out.println("Error: student list is full.");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        Student student = new Student(name, email);
        students[studentCount] = student;
        studentCount++;

        System.out.println("Student added: " + student.getSummary());
    }

    public void addMentor() {
        if (mentorCount >= mentors.length) {
            System.out.println("Error: mentor list is full.");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Expertise: ");
        String expertise = scanner.nextLine();

        Mentor mentor = new Mentor(name, email, expertise);
        mentors[mentorCount] = mentor;
        mentorCount++;

        System.out.println("Mentor added: " + mentor.getSummary());
    }

    public void addCourse() {
        if (courseCount >= courses.length) {
            System.out.println("Error: course list is full.");
            return;
        }

        try {
            System.out.print("Type (self-paced/live cohort): ");
            String type = scanner.nextLine();

            System.out.print("Course name: ");
            String courseName = scanner.nextLine();

            System.out.print("Max students: ");
            int maxStudents = Integer.parseInt(scanner.nextLine());

            if (type.equalsIgnoreCase("self-paced")) {
                System.out.print("Estimated hours: ");
                int estimatedHours = Integer.parseInt(scanner.nextLine());

                Course course = new SelfPacedCourse(courseName, maxStudents, estimatedHours);
                courses[courseCount] = course;
                courseCount++;

                System.out.println("Course added: " + course);

            } else if (type.equalsIgnoreCase("live cohort")) {
                System.out.print("Start date: ");
                String startDate = scanner.nextLine();

                System.out.print("End date: ");
                String endDate = scanner.nextLine();

                System.out.print("Mentor ID: ");
                String mentorId = scanner.nextLine();

                Mentor mentor = findMentorById(mentorId);

                if (mentor == null) {
                    System.out.println("Error: mentor not found.");
                    return;
                }

                Course course = new LiveCohortCourse(
                        courseName,
                        maxStudents,
                        startDate,
                        endDate,
                        mentor
                );

                courses[courseCount] = course;
                courseCount++;

                System.out.println("Course added: " + course);

            } else {
                System.out.println("Error: invalid course type.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: max students / estimated hours must be valid numbers.");
        } catch (EnrolmentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void enrolStudent() {
        try {
            System.out.print("Student ID: ");
            String studentId = scanner.nextLine();

            System.out.print("Course ID: ");
            String courseId = scanner.nextLine();

            Student student = findStudentById(studentId);
            Course course = findCourseById(courseId);

            if (student == null) {
                System.out.println("Error: student not found.");
                return;
            }

            if (course == null) {
                System.out.println("Error: course not found.");
                return;
            }

            course.enrol(student);
            student.enrol(course);

            System.out.println("Student enrolled successfully.");

        } catch (CourseFullException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (AlreadyEnrolledException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (EnrolmentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateProgress() {
        try {
            System.out.print("Student ID: ");
            String studentId = scanner.nextLine();

            System.out.print("Course ID: ");
            String courseId = scanner.nextLine();

            System.out.print("Progress: ");
            int progress = Integer.parseInt(scanner.nextLine());

            Student student = findStudentById(studentId);
            Course course = findCourseById(courseId);

            if (student == null) {
                System.out.println("Error: student not found.");
                return;
            }

            if (course == null) {
                System.out.println("Error: course not found.");
                return;
            }

            course.updateProgress(student, progress);

            System.out.println("Progress updated successfully.");

        } catch (InvalidProgressException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: progress must be a valid integer.");
        } catch (EnrolmentException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Progress update operation finished.");
        }
    }

    public void viewAllCourses() {
        if (courseCount == 0) {
            System.out.println("No courses registered.");
            return;
        }

        System.out.printf("%-10s %-25s %-15s %-15s%n",
                "ID", "Name", "Type", "Capacity");

        System.out.println("----------------------------------------------------------------");

        for (int i = 0; i < courseCount; i++) {
            Course course = courses[i];

            System.out.printf("%-10s %-25s %-15s %-15s%n",
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCourseType(),
                    course.capacityStatus());
        }
    }

    public void viewCourseReport() {
        System.out.print("Course ID: ");
        String courseId = scanner.nextLine();

        Course course = findCourseById(courseId);

        if (course == null) {
            System.out.println("Error: course not found.");
            return;
        }

        if (course instanceof Reportable) {
            Reportable reportable = (Reportable) course;
            System.out.println(reportable.generateReport());
        } else {
            System.out.println("Error: this course does not support reports.");
        }
    }

    public void viewAllStudents() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }

        for (int i = 0; i < studentCount; i++) {
            Student student = students[i];

            System.out.println(
                    student.getSummary()
                            + " | Enrolled courses: "
                            + student.getCourseCount()
            );
        }
    }

    public void run() {

        while (true) {

            System.out.println();
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║       HYF ACADEMY COURSE PLATFORM        ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println("  1. Add course");
            System.out.println("  2. Add student");
            System.out.println("  3. Add mentor");
            System.out.println("  4. Enrol student in course");
            System.out.println("  5. Update student progress");
            System.out.println("  6. View all courses");
            System.out.println("  7. View course report");
            System.out.println("  8. View all students");
            System.out.println("  9. Exit");
            System.out.println("══════════════════════════════════════════");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    addCourse();
                    break;

                case "2":
                    addStudent();
                    break;

                case "3":
                    addMentor();
                    break;

                case "4":
                    enrolStudent();
                    break;

                case "5":
                    updateProgress();
                    break;

                case "6":
                    viewAllCourses();
                    break;

                case "7":
                    viewCourseReport();
                    break;

                case "8":
                    viewAllStudents();
                    break;

                case "9":
                    System.out.println("Goodbye.");
                    return;

                default:
                    System.out.println("Error: invalid menu option.");
            }
        }
    }

    private Student findStudentById(String studentId) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getUserId().equals(studentId)) {
                return students[i];
            }
        }

        return null;
    }

    private Course findCourseById(String courseId) {
        for (int i = 0; i < courseCount; i++) {
            if (courses[i].getCourseId().equals(courseId)) {
                return courses[i];
            }
        }

        return null;
    }

    private Mentor findMentorById(String mentorId) {
        for (int i = 0; i < mentorCount; i++) {
            if (mentors[i].getUserId().equals(mentorId)) {
                return mentors[i];
            }
        }

        return null;
    }
}