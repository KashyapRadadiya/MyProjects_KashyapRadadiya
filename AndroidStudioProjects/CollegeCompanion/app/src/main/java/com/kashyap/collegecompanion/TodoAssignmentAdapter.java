package com.kashyap.collegecompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AlertDialog;

public class TodoAssignmentAdapter extends RecyclerView.Adapter<TodoAssignmentAdapter.ViewHolder> {

    private ArrayList<TodoAssignment> todoList;
    private boolean isAdmin;

    public TodoAssignmentAdapter(ArrayList<TodoAssignment> todoList, TodoAssignmentActivity todoAssignmentActivity) {
        this.todoList = todoList;
        String email = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : "";
        this.isAdmin = email != null && email.endsWith("@admin.com");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoAssignment todo = todoList.get(position);
        holder.textView.setText(todo.getText());
        holder.dateView.setText(new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(new Date(todo.getTimestamp())));
        if (isAdmin) {
            holder.actionButton.setVisibility(View.VISIBLE);
            holder.actionButton.setOnClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            FirebaseFirestore.getInstance().collection("todo_assignments")
                                    .document(todo.getId())
                                    .delete();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        } else {
            holder.actionButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, dateView;
        ImageButton actionButton; // Add this line if you have an ImageButton

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.todoTextView);
            dateView = itemView.findViewById(R.id.todoDateView);
            actionButton = itemView.findViewById(R.id.todoActionButton); // Make sure this ID matches your layout
        }
    }
}