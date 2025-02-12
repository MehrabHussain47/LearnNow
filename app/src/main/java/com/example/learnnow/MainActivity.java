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
    TextView welcomeText;
    DatabaseHelper db;
    String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        welcomeText = findViewById(R.id.textWelcome);

        selectedRole = getIntent().getStringExtra("USER_ROLE");

        if (selectedRole != null) {
            welcomeText.setText("Welcome, " + selectedRole + "!");
        }

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Admin login check
            if (email.equals("admin@gmail.com") && password.equals("admin")) {
                Toast.makeText(getApplicationContext(), "Admin Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
                return;
            }

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            else if (!isValidEmail(email)) {
                editTextEmail.setError("Invalid Email");
            }
            else if (!db.checkEmailExists(email)) {
                Toast.makeText(getApplicationContext(), "User not registered. Please Register first.", Toast.LENGTH_SHORT).show();
            }
            else if (!db.checkEmailPassword(email, password)) {
                Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
            }
            else if (!db.checkEmailPasswordRole(email, password, selectedRole)) {
                Toast.makeText(getApplicationContext(), "Invalid Credentials or Role mismatch", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                if (selectedRole.equals("Student")) {
                    startActivity(new Intent(MainActivity.this, StudentActivity.class));
                } else if (selectedRole.equals("Instructor")) {
                    startActivity(new Intent(MainActivity.this, InstructorActivity.class));
                }
                finish();
            }
        });

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("USER_ROLE", selectedRole);
            startActivity(intent);
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@(gmail\\.com|lus\\.ac\\.bd)";
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.matches(emailPattern);
    }
}
