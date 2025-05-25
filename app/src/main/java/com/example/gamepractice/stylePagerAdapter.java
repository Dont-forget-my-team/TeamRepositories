package com.example.gamepractice;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StylePagerAdapter extends FragmentStateAdapter {

    public StylePagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new StyleSlide1Fragment();
            case 1: return new StyleSlide2Fragment();
            case 2: return new StyleSlide3Fragment();
            default: return new StyleSlide1Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // 슬라이드 수만큼
    }
}
