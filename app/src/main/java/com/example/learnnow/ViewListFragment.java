package com.example.learnnow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noCourseTextView;
    private CourseAdapter courseAdapter;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        noCourseTextView = view.findViewById(R.id.noCourseTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        List<CourseModel> courseList = databaseHelper.getAllCourses();

        if (courseList == null || courseList.isEmpty()) {
            noCourseTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No Course Available", Toast.LENGTH_SHORT).show();
        } else {
            noCourseTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            // Assuming you have a way to obtain the current student's name (e.g., from SharedPreferences or an Intent)
            String currentStudentName = "You have a request for a course enrollment."; // Replace this with your actual method of retrieving the student's name
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            courseAdapter = new CourseAdapter(courseList, currentStudentName, dbHelper);
            recyclerView.setAdapter(courseAdapter);
        }

        return view;
    }
}
