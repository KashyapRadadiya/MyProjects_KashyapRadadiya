package com.kashyap.collegecompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kashyap.collegecompanion.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExamReminderAdapter extends RecyclerView.Adapter<ExamReminderAdapter.ViewHolder> {

    private ArrayList<ExamReminder> reminderList;
    private boolean isAdmin;

    public ExamReminderAdapter(ArrayList<ExamReminder> reminderList) {
        this.reminderList = reminderList;
        String email = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : "";
        this.isAdmin = email != null && email.endsWith("@admin.com");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamReminder reminder = reminderList.get(position);
        holder.textView.setText(reminder.getText());
        String dateStr = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(new Date(reminder.getTimestamp()));
        holder.dateView.setText(dateStr);
        if (isAdmin) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Delete Reminder")
                        .setMessage("Are you sure you want to delete this reminder?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            FirebaseFirestore.getInstance().collection("exam_reminders")
                                    .document(reminder.getId())
                                    .delete();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, dateView;
        ImageButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.examTextView);
            dateView = itemView.findViewById(R.id.examDateView);
            deleteButton = itemView.findViewById(R.id.examDeleteButton); // Add this to your layout
        }
    }
}