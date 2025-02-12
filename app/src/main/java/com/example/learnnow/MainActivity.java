package com.example.learnnow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonRegister;
    TextView welcomeText;
    DatabaseHelper db;
    FirebaseAuth firebaseAuth;
    String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
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

            if (email.equals("admin@gmail.com") && password.equals("admin")) {
                Toast.makeText(getApplicationContext(), "Admin Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
                return;
            }

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
            } else if (!isValidEmail(email)) {
                editTextEmail.setError("Invalid Email");
            } else {
                loginUser(email, password);
            }
        });

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.putExtra("USER_ROLE", selectedRole);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            if (!db.checkEmailExists(email)) {
                                db.insertUser(user.getDisplayName(), email, password, selectedRole);
                            }

                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            if ("Student".equals(selectedRole)) {
                                startActivity(new Intent(MainActivity.this, StudentActivity.class));
                            } else if ("Instructor".equals(selectedRole)) {
                                startActivity(new Intent(MainActivity.this, InstructorActivity.class));
                            }
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@(gmail\\.com|lus\\.ac\\.bd)";
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.matches(emailPattern);
    }
}
