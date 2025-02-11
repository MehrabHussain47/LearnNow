package com.example.learnnow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonRegister, buttonLogin;
    DatabaseHelper db;
    String selectedRole; // Role will be passed from RoleSelectionActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Get the selected role passed from RoleSelectionActivity
        selectedRole = getIntent().getStringExtra("USER_ROLE");

        buttonRegister.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            // Check for empty fields
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
            // Check for password strength first
            else if (!isValidPassword(password)) {
                editTextPassword.setError("Password must include at least one lowercase letter, one uppercase letter, one digit, and be at least 6 characters long.");
            }
            // Then check if the passwords match
            else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
            // Check for valid email
            else if (!isValidEmail(email)) {
                editTextEmail.setError("Invalid Email Address");
            }
            else {
                // Check if the email already exists
                if (db.checkEmailExists(email)) {
                    Toast.makeText(RegisterActivity.this, "Email already exists, Registration Failed.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = db.insertUser(fullName, email, password, selectedRole);
                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.putExtra("USER_ROLE", selectedRole); // Keep the role
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("USER_ROLE", selectedRole);
            startActivity(intent);
            finish();
        });
    }

    // Validate Email using regex (for valid Gmail address)
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@(gmail\\.com|lus\\.ac\\.bd)"; // Matches Gmail addresses
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Validate Password using regex (at least 6 characters, one uppercase, one lowercase, and one digit)
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
