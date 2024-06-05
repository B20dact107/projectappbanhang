package com.example.project1722.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1722.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    private TextView profileEmail;
    private Button logoutButton,manager;
    private ImageView backbtn;
    private static final String ADMIN_UID = "RcqocMr82TaiRHj3oFevPL6dCwL2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        profileEmail = findViewById(R.id.profile_email);
        logoutButton = findViewById(R.id.logout_button);
        backbtn =  findViewById(R.id.backBtn);
        manager=findViewById(R.id.manager);

        String email = getIntent().getStringExtra("user_email");

        if (email != null) {
            profileEmail.setText(email);
        } else {
            profileEmail.setText("Email không khả dụng");
        }
        if (currentUser != null) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            //RcqocMr82TaiRHj3oFevPL6dCwL2
            System.out.println(currentUser.getUid());
            profileEmail.setText(currentUser.getEmail());
        }
        backbtn.setOnClickListener(v -> finish());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    if (currentUser.getUid().equals(ADMIN_UID)) {
                        startActivity(new Intent(ProfileActivity.this, ManageActivity.class));
                    } else {

                        Toast.makeText(ProfileActivity.this, "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}