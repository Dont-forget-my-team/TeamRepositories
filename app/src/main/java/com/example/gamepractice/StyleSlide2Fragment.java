package com.example.gamepractice;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StyleSlide2Fragment extends Fragment {

    private ImageView editWindowIcon;
    private LinearLayout styleSelectionBar;
    private boolean isStyleBarVisible = false;

    private GameUIController uiController;

    // Activity에 UI 변경을 요청할 인터페이스
    public interface GameUIController {
        void toggleBottomUI(boolean show);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GameUIController) {
            uiController = (GameUIController) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GameUIController");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_style_slide2, container, false);

        editWindowIcon = view.findViewById(R.id.editWindowIcon);
        styleSelectionBar = view.findViewById(R.id.styleSelectionBar);

        editWindowIcon.setOnClickListener(v -> toggleStyleBar());

        return view;
    }

    private void toggleStyleBar() {
        isStyleBarVisible = !isStyleBarVisible;

        if (isStyleBarVisible) {
            styleSelectionBar.setAlpha(0f);
            styleSelectionBar.setVisibility(View.VISIBLE);
            styleSelectionBar.animate().alpha(1f).setDuration(300).start();
        } else {
            styleSelectionBar.animate().alpha(0f).setDuration(300)
                    .withEndAction(() -> styleSelectionBar.setVisibility(View.INVISIBLE))
                    .start();
        }

        if (uiController != null) {
            uiController.toggleBottomUI(!isStyleBarVisible);
        }
    }
}

