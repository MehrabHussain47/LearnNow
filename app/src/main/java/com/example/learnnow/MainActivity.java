package com.example.learnnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonRegister;
    TextView welcomeText;  // Welcome text view
    DatabaseHelper db;
    String selectedRole; // Selected role passed from RoleSelectionActivity or RegisterActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        welcomeText = findViewById(R.id.textWelcome);  // Initialize welcome text view

        // Get the selected role passed from RoleSelectionActivity or RegisterActivity
        selectedRole = getIntent().getStringExtra("USER_ROLE");

        // Display the welcome message with the role
        if (selectedRole != null) {
            welcomeText.setText("Welcome, " + selectedRole + "!");
        }

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Check if any field is empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            // Validate email format
            else if (!isValidEmail(email)) {
                editTextEmail.setError("Invalid Email");
            }
            // Check if email exists in the database
            else if (!db.checkEmailExists(email)) {
                Toast.makeText(getApplicationContext(), "User not registered. Please Register first.", Toast.LENGTH_SHORT).show();
            }
            // Check if the email and password match, but role does not match
            else if (!db.checkEmailPassword(email, password)) {
                Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
            }
            // Check if email, password, and role do not match together
            else if (!db.checkEmailPasswordRole(email, password, selectedRole)) {
                Toast.makeText(getApplicationContext(), "Invalid Credentials or Role mismatch", Toast.LENGTH_SHORT).show();
            }
            // Successful login
            else {
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                // Navigate to the respective activity based on role
                if (selectedRole.equals("Student")) {
                    startActivity(new Intent(MainActivity.this, StudentActivity.class)); // Replace with Student's Activity
                } else if (selectedRole.equals("Instructor")) {
                    startActivity(new Intent(MainActivity.this, InstructorActivity.class)); // Replace with Instructor's Activity
                }
                finish();
            }
        });


        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("USER_ROLE", selectedRole); // Keep the role
            startActivity(intent);
        });
    }

    // Validate Email using regex (for valid Gmail address)
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@gmail\\.com"; // Matches Gmail addresses
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.matches(emailPattern);
    }
}
