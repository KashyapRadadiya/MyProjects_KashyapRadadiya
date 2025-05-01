package com.kashyap.collegecompanion;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.kashyap.collegecompanion.R;

public class MainDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        Button materialsButton = findViewById(R.id.materialsButton);
        Button attendanceButton = findViewById(R.id.attendanceButton);
        Button examButton = findViewById(R.id.examButton);
        Button noticeButton = findViewById(R.id.noticeButton);
        Button todoButton = findViewById(R.id.todoButton);
        Button aboutAppButton = findViewById(R.id.aboutAppButton);


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