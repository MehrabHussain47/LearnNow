package com.example.learnnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private DatabaseHelper db;
    private List<UserModel> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();
    }

    private void loadUsers() {
        userList = db.getAllUsers(); // Fetch users from DB
        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);
    }

    @Override
    public void onEditUser(UserModel user) {
        Intent intent = new Intent(AdminActivity.this, EditUserActivity.class);
        intent.putExtra("USER_ID", user.getId()); // Pass the user ID
        startActivity(intent);
    }

    @Override
    public void onDeleteUser(int userId) {
        boolean deleted = db.deleteUser(userId);
        if (deleted) {
            Toast.makeText(this, "User Deleted", Toast.LENGTH_SHORT).show();
            loadUsers(); // Refresh list after deletion
        } else {
            Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }
    }
}