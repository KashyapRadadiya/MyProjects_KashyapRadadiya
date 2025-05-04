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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.kashyap.collegecompanion.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.android.material.button.MaterialButton;

public class TodoAssignmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoAssignmentAdapter adapter;
    private ArrayList<TodoAssignment> todoList = new ArrayList<>();
    private FirebaseFirestore db;
    private MaterialButton addTodoButton; // <-- Changed from ImageButton to MaterialButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_assignment);

        recyclerView = findViewById(R.id.todoRecyclerView);
        addTodoButton = findViewById(R.id.addTodoButton); // This now matches the MaterialButton in XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoAssignmentAdapter(todoList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadTodos();

        // Show add button only for Admin
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email != null && email.endsWith("@admin.com")) {
            addTodoButton.setVisibility(View.VISIBLE);
        } else {
            addTodoButton.setVisibility(View.GONE);
        }

        addTodoButton.setOnClickListener(v -> showAddTodoDialog());
    }

    private void loadTodos() {
        db.collection("todo_assignments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Failed to load items.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    todoList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        TodoAssignment todo = doc.toObject(TodoAssignment.class);
                        if (todo != null) {
                            todo.setId(doc.getId()); // <-- Set the Firestore document ID
                            todoList.add(todo);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddTodoDialog() {
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New To-Do/Assignment")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        postTodo(text);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void postTodo(String text) {
        Map<String, Object> todo = new HashMap<>();
        todo.put("text", text);
        todo.put("timestamp", System.currentTimeMillis());
        db.collection("todo_assignments").add(todo)
                .addOnSuccessListener(docRef -> Toast.makeText(this, "Item added.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add item.", Toast.LENGTH_SHORT).show());
    }
}