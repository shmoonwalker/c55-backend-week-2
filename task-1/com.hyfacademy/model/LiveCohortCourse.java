package com.hyfacademy.model;
import com.hyfacademy.service.Reportable;

public class LiveCohortCourse extends Course implements Reportable {

    private final String startDate;
    private final String endDate;
    private Mentor mentor;

    public LiveCohortCourse(
            String courseName,
            int maxStudents,
            String startDate,
            String endDate,
            Mentor mentor
    ) {
        super(courseName, maxStudents);

        this.startDate = startDate;
        this.endDate = endDate;

        assignMentor(mentor);
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void assignMentor(Mentor mentor) {
        this.mentor = mentor;
        mentor.assignToCourse(this);
    }

    @Override
    public String getCourseType() {
        return "Live Cohort";
    }

    @Override
    public String getScheduleInfo() {
        return startDate
                + " to "
                + endDate
                + " | Mentor: "
                + mentor.getName();
    }
    @Override
    public String generateReport() {

        StringBuilder report = new StringBuilder();

        report.append("══════════════════════════════════════════\n");
        report.append("  COURSE REPORT — Live Cohort\n");
        report.append("══════════════════════════════════════════\n");

        report.append("  ID          : ").append(getCourseId()).append("\n");
        report.append("  Name        : ").append(getCourseName()).append("\n");

        report.append("  Schedule    : ")
                .append(startDate)
                .append(" to ")
                .append(endDate)
                .append("\n");

        report.append("  Mentor      : ")
                .append(mentor.getName())
                .append("\n");

        report.append("  Capacity    : ")
                .append(getEnrolledCount())
                .append("/")
                .append(getMaxStudents())
                .append(isFull() ? " — FULL\n" : " — Open\n");

        report.append("──────────────────────────────────────────\n");
        report.append("  STUDENT PROGRESS\n");
        report.append("──────────────────────────────────────────\n");

        int total = 0;

        for (int i = 0; i < getEnrolledCount(); i++) {

            Student student = getStudents()[i];
            int progress = getStudentProgress()[i];

            total += progress;

            int filled = Math.round(progress / 10.0f);

            StringBuilder bar = new StringBuilder();

            for (int j = 0; j < 10; j++) {
                if (j < filled) {
                    bar.append("█");
                } else {
                    bar.append("░");
                }
            }

            report.append(String.format(
                    "  %-9s %-20s %3d%%   %s%n",
                    student.getUserId(),
                    student.getName(),
                    progress,
                    bar
            ));
        }

        int average = getEnrolledCount() == 0
                ? 0
                : total / getEnrolledCount();

        report.append("──────────────────────────────────────────\n");
        report.append("  Avg Progress : ")
                .append(average)
                .append("%\n");

        report.append("══════════════════════════════════════════");

        return report.toString();
    }
}