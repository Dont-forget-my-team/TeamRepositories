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
import androidx.lifecycle.ViewModelProvider;

public class StyleSlide2Fragment extends Fragment {

    private ImageView editWindowIcon;
    private LinearLayout styleSelectionBar;
    private boolean isStyleBarVisible = false;


    private ImageView sofaView; // 소파 뷰

    private GameUIController uiController;

    private TextView coinText;
    private int currentCoins;

    private TextView pointAddedText;
    private TextView pointDeductedText;

    private ImageView overlayWindowImage;

    private LinearLayout styleOptions;

    private StyleViewModel viewModel;

    private ImageView carpetView;
    private ImageView wallhangerView;
    private ImageView wallpaperView;



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

        viewModel = new ViewModelProvider(requireActivity()).get(StyleViewModel.class);


        View view = inflater.inflate(R.layout.fragment_style_slide2, container, false);

        // ✅ 테스트용 초기화: 코인, 구매 상태, 선택 스타일 리셋
        SharedPreferencesActivity.resetForTesting(requireContext());

        // 기본 UI 초기화
        editWindowIcon = view.findViewById(R.id.editWindowIcon);
        styleSelectionBar = view.findViewById(R.id.styleSelectionBar);
        styleOptions = view.findViewById(R.id.styleOptions);
        overlayWindowImage = view.findViewById(R.id.imageView2);
        overlayWindowImage.setVisibility(View.INVISIBLE);

        // ✅ 소파 뷰 초기화 및 숨기기
        sofaView = view.findViewById(R.id.soapview);
        sofaView.setVisibility(View.INVISIBLE);

        carpetView = view.findViewById(R.id.carpetview);
        carpetView.setVisibility(View.INVISIBLE);

        wallhangerView = view.findViewById(R.id.wallhangerview);
        wallhangerView.setVisibility(View.INVISIBLE);

        wallpaperView = view.findViewById(R.id.roomImage); // wallpaper는 항상 visible로 사용

        coinText = requireActivity().findViewById(R.id.coinText);
        pointAddedText = requireActivity().findViewById(R.id.pointAddedText);
        pointDeductedText = requireActivity().findViewById(R.id.pointDeductedText);

        // 초기 코인 불러오기 (500으로 리셋된 상태)
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

        // 소파 스타일
        btnSofa.setOnClickListener(v -> {
            styleOptions.removeAllViews();
            addStyleItem(styleOptions, R.drawable.sopa1, 50, "sofa_design_1");
            addStyleItem(styleOptions, R.drawable.sopa2, 50, "sofa_design_2");
            addStyleItem(styleOptions, R.drawable.sopa3, 50, "sofa_design_3");
        });

        // 기타 카테고리 (임시)
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

        // ✅ 초기 스타일 적용 생략 (초기화되므로 적용할 스타일 없음)

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

        // ViewModel 가져오기
        StyleViewModel viewModel = new ViewModelProvider(requireActivity()).get(StyleViewModel.class);

        LinearLayout itemLayout = new LinearLayout(context);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        itemLayout.setPadding(16, 16, 16, 16);

        // ✅ 선택된 스타일 여부 확인
        boolean isWindow = itemKey.startsWith("window");
        boolean isSofa = itemKey.startsWith("sofa");

        boolean isPurchased = SharedPreferencesActivity.isPurchased(context, itemKey);
        boolean isSelected = false;

        if (isWindow) {
            isSelected = itemKey.equals(SharedPreferencesActivity.getSelectedWindowStyle(context));
        } else if (isSofa) {
            isSelected = itemKey.equals(SharedPreferencesActivity.getSelectedSofaStyle(context));
        }

        // ✅ 선택된 항목이면 외곽선 강조
        if (isSelected) {
            itemLayout.setBackgroundResource(R.drawable.border_selected);
        }

        // 이미지
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageResId);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(120, 120);
        imageParams.setMargins(0, 0, 0, 8);
        imageView.setLayoutParams(imageParams);
        itemLayout.addView(imageView);

        // 버튼
        Button button = new Button(context);
        button.setTag(itemKey);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(180, 80);
        button.setLayoutParams(buttonParams);
        button.setTextSize(16);
        button.setAllCaps(false);
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
        button.setTextColor(android.graphics.Color.BLACK);
        button.setPadding(12, 0, 0, 0);

        if (isPurchased) {
            if (isSelected) {
                button.setText("적용됨");
                button.setEnabled(false);
            } else {
                button.setText("적용하기");
                button.setOnClickListener(v -> {
                    applyStyle(itemKey);

                    if (isWindow) {
                        SharedPreferencesActivity.saveSelectedWindowStyle(context, itemKey);
                        viewModel.setSelectedWindowStyle(itemKey); // 🔹 ViewModel 반영
                    } else if (isSofa) {
                        SharedPreferencesActivity.saveSelectedSofaStyle(context, itemKey);
                        viewModel.setSelectedSofaStyle(itemKey); // 🔹 ViewModel 반영
                    }

                    Toast.makeText(context, "스타일이 적용됐어요!", Toast.LENGTH_SHORT).show();

                    // UI 갱신
                    styleOptions.removeAllViews();
                    if (isWindow) {
                        addStyleItem(styleOptions, R.drawable.window1, 50, "window_design_1");
                        addStyleItem(styleOptions, R.drawable.window_design__2, 50, "window_design_2");
                        addStyleItem(styleOptions, R.drawable.window_design__3, 50, "window_design_3");
                    } else if (isSofa) {
                        addStyleItem(styleOptions, R.drawable.sopa1, 50, "sofa_design_1");
                        addStyleItem(styleOptions, R.drawable.sopa2, 50, "sofa_design_2");
                        addStyleItem(styleOptions, R.drawable.sopa3, 50, "sofa_design_3");
                    }
                });
            }
        } else {
            button.setText("★ " + cost);
            button.setOnClickListener(v -> {
                if (currentCoins >= cost) {
                    currentCoins -= cost;
                    SharedPreferencesActivity.saveCoins(context, currentCoins);
                    SharedPreferencesActivity.savePurchase(context, itemKey, true);
                    updateCoinText();

                    if (isWindow) {
                        SharedPreferencesActivity.saveSelectedWindowStyle(context, itemKey);
                        viewModel.setSelectedWindowStyle(itemKey); // 🔹 ViewModel 반영
                    } else if (isSofa) {
                        SharedPreferencesActivity.saveSelectedSofaStyle(context, itemKey);
                        viewModel.setSelectedSofaStyle(itemKey); // 🔹 ViewModel 반영
                    }

                    applyStyle(itemKey);
                    showPointChange(pointDeductedText, "-" + cost);
                    Toast.makeText(context, "스타일을 구매하고 적용했어요!", Toast.LENGTH_SHORT).show();

                    // 다시 버튼 UI 갱신
                    styleOptions.removeAllViews();
                    if (isWindow) {
                        addStyleItem(styleOptions, R.drawable.window1, 50, "window_design_1");
                        addStyleItem(styleOptions, R.drawable.window_design__2, 50, "window_design_2");
                        addStyleItem(styleOptions, R.drawable.window_design__3, 50, "window_design_3");
                    } else if (isSofa) {
                        addStyleItem(styleOptions, R.drawable.sopa1, 50, "sofa_design_1");
                        addStyleItem(styleOptions, R.drawable.sopa2, 50, "sofa_design_2");
                        addStyleItem(styleOptions, R.drawable.sopa3, 50, "sofa_design_3");
                    }
                } else {
                    Toast.makeText(context, "포인트가 부족해요!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        itemLayout.addView(button);
        container.addView(itemLayout);
    }




    private void applyStyle(String itemKey) {
        switch (itemKey) {
            case "window_design_1":
                overlayWindowImage.setImageResource(R.drawable.window1);
                overlayWindowImage.setVisibility(View.VISIBLE);
                break;
            case "window_design_2":
                overlayWindowImage.setImageResource(R.drawable.window_design__2);
                overlayWindowImage.setVisibility(View.VISIBLE);
                break;
            case "window_design_3":
                overlayWindowImage.setImageResource(R.drawable.window_design__3);
                overlayWindowImage.setVisibility(View.VISIBLE);
                break;

            case "sofa_design_1":
                sofaView.setImageResource(R.drawable.sopa1);
                sofaView.setVisibility(View.VISIBLE);
                break;
            case "sofa_design_2":
                sofaView.setImageResource(R.drawable.sopa2);
                sofaView.setVisibility(View.VISIBLE);
                break;
            case "sofa_design_3":
                sofaView.setImageResource(R.drawable.sopa3);
                sofaView.setVisibility(View.VISIBLE);
                break;
            case "carpet_design_1":
                carpetView.setImageResource(R.drawable.carpet1);
                carpetView.setVisibility(View.VISIBLE);
                break;
            case "carpet_design_2":
                carpetView.setImageResource(R.drawable.carpet2);
                carpetView.setVisibility(View.VISIBLE);
                break;
            case "carpet_design_3":
                carpetView.setImageResource(R.drawable.carpet3);
                carpetView.setVisibility(View.VISIBLE);
                break;

            case "wallhanger_design_1":
                wallhangerView.setImageResource(R.drawable.wallhager1);
                wallhangerView.setVisibility(View.VISIBLE);
                break;
            case "wallhanger_design_2":
                wallhangerView.setImageResource(R.drawable.wallhanger2);
                wallhangerView.setVisibility(View.VISIBLE);
                break;
            case "wallhanger_design_3":
                wallhangerView.setImageResource(R.drawable.wallhanger3);
                wallhangerView.setVisibility(View.VISIBLE);
                break;

            case "wallpaper_design_1":
                wallpaperView.setImageResource(R.drawable.room1);
                break;
            case "wallpaper_design_2":
                wallpaperView.setImageResource(R.drawable.room2);
                break;
            case "wallpaper_design_3":
                wallpaperView.setImageResource(R.drawable.room3);
                break;

            default:
                overlayWindowImage.setVisibility(View.INVISIBLE);
                sofaView.setVisibility(View.INVISIBLE);
                break;
        }
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



