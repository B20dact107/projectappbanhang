package com.example.project1722.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project1722.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private EditText etk,emk,emk1;
    private Button btlogin,btsingup;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mauth = FirebaseAuth.getInstance();
        etk=findViewById(R.id.edittk);
        emk=findViewById(R.id.editmk);
        emk1=findViewById(R.id.editTextRePassword);
        btlogin=findViewById(R.id.login);
        btsingup=findViewById(R.id.singup);
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btsingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singup();
            }
        });

    }

    private void login() {
        Intent i = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(i);

    }

    private void singup() {
        String emai,pass,pass1;
        emai=etk.getText().toString();
        pass=emk.getText().toString();
        pass1=emk1.getText().toString();
        if(pass.equalsIgnoreCase(pass1)==false){
            showAlertDialog("Lỗi", "Mật khẩu và mật khẩu xác thực không khớp");
            return;
        }
        if(TextUtils.isEmpty(emai)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(pass1)){
            showAlertDialog("Lỗi", "Vui lòng nhập tài khoản và mật khẩu");
            return;
        }
        mauth.createUserWithEmailAndPassword(emai,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showSuccessDialog();
//                    Toast.makeText(getApplicationContext(),"Tao tai khoan thanh cong",Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(SingupActivity.this,MainActivity.class);
//                    startActivity(i);

                }else{
                    showAlertDialog("Lỗi", "Tạo tài khoản không thành công");
                }
            }
        });

    }
    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng ký thành công")
                .setMessage("Tài khoản của bạn đã được tạo thành công.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}