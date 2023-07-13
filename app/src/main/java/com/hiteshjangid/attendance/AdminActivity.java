package com.hiteshjangid.attendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hiteshjangid.attendance.common.Common;
import com.hiteshjangid.attendance.databinding.ActivityAdminBinding;
import com.hiteshjangid.attendance.ui.grade.HomeActivity;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(view -> {
            String password = binding.password.getText().toString().trim();

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(AdminActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = new ProgressDialog(AdminActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                authenticateAdmin(password);
            }
        });
    }

    private void authenticateAdmin(String password) {
        // Replace the following condition with your secure authentication logic
        if (password.equals("123")) {
            Intent intent = new Intent(AdminActivity.this, HomeActivity.class);
            Common.currentUserType = "admin";
            progressDialog.dismiss();
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(AdminActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
