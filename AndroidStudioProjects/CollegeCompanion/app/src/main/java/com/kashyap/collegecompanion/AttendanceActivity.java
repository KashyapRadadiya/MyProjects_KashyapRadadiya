package com.kashyap.collegecompanion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.kashyap.collegecompanion.R;
import java.util.*;

public class AttendanceActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button markAttendanceButton, viewAttendanceButton;
    private EditText studentIdEditText;
    private ListView attendanceListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        markAttendanceButton = findViewById(R.id.markAttendanceButton);
        viewAttendanceButton = findViewById(R.id.viewAttendanceButton);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        attendanceListView = findViewById(R.id.attendanceListView);

        // Only allow admins to mark attendance
        String email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;
        if (email == null || !email.endsWith("@admin.com")) {
            markAttendanceButton.setVisibility(View.GONE);
            studentIdEditText.setVisibility(View.GONE);
        } else {
            markAttendanceButton.setOnClickListener(v -> markAttendance());
        }

        viewAttendanceButton.setOnClickListener(v -> viewAttendance());
    }

    private void markAttendance() {
        String studentId = studentIdEditText.getText().toString().trim();
        if (studentId.isEmpty()) {
            Toast.makeText(this, "Enter Student ID", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> attendance = new HashMap<>();
        attendance.put("studentId", studentId);
        attendance.put("timestamp", System.currentTimeMillis());
        db.collection("attendance").add(attendance)
                .addOnSuccessListener(docRef -> Toast.makeText(this, "Attendance marked", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to mark attendance", Toast.LENGTH_SHORT).show());
    }

    private void viewAttendance() {
        db.collection("attendance")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> attendanceList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String studentId = doc.getString("studentId");
                        Date date = new Date(doc.getLong("timestamp"));
                        attendanceList.add(studentId + " - " + date.toString());
                    }
                    // Simple ArrayAdapter for demonstration
                    attendanceListView.setAdapter(new android.widget.ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendanceList));
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load attendance", Toast.LENGTH_SHORT).show());
    }
}