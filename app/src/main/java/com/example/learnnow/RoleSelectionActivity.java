package com.example.learnnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        Button studentButton = findViewById(R.id.button_student);
        Button instructorButton = findViewById(R.id.button_instructor);

        studentButton.setOnClickListener(v -> navigateToMainActivity("Student"));
        instructorButton.setOnClickListener(v -> navigateToMainActivity("Instructor"));
    }

    private void navigateToMainActivity(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, MainActivity.class);
        intent.putExtra("USER_ROLE", role); // Pass the selected role
        startActivity(intent);
        finish();
    }
}

