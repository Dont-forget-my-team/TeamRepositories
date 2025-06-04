package com.example.gamepractice;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StyleSlide2Fragment extends Fragment {

    private ImageView editWindowIcon;
    private LinearLayout styleSelectionBar;
    private boolean isStyleBarVisible = false;

    private GameUIController uiController;

    private TextView coinText;
    private int currentCoins;

    private TextView pointAddedText;
    private TextView pointDeductedText;

    private ImageView overlayWindowImage;

    private LinearLayout styleOptions;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_style_slide2, container, false);

        // 초기화
        editWindowIcon = view.findViewById(R.id.editWindowIcon);
        styleSelectionBar = view.findViewById(R.id.styleSelectionBar);
        styleOptions = view.findViewById(R.id.styleOptions);
        overlayWindowImage = view.findViewById(R.id.imageView2);
        overlayWindowImage.setVisibility(View.INVISIBLE);

        coinText = requireActivity().findViewById(R.id.coinText);
        pointAddedText = requireActivity().findViewById(R.id.pointAddedText);
        pointDeductedText = requireActivity().findViewById(R.id.pointDeductedText);

        currentCoins = SharedPreferencesActivity.loadCoins(requireContext());
        updateCoinText();

        editWindowIcon.setOnClickListener(v -> toggleStyleBar());

        // 카테고리 버튼
        Button btnWindow = view.findViewById(R.id.btn_window);
        Button btnSofa = view.findViewById(R.id.btn_sofa);
        Button btnCabinet = view.findViewById(R.id.btn_cabinet);
        Button btnCarpet = view.findViewById(R.id.btn_carpet);
        Button btnWallpaper = view.findViewById(R.id.btn_wallpaper);

        // 창문 스타일
        btnWindow.setOnClickListener(v -> {
            styleOptions.removeAllViews();
            addStyleItem(styleOptions, R.drawable.window1, 50, "window_design_1");
            addStyleItem(styleOptions, R.drawable.window_design__2, 50, "window_design_2");
            addStyleItem(styleOptions, R.drawable.window_design__3, 50, "window_design_3");
        });

        // 소파 스타일 (예시용)
        btnSofa.setOnClickListener(v -> {
            styleOptions.removeAllViews();
            addStyleItem(styleOptions, R.drawable.sopa1, 50, "sofa_design_1");
            addStyleItem(styleOptions, R.drawable.sopa2, 50, "sofa_design_2");
            addStyleItem(styleOptions, R.drawable.sopa3, 50, "sofa_design_3");
        });

        // 나머지 카테고리도 필요 시 구현
        btnCabinet.setOnClickListener(v -> {
            styleOptions.removeAllViews();
            Toast.makeText(getContext(), "벽장 스타일 준비 중!", Toast.LENGTH_SHORT).show();
        });

        btnCarpet.setOnClickListener(v -> {
            styleOptions.removeAllViews();
            Toast.makeText(getContext(), "카펫 스타일 준비 중!", Toast.LENGTH_SHORT).show();
        });

        btnWallpaper.setOnClickListener(v -> {
            styleOptions.removeAllViews();
            Toast.makeText(getContext(), "벽지 스타일 준비 중!", Toast.LENGTH_SHORT).show();
        });

        // 초기 적용 (구매 내역 확인)
        if (SharedPreferencesActivity.isPurchased(requireContext(), "window_design_1")) {
            applyStyle("window_design_1");
        } else if (SharedPreferencesActivity.isPurchased(requireContext(), "window_design_2")) {
            applyStyle("window_design_2");
        } else if (SharedPreferencesActivity.isPurchased(requireContext(), "window_design_3")) {
            applyStyle("window_design_3");
        }

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

    private void addStyleItem(LinearLayout container, int imageResId, int cost, String itemKey) {
        Context context = requireContext();



        LinearLayout itemLayout = new LinearLayout(context);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        itemLayout.setPadding(16, 16, 16, 16);

        // 이미지뷰 크게
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageResId);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(120, 120); // 크게 조정
        imageParams.setMargins(0, 0, 0, 8); // 이미지 아래 간격
        imageView.setLayoutParams(imageParams);
        itemLayout.addView(imageView);

        // 버튼 크게 + 글자 큼
        Button button = new Button(context);
        button.setText("★ " + cost);
        button.setTag(itemKey);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(180, 80); // 넓고 높게
        button.setLayoutParams(buttonParams);
        button.setTextSize(16); // 글자 키움
        button.setAllCaps(false); // 소문자 유지
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
        button.setTextColor(android.graphics.Color.BLACK);
        button.setPadding(12, 0, 0, 0);
        itemLayout.addView(button);

        // 구매 상태 확인
        if (SharedPreferencesActivity.isPurchased(context, itemKey)) {
            button.setEnabled(false);
            button.setText("구매 완료");
        } else {
            button.setOnClickListener(v -> {
                if (currentCoins >= cost) {
                    currentCoins -= cost;
                    SharedPreferencesActivity.saveCoins(context, currentCoins);
                    SharedPreferencesActivity.savePurchase(context, itemKey, true);
                    updateCoinText();
                    button.setEnabled(false);
                    button.setText("구매 완료");
                    applyStyle(itemKey);
                    showPointChange(pointDeductedText, "-" + cost);
                    Toast.makeText(context, "스타일을 구매했어요!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "포인트가 부족해요!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        container.addView(itemLayout);
    }


    private void applyStyle(String itemKey) {
        int drawableRes;
        switch (itemKey) {
            case "window_design_1":
                drawableRes = R.drawable.window1;
                break;
            case "window_design_2":
                drawableRes = R.drawable.window_design__2;
                break;
            case "window_design_3":
                drawableRes = R.drawable.window_design__3;
                break;
            case "sofa_design_1":
                drawableRes = R.drawable.sopa1;
                break;
            case "sofa_design_2":
                drawableRes = R.drawable.sopa2;
                break;
            case "sofa_design_3":
                drawableRes = R.drawable.sopa3;
                break;
            default:
                overlayWindowImage.setVisibility(View.INVISIBLE);
                return;
        }

        overlayWindowImage.setImageResource(drawableRes);
        overlayWindowImage.setVisibility(View.VISIBLE);
        overlayWindowImage.bringToFront();
    }

    private void updateCoinText() {
        coinText.setText("★ " + currentCoins);
    }

    private void showPointChange(TextView textView, String text) {
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
        textView.setAlpha(0f);
        textView.setTranslationY(30f);
        textView.animate()
                .alpha(1f)
                .translationYBy(-30f)
                .setDuration(600)
                .withEndAction(() -> textView.setVisibility(View.GONE))
                .start();
    }

    public void addCoins(int amount) {
        currentCoins += amount;
        SharedPreferencesActivity.saveCoins(requireContext(), currentCoins);
        updateCoinText();
        showPointChange(pointAddedText, "+" + amount);
    }
}



