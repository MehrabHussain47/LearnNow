package com.example.learnnow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditUserActivity extends AppCompatActivity {

    private EditText editTextFullName;
    private Button buttonSave;
    private int userId;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("USER_ID", -1);

        editTextFullName = findViewById(R.id.editTextFullName);
        buttonSave = findViewById(R.id.buttonSave);

        UserModel user = db.getUserById(userId);
        if (user != null) {
            editTextFullName.setText(user.getFullName());
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFullName = editTextFullName.getText().toString().trim();


                if (newFullName.isEmpty()) {
                    Toast.makeText(EditUserActivity.this, "Full Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isUpdated = db.updateUserFullName(userId, newFullName);

                if (isUpdated) {
                    Toast.makeText(EditUserActivity.this, "Full Name updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditUserActivity.this, "Failed to update Full Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}