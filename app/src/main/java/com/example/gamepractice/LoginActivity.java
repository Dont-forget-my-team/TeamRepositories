package com.example.gamepractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 로그인 상태 초기화 (테스트용)
        SharedPreferences.Editor testEditor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
        testEditor.remove("isLoggedIn"); // 또는 testEditor.putBoolean("isLoggedIn", false);
        testEditor.apply();


        // 🔒 로그인한 적이 있다면 바로 GameActivity로 이동
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // 로그인 로직
                if (email.equals("test@example.com") && password.equals("1234")) {
                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                    // ✅ 로그인 상태 저장
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    // Login2Activity로 이동
                    Intent intent = new Intent(LoginActivity.this, Login2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패. 이메일 또는 비밀번호 확인.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

