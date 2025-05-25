package com.example.teamproject_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;            // ← 이미 추가되어 있어야 합니다
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_7_1.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) onCreate 진입 로그
        Log.d(TAG, "onCreate 호출됨");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 2) 버튼 참조 로그
        Button btnNext = findViewById(R.id.btnNext);
        Log.d(TAG, "btnNext is " + (btnNext == null ? "NULL" : "NOT NULL"));

        // 3) 시스템 바 인셋 처리
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                }
        );

        // 4) 클릭 리스너 등록
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "Next 버튼 클릭!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, NextActivity.class));
            });
        }
    }
}
