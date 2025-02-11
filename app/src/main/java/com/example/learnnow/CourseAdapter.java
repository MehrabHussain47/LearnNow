package com.example.learnnow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<CourseModel> courseList;
    private String currentStudentName; // the logged-in student's full name
    private DatabaseHelper databaseHelper;

    // New constructor with all necessary parameters
    public CourseAdapter(List<CourseModel> courseList, String currentStudentName, DatabaseHelper databaseHelper) {
        this.courseList = courseList;
        this.currentStudentName = currentStudentName;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseModel course = courseList.get(position);
        holder.courseNameTextView.setText("Course Name: " + course.getCourseName());
        holder.instructorNameTextView.setText("Instructor Name: " + course.getInstructorName());
        if(course.getCourseImage() != null){
            holder.courseImageView.setImageBitmap(course.getCourseImage());
        } else {
            holder.courseImageView.setImageResource(R.drawable.ic_placeholder);
        }

        holder.enrollButton.setOnClickListener(v -> {
            // Insert a request using the student's full name and course name
            boolean inserted = databaseHelper.insertRequest(currentStudentName, course.getCourseName());
            if(inserted) {
                Toast.makeText(v.getContext(), "Request Sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImageView;
        TextView courseNameTextView, instructorNameTextView;
        Button enrollButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImageView = itemView.findViewById(R.id.courseImage);
            courseNameTextView = itemView.findViewById(R.id.courseName);
            instructorNameTextView = itemView.findViewById(R.id.instructorName);
            enrollButton = itemView.findViewById(R.id.enrollButton);
        }
    }
}
