package com.example.learnnow;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddCourseFragment extends Fragment {

    private static final int PICK_IMAGE = 1;

    private EditText courseNameEditText;
    private EditText instructorNameEditText;
    private ImageView photoImageView;
    private Button selectPhotoButton;
    private Button addCourseButton;

    private Uri selectedImageUri;

    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);

        // Initialize views
        courseNameEditText = view.findViewById(R.id.courseName);
        instructorNameEditText = view.findViewById(R.id.instructorName);
        photoImageView = view.findViewById(R.id.photoImageView);
        selectPhotoButton = view.findViewById(R.id.selectPhotoButton);
        addCourseButton = view.findViewById(R.id.addCourseButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(getContext());

        // Handle select photo button click
        selectPhotoButton.setOnClickListener(v -> openGallery());

        // Handle add course button click
        addCourseButton.setOnClickListener(v -> addCourse());

        return view;
    }

    // Open the gallery to select an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    // Handle the result of the image selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {
            selectedImageUri = data.getData();
            photoImageView.setImageURI(selectedImageUri); // Show the selected image in ImageView
        }
    }

    // Add course to the database
    private void addCourse() {
        String courseName = courseNameEditText.getText().toString();
        String instructorName = instructorNameEditText.getText().toString();

        if (courseName.isEmpty() || instructorName.isEmpty() || selectedImageUri == null) {
            Toast.makeText(getContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
        } else {
            // Convert image URI to string path (this can be adjusted based on your needs)
            String imagePath = selectedImageUri.toString();

            // Insert course into the database
            databaseHelper.insertCourse(courseName, instructorName, imagePath);

            // Show success message
            Toast.makeText(getContext(), "Course added successfully!", Toast.LENGTH_SHORT).show();

            // Clear the fields
            courseNameEditText.setText("");
            instructorNameEditText.setText("");
            photoImageView.setImageResource(R.drawable.ic_placeholder); // Reset ImageView
        }
    }
}
