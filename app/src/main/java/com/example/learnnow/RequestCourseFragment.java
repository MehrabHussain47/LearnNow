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

public class RequestCourseFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noRequestTextView;
    private RequestAdapter requestAdapter;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_course, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRequests);
        noRequestTextView = view.findViewById(R.id.noRequestTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        List<RequestModel> requestList = databaseHelper.getAllRequests();

        if (requestList == null || requestList.isEmpty()) {
            noRequestTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No Request Available", Toast.LENGTH_SHORT).show();
        } else {
            noRequestTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            requestAdapter = new RequestAdapter(requestList);
            recyclerView.setAdapter(requestAdapter);
        }

        return view;
    }
}
