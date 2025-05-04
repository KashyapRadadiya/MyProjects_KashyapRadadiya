package com.kashyap.collegecompanion;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;
import com.kashyap.collegecompanion.R;

public class MainDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        MaterialButton materialsButton = findViewById(R.id.materialsButton);
        MaterialButton attendanceButton = findViewById(R.id.attendanceButton);
        MaterialButton examButton = findViewById(R.id.examButton);
        MaterialButton noticeButton = findViewById(R.id.noticeButton);
        MaterialButton todoButton = findViewById(R.id.todoButton);
        MaterialButton aboutAppButton = findViewById(R.id.aboutAppButton);


        materialsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MaterialsActivity.class));
        });

        attendanceButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AttendanceActivity.class));
        });

        examButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ExamReminderActivity.class));
        });

        noticeButton.setOnClickListener(v -> {
            startActivity(new Intent(this, NoticeBoardActivity.class));
        });

        todoButton.setOnClickListener(v -> {
            startActivity(new Intent(this, TodoAssignmentActivity.class));
        });

        aboutAppButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutAppActivity.class));
        });

    }
}