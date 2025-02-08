//package com.example.learnnow;
//
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class StudentActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student);
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        // Set up the item selected listener
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//            switch (item.getItemId()) {
//                case R.id.nav_enrolled:
//                    selectedFragment = new EnrolledFragment();
//                    break;
//                case R.id.nav_view_list:  // New case for View List
//                    selectedFragment = new ViewListFragment();  // Ensure ViewListFragment is created
//                    break;
//                case R.id.nav_details:
//                    selectedFragment = new DetailsFragment();
//                    break;
//            }
//            if (selectedFragment != null) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//            }
//            return true;
//        });
//
//        // Load default fragment
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EnrolledFragment()).commit();
//        }
//    }
//}
package com.example.learnnow;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_view_list) {
                selectedFragment = new ViewListFragment();
            } else if (item.getItemId() == R.id.nav_enrolled) {
                selectedFragment = new EnrolledFragment();
            } else if (item.getItemId() == R.id.nav_details) {
                selectedFragment = new DetailsFragment();
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewListFragment()).commit();
        }
    }
}
