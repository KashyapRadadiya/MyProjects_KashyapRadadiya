package com.kashyap.collegecompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.kashyap.collegecompanion.MaterialAdapter; // Add this import
import com.kashyap.collegecompanion.R;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MaterialsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MaterialAdapter adapter;
    private ArrayList<Material> materialList = new ArrayList<>();
    private FirebaseFirestore db;
    private MaterialButton addMaterialButton; // <-- Changed from ImageButton to MaterialButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);

        recyclerView = findViewById(R.id.materialRecyclerView);
        addMaterialButton = findViewById(R.id.addMaterialButton); // This now matches the MaterialButton in XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MaterialAdapter(materialList, this); // Use external adapter and pass context
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadMaterials();

        // Show add button only for Admin
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email != null && email.endsWith("@admin.com")) {
            addMaterialButton.setVisibility(View.VISIBLE);
        } else {
            addMaterialButton.setVisibility(View.GONE);
        }

        addMaterialButton.setOnClickListener(v -> showAddMaterialDialog());
    }

    private void loadMaterials() {
        db.collection("materials")
                .orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Failed to load materials.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    materialList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Material material = doc.toObject(Material.class);
                        if (material != null) {
                            material.setId(doc.getId()); // Set Firestore document ID
                            materialList.add(material);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddMaterialDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_material, null);
        EditText titleInput = dialogView.findViewById(R.id.materialTitleInput);
        EditText linkInput = dialogView.findViewById(R.id.materialLinkInput);

        new AlertDialog.Builder(this)
                .setTitle("Add Material")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = titleInput.getText().toString().trim();
                    String link = linkInput.getText().toString().trim();
                    if (!title.isEmpty() && !link.isEmpty()) {
                        postMaterial(title, link);
                    } else {
                        Toast.makeText(this, "Please enter both title and link.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void postMaterial(String title, String link) {
        Map<String, Object> material = new HashMap<>();
        material.put("title", title);
        material.put("link", link);
        material.put("timestamp", System.currentTimeMillis()); // Add timestamp for sorting
        db.collection("materials").add(material)
                .addOnSuccessListener(docRef -> Toast.makeText(this, "Material added.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add material.", Toast.LENGTH_SHORT).show());
    }
}