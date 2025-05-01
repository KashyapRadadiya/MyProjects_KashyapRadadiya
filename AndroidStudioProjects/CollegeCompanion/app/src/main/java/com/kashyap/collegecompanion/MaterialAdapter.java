package com.kashyap.collegecompanion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private ArrayList<Material> materialList;
    private Context context;
    private boolean isAdmin;

    public MaterialAdapter(ArrayList<Material> materialList, Context context) {
        this.materialList = materialList;
        this.context = context;
        String email = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : "";
        this.isAdmin = email != null && email.endsWith("@admin.com");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Material material = materialList.get(position);
        holder.titleView.setText(material.getTitle());
        holder.dateView.setText(new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(new Date(material.getTimestamp())));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(material.getLink()));
            context.startActivity(intent);
        });
        if (isAdmin) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Delete Material")
                        .setMessage("Are you sure you want to delete this material?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            FirebaseFirestore.getInstance().collection("materials")
                                    .document(material.getId())
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
        return materialList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, dateView;
        ImageButton deleteButton;
        ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.materialTitleView);
            dateView = itemView.findViewById(R.id.materialDateView);
            deleteButton = itemView.findViewById(R.id.materialDeleteButton); // Add this to your layout
        }
    }
}