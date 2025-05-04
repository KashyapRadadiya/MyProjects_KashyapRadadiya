package com.kashyap.collegecompanion;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.kashyap.collegecompanion.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExamReminderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExamReminderAdapter adapter;
    private ArrayList<ExamReminder> reminderList = new ArrayList<>();
    private FirebaseFirestore db;
    private MaterialButton addReminderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_reminder);

        recyclerView = findViewById(R.id.examRecyclerView);
        addReminderButton = findViewById(R.id.addExamButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExamReminderAdapter(reminderList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadReminders();

        // Show add button only for Admin
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email != null && email.endsWith("@admin.com")) {
            addReminderButton.setVisibility(View.VISIBLE);
        } else {
            addReminderButton.setVisibility(View.GONE);
        }

        addReminderButton.setOnClickListener(v -> showAddReminderDialog());
    }

    private void loadReminders() {
        db.collection("exam_reminders")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Failed to load reminders.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    reminderList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        ExamReminder reminder = doc.toObject(ExamReminder.class);
                        if (reminder != null) {
                            reminder.setId(doc.getId()); // <-- Set the Firestore document ID
                            reminderList.add(reminder);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddReminderDialog() {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New Exam Reminder")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        postReminder(text);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void postReminder(String text) {
        Map<String, Object> reminder = new HashMap<>();
        reminder.put("text", text);
        reminder.put("timestamp", System.currentTimeMillis());
        db.collection("exam_reminders").add(reminder)
                .addOnSuccessListener(docRef -> Toast.makeText(this, "Reminder added.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add reminder.", Toast.LENGTH_SHORT).show());
    }
}