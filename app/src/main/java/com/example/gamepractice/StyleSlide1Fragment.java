package com.example.gamepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StyleSlide1Fragment extends Fragment {

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_style_slide1, container, false);

        ImageView windowView = view.findViewById(R.id.imageView2);
        ImageView sofaView = view.findViewById(R.id.soapview);

        String selectedWindow = SharedPreferencesActivity.getSelectedWindowStyle(requireContext());
        if (selectedWindow != null) {
            switch (selectedWindow) {
                case "window_design_1":
                    windowView.setImageResource(R.drawable.window1); break;
                case "window_design_2":
                    windowView.setImageResource(R.drawable.window_design__2); break;
                case "window_design_3":
                    windowView.setImageResource(R.drawable.window_design__3); break;
            }
            windowView.setVisibility(View.VISIBLE);
        }

        String selectedSofa = SharedPreferencesActivity.getSelectedSofaStyle(requireContext());
        if (selectedSofa != null) {
            switch (selectedSofa) {
                case "sofa_design_1":
                    sofaView.setImageResource(R.drawable.sopa1); break;
                case "sofa_design_2":
                    sofaView.setImageResource(R.drawable.sopa2); break;
                case "sofa_design_3":
                    sofaView.setImageResource(R.drawable.sopa3); break;
            }
            sofaView.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
