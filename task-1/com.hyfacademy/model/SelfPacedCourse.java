package com.hyfacademy.model;
import com.hyfacademy.service.Reportable;

public class SelfPacedCourse extends Course implements Reportable {

    private int estimatedHours;

    public SelfPacedCourse(
            String courseName,
            int maxStudents,
            int estimatedHours
    ) {
        super(courseName, maxStudents);
        this.estimatedHours = estimatedHours;
    }

    @Override
    public String getCourseType() {
        return "Self-Paced";
    }

    @Override
    public String getScheduleInfo() {
        return "Estimated: "
                + estimatedHours
                + " hours — complete at your own pace";
    }
    @Override
    public String generateReport() {

        StringBuilder report = new StringBuilder();

        report.append("══════════════════════════════════════════\n");
        report.append("  COURSE REPORT — Self-Paced\n");
        report.append("══════════════════════════════════════════\n");
        report.append("  ID          : ").append(getCourseId()).append("\n");
        report.append("  Name        : ").append(getCourseName()).append("\n");
        report.append("  Capacity    : ")
                .append(getEnrolledCount())
                .append("/")
                .append(getMaxStudents())
                .append(isFull() ? " — FULL\n" : " — Open\n");

        report.append("  Est. Hours  : ")
                .append(estimatedHours)
                .append("\n");

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