package com.example.learnnow;

public class RequestModel {
    private int requestId;
    private String studentFullName;
    private String courseName;

    public RequestModel(int requestId, String studentFullName, String courseName) {
        this.requestId = requestId;
        this.studentFullName = studentFullName;
        this.courseName = courseName;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public String getCourseName() {
        return courseName;
    }
}
