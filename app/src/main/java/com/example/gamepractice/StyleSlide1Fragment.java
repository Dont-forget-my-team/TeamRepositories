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

        return view;
    }
}
