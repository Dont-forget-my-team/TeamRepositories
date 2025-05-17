package com.example.gamepractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Login2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2); // login2.xml

        Button btnToGame = findViewById(R.id.buttonName); // 버튼 ID가 "buttonName"이라고 가정
        btnToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login2Activity.this, GameActivity.class);
                startActivity(intent);
                finish(); // 이전 액티비티 종료 (Login2Activity)
            }
        });
    }
}
