package com.kashyap.collegecompanion;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.kashyap.collegecompanion.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticeBoardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    private ArrayList<Notice> noticeList = new ArrayList<>();
    private FirebaseFirestore db;
    private ImageButton addNoticeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        recyclerView = findViewById(R.id.noticeRecyclerView);
        addNoticeButton = findViewById(R.id.addNoticeButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoticeAdapter(noticeList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadNotices();

        // Show add button only for Admin
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email != null && email.endsWith("@admin.com")) { // Example: use your logic for admin
            addNoticeButton.setVisibility(View.VISIBLE);
        } else {
            addNoticeButton.setVisibility(View.GONE);
        }

        addNoticeButton.setOnClickListener(v -> showAddNoticeDialog());
    }

    private void loadNotices() {
        db.collection("notices")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Failed to load notices.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    noticeList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Notice notice = doc.toObject(Notice.class);
                        if (notice != null) {
                            notice.setId(doc.getId()); // <-- Set the Firestore document ID
                            noticeList.add(notice);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddNoticeDialog() {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New Notice")
                .setView(input)
                .setPositiveButton("Post", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        postNotice(text);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void postNotice(String text) {
        Map<String, Object> notice = new HashMap<>();
        notice.put("text", text);
        notice.put("timestamp", System.currentTimeMillis());
        db.collection("notices").add(notice)
                .addOnSuccessListener(docRef -> Toast.makeText(this, "Notice posted.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to post notice.", Toast.LENGTH_SHORT).show());
    }
}