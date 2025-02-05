package com.example.learnnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class RoleSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        Button studentButton = findViewById(R.id.button_student);
        Button instructorButton = findViewById(R.id.button_instructor);

        studentButton.setOnClickListener(v -> navigateToMainActivity());
        instructorButton.setOnClickListener(v -> navigateToMainActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(RoleSelectionActivity.this, MainActivity.class);
        startActivity(intent);
    }
}