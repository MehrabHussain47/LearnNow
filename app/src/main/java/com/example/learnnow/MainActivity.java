package com.example.learnnow;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeText = findViewById(R.id.text_welcome);

        // Get the role from intent
        String role = getIntent().getStringExtra("USER_ROLE");
        if (role != null) {
            welcomeText.setText("Welcome, " + role + "!");
        }
    }
}
