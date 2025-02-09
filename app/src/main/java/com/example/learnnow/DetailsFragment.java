package com.example.learnnow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    private TextView textViewDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        textViewDetails = view.findViewById(R.id.textViewDetails);

        // Set the content to display
        String detailsContent = "About LearnNow:\n" +
                "Welcome to LearnNow – Your Gateway to Knowledge!\n\n" +
                "LearnNow is an innovative e-learning platform designed to make education more accessible and engaging. " +
                "Whether you're a student looking to expand your knowledge or an instructor eager to share expertise, " +
                "LearnNow provides a seamless learning experience.\n\n" +
                "What You Can Do with LearnNow?\n" +
                "🔹 Explore & Enroll in Courses – Browse a variety of courses across different subjects.\n" +
                "🔹 Learn at Your Own Pace – Access video lectures, PDFs, and quizzes anytime, anywhere.\n" +
                "🔹 Track Your Progress – Keep track of enrolled courses and completed lessons.\n" +
                "🔹 Request a Course – Can't find a course? Request one, and instructors can create it for you!\n" +
                "🔹 Teach & Earn – Instructors can create and sell courses, reaching a global audience.\n\n" +
                "Why Choose LearnNow?\n" +
                "✔ User-Friendly Interface – Simple and intuitive design for a smooth learning experience.\n" +
                "✔ Quality Content – Courses from experienced professionals and educators.\n" +
                "✔ Affordable Learning – Access valuable knowledge at competitive prices.\n" +
                "✔ Secure Platform – Your data and transactions are always safe with us.\n\n" +
                "Start your learning journey today with LearnNow – where knowledge meets opportunity!";

        textViewDetails.setText(detailsContent);

        return view;
    }
}
