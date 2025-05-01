package com.kashyap.collegecompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kashyap.collegecompanion.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AlertDialog;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private ArrayList<Notice> noticeList;
    private boolean isAdmin;

    public NoticeAdapter(ArrayList<Notice> noticeList) {
        this.noticeList = noticeList;
        String email = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : "";
        this.isAdmin = email != null && email.endsWith("@admin.com");
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = noticeList.get(position);
        holder.noticeText.setText(notice.getText());
        holder.noticeTime.setText(new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(new Date(notice.getTimestamp())));
        if (isAdmin) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Delete Notice")
                        .setMessage("Are you sure you want to delete this notice?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            FirebaseFirestore.getInstance().collection("notices")
                                    .document(notice.getId())
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
        return noticeList.size();
    }

    static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView noticeText, noticeTime;
        ImageButton deleteButton;
        NoticeViewHolder(View itemView) {
            super(itemView);
            noticeText = itemView.findViewById(R.id.noticeText);
            noticeTime = itemView.findViewById(R.id.noticeTime);
            deleteButton = itemView.findViewById(R.id.noticeDeleteButton);
        }
    }
}