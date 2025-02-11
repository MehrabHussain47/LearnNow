package com.example.learnnow;

public class CourseModel {
    private int courseId;
    private String courseName;
    private String instructorName;
    private String imagePath; // URL or path to the course image

    public CourseModel(int courseId, String courseName, String instructorName, String imagePath) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.imagePath = imagePath;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getImagePath() {
        return imagePath;
    }
}
