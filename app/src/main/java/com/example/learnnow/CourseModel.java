package com.example.learnnow;

import android.graphics.Bitmap;

public class CourseModel {
    private int courseId;
    private String courseName;
    private String instructorName;
    private Bitmap courseImage;

    public CourseModel(int courseId, String courseName, String instructorName, Bitmap courseImage) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.courseImage = courseImage;
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

    public Bitmap getCourseImage() {
        return courseImage;
    }
}
