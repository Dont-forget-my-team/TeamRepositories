package com.example.gamepractice;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private StylePagerAdapter pagerAdapter;
    private ImageView starIcon;
    private TextView scoreTextView;
    private ImageView settingsIcon;
    private TextView houseTextView;
    private ImageView prevButton;
    private ImageView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // UI 요소 초기화
        viewPager = findViewById(R.id.viewPager);
        starIcon = findViewById(R.id.starIcon);
        scoreTextView = findViewById(R.id.scoreTextView);
        settingsIcon = findViewById(R.id.settingsIcon);
        houseTextView = findViewById(R.id.houseTextView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        // 프래그먼트 기반 어댑터 설정
        pagerAdapter = new StylePagerAdapter(this); // ← 이 부분이 핵심!
        viewPager.setAdapter(pagerAdapter);

        // 버튼 이벤트
        prevButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        nextButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < pagerAdapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        });

        // 별, 설정 아이콘 클릭 시 동작 추가 가능
    }
}
