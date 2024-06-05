package com.example.project1722.Activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etk,emk;
    private Button btlogin,btsingup;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth = FirebaseAuth.getInstance();
        etk=findViewById(R.id.edittk);
        emk=findViewById(R.id.editmk);
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
        findViewById(R.id.fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.facebook.com/login");
                       // "https://www.facebook.com/login"
            }
        });

        findViewById(R.id.gg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Ftakeout.google.com%2F%3Fhl%3Dvi&followup=https%3A%2F%2Ftakeout.google.com%2F%3Fhl%3Dvi&hl=vi&ifkv=AaSxoQzvGR4OEJloQiD1ZMk6oqya3D9LONXUIxsAwQZrnOrJn8T6pmWB_AIT9jTtW5TyVOlbDbIy1A&osid=1&passive=1209600&flowName=GlifWebSignIn&flowEntry=ServiceLogin&dsh=S571283906%3A1716472623976737&ddm=0");
            }
        });

    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void singup() {
        Intent i = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(i);

    }

    private void login() {
        String emai,pass;
        emai=etk.getText().toString();
        pass=emk.getText().toString();
        if(TextUtils.isEmpty(emai)||TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Vui lòng nhập tài khoản và mật khẩu",Toast.LENGTH_SHORT).show();
            return;
        }
        mauth.signInWithEmailAndPassword(emai,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = mauth.getCurrentUser();
                if(task.isSuccessful()){
                    String uid = user.getUid();
                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.putExtra("user_email", emai);  // Truyền email sang MainActivity
                    i.putExtra("user_uid", uid);
                    startActivity(i);

                }else{
                    showAlertDialog("Lỗi", "Tạo tài khoản hoặc  mật khẩu không chính xác!");
                }
            }
        });
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