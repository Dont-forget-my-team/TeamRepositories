package com.example.gamepractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class GameActivity extends AppCompatActivity implements  StyleSlide2Fragment.GameUIController{

    private ViewPager2 viewPager;
    private stylePagerAdapter pagerAdapter;
    private ImageView starIcon;
    private TextView scoreTextView;
    private ImageView settingsIcon;
    private TextView houseTextView;
    private ImageView prevButton;
    private ImageView nextButton;

    private ImageView editWindowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ⭐ 자동 로그인 체크
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // ❌ 로그인 기록이 없다면 로그인 화면으로
            Intent intent = new Intent(GameActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // 이 액티비티 종료
            return;
        }






        // ✅ 로그인 된 상태라면 게임 화면 표시
        setContentView(R.layout.activity_game);
        // UI 요소 초기화
        viewPager = findViewById(R.id.viewPager);
        starIcon = findViewById(R.id.starIcon);
        settingsIcon = findViewById(R.id.settingsIcon);
        houseTextView = findViewById(R.id.houseTextView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        editWindowIcon = findViewById(R.id.editWindowIcon);

        // 프래그먼트 기반 어댑터 설정
        pagerAdapter = new stylePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // 버튼 이벤트
        prevButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        //edit이벤트


        nextButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < pagerAdapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        });

        //페이지 이름변경
        viewPager.setAdapter(pagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0:
                        houseTextView.setText("HOUSE");
                        break;
                    case 1:
                        houseTextView.setText("STYLE");
                        break;
                    case 2:
                        houseTextView.setText("CALENDER");
                        break;
                    case 3:
                        houseTextView.setText("CHART");
                        break;
                }
            }
        });


        // 별, 설정 아이콘 클릭 시 동작 추가 가능

    }
    public void toggleBottomUI(boolean show) {
        int visibility = show ?
                View.VISIBLE : View.INVISIBLE;
        boolean enabled = show;

        houseTextView.setVisibility(visibility);
        prevButton.setVisibility(visibility);
        nextButton.setVisibility(visibility);

        prevButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
    }

    public void updateCoinTextFromActivity(int currentCoins) {
        TextView coinText = findViewById(R.id.coinText);
        if (coinText != null) {
            coinText.setText("★ " + currentCoins);
            Log.d("CoinsDebug", "✅ GameActivity에서 코인 텍스트 업데이트됨: " + currentCoins);
        } else {
            Log.d("CoinsDebug", "❌ coinText is NULL in GameActivity");
        }
    }
}

