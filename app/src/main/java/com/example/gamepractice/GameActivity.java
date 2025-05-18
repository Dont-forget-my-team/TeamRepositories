package com.example.gamepractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ImagePagerAdapter pagerAdapter;
    private List<Integer> imageList;
    private ImageView starIcon;
    private TextView scoreTextView; // 추가
    private ImageView settingsIcon;
    private TextView houseTextView;
    private ImageView prevButton;
    private ImageView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); // activity_main 대신 activity_game 사용

        // UI 요소 초기화
        viewPager = findViewById(R.id.viewPager);
        starIcon = findViewById(R.id.starIcon);
        scoreTextView = findViewById(R.id.scoreTextView); // 추가
        settingsIcon = findViewById(R.id.settingsIcon);
        houseTextView = findViewById(R.id.houseTextView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        // 이미지 리스트 초기화 (res/drawable 폴더에 있는 이미지 리소스 ID)
        imageList = new ArrayList<>();
        imageList.add(R.drawable.signal_9630065); // 실제 이미지 리소스 ID로 변경
        imageList.add(R.drawable.signal_9630065); // 실제 이미지 리소스 ID로 변경
        imageList.add(R.drawable.signal_9630065); // 실제 이미지 리소스 ID로 변경
        // ... 더 많은 이미지 추가 ...

        // 어댑터 생성 및 설정
        pagerAdapter = new ImagePagerAdapter(this, imageList); // this (Context) 전달
        viewPager.setAdapter(pagerAdapter);

        // 이전/다음 버튼 클릭 리스너 (선택 사항)
        prevButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        nextButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < imageList.size() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        });

        // 상단 아이콘 클릭 리스너 (예시)
        starIcon.setOnClickListener(v -> {
            // 별 아이콘 클릭 시 동작 구현
        });

        settingsIcon.setOnClickListener(v -> {
            // 설정 아이콘 클릭 시 동작 구현
        });
    }
}