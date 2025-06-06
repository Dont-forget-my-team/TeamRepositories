package com.example.gamepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class StyleSlide1Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_style_slide1, container, false);

        ImageView windowView = view.findViewById(R.id.imageView2);
        ImageView sofaView = view.findViewById(R.id.soapview);
        ImageView carpetView = view.findViewById(R.id.carpetview);
        ImageView wallhangerView = view.findViewById(R.id.wallhangerview);
        ImageView wallpaperView = view.findViewById(R.id.roomImage); // wallpaper는 roomImage로 처리됨


        // ✅ ViewModel 연결
        StyleViewModel viewModel = new ViewModelProvider(requireActivity()).get(StyleViewModel.class);

        // ✅ 창문 스타일 옵저빙
        viewModel.getSelectedWindowStyle().observe(getViewLifecycleOwner(), selectedWindow -> {
            if (selectedWindow == null) return;

            switch (selectedWindow) {
                case "window_design_1":
                    windowView.setImageResource(R.drawable.window1); break;
                case "window_design_2":
                    windowView.setImageResource(R.drawable.window_design__2); break;
                case "window_design_3":
                    windowView.setImageResource(R.drawable.window_design__3); break;
            }
            windowView.setVisibility(View.VISIBLE);
        });

        // ✅ 소파 스타일 옵저빙
        viewModel.getSelectedSofaStyle().observe(getViewLifecycleOwner(), selectedSofa -> {
            if (selectedSofa == null) return;

            switch (selectedSofa) {
                case "sofa_design_1":
                    sofaView.setImageResource(R.drawable.sopa1); break;
                case "sofa_design_2":
                    sofaView.setImageResource(R.drawable.sopa2); break;
                case "sofa_design_3":
                    sofaView.setImageResource(R.drawable.sopa3); break;
            }
            sofaView.setVisibility(View.VISIBLE);
        });

        viewModel.getSelectedCarpetStyle().observe(getViewLifecycleOwner(), selectedCarpet -> {
            if (selectedCarpet == null) return;

            switch (selectedCarpet) {
                case "carpet_design_1":
                    carpetView.setImageResource(R.drawable.carpet1); break;
                case "carpet_design_2":
                    carpetView.setImageResource(R.drawable.carpet2); break;
                case "carpet_design_3":
                    carpetView.setImageResource(R.drawable.carpet3); break;
            }
            carpetView.setVisibility(View.VISIBLE);
        });

        viewModel.getSelectedWallHangerStyle().observe(getViewLifecycleOwner(), selectedWallHanger -> {
            if (selectedWallHanger == null) return;

            switch (selectedWallHanger) {
                case "wallhanger_design_1":
                    wallhangerView.setImageResource(R.drawable.wallhager1); break;
                case "wallhanger_design_2":
                    wallhangerView.setImageResource(R.drawable.wallhanger2); break;
                case "wallhanger_design_3":
                    wallhangerView.setImageResource(R.drawable.wallhanger3); break;
            }
            wallhangerView.setVisibility(View.VISIBLE);
        });

        viewModel.getSelectedWallpaperStyle().observe(getViewLifecycleOwner(), selectedWallpaper -> {
            if (selectedWallpaper == null) return;

            switch (selectedWallpaper) {
                case "wallpaper_design_1":
                    wallpaperView.setImageResource(R.drawable.room1); break;
                case "wallpaper_design_2":
                    wallpaperView.setImageResource(R.drawable.room2); break;
                case "wallpaper_design_3":
                    wallpaperView.setImageResource(R.drawable.room3); break;
            }
        });

        return view;
    }
}
