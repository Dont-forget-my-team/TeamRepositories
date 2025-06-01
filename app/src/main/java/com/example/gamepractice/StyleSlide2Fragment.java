package com.example.gamepractice;

import android.content.Context;
import android.os.Bundle;
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

    private TextView coinText;            // 현재 포인트를 표시하는 텍스트
    private int currentCoins;             // 현재 보유 포인트

    private TextView pointAddedText;      // +포인트 애니메이션
    private TextView pointDeductedText;   // -포인트 애니메이션

    // --- 화면에 스타일을 덮어쓸 Overlay ImageView ---
    private ImageView overlayWindowImage;

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

        // (1) 스타일 토글 관련 초기화
        editWindowIcon = view.findViewById(R.id.editWindowIcon);
        styleSelectionBar = view.findViewById(R.id.styleSelectionBar);
        editWindowIcon.setOnClickListener(v -> toggleStyleBar());

        // (2) 포인트 UI 요소 초기화 (Activity 레이아웃 안에 있는 TextView들)
        coinText = requireActivity().findViewById(R.id.coinText);
        pointAddedText = requireActivity().findViewById(R.id.pointAddedText);
        pointDeductedText = requireActivity().findViewById(R.id.pointDeductedText);

        // (3) 오버레이용 ImageView 초기화 (레이아웃에 정의되어 있어야 함)
        overlayWindowImage = view.findViewById(R.id.imageView2);
        // 처음에는 보이지 않도록 설정
        overlayWindowImage.setVisibility(View.INVISIBLE);

        // (4) 현재 포인트 불러오기
        currentCoins = SharedPreferencesActivity.loadCoins(requireContext());
        updateCoinText();

        // (5) 각 스타일 구매 버튼 설정 (버튼 ID와 각 아이템의 고유 key를 함께 전달)
        setupBuyButton(view, R.id.buybutton_window1, 50, "window_design_1");
        setupBuyButton(view, R.id.buybutton_window2, 50, "window_design_2");
        setupBuyButton(view, R.id.buybutton_window3, 50, "window_design_3");

        // (6) **최초 로딩 시, 이미 구매된 스타일이 있으면 미리 적용하기**
        // 예시: 만약 여러 개 중 “하나만” 적용한다면, 먼저 구매된 키를 찾아 applyStyle(...)
        // → 여러 개 동시 적용이 아니라 마지막에 산 아이템만 적용하고 싶다면 SharedPreferences에 “current_style” 같은 키를 따로 저장해두면 편합니다.
        // 여기서는 단순히 “window_design_1”이 구매되어 있으면 미리 적용해보는 예시:
        if (SharedPreferencesActivity.isPurchased(requireContext(), "window_design_1")) {
            applyStyle("window_design_1");
        } else if (SharedPreferencesActivity.isPurchased(requireContext(), "window_design_2")) {
            applyStyle("window_design_2");
        } else if (SharedPreferencesActivity.isPurchased(requireContext(), "window_design_3")) {
            applyStyle("window_design_3");
        }
        // → 필요에 따라 우선순위를 바꾸거나, 마지막에 구매된 것만 보여주도록 “current_style” 값을 저장하는 방법 권장

        return view;
    }

    // 스타일 바 토글
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

    /**
     * (7) 버튼 클릭 시 포인트 차감, 구매 처리, 그리고 스타일 적용
     *
     * @param parent   fragment 뷰
     * @param buttonId XML에 정의된 Button 뷰 ID
     * @param cost     해당 아이템 가격(포인트)
     * @param itemKey  SharedPreferences에 저장할 고유한 키(String)
     */
    private void setupBuyButton(View parent, int buttonId, int cost, String itemKey) {
        Button button = parent.findViewById(buttonId);

        // 이미 해당 아이템을 구매했는지 체크
        boolean alreadyPurchased = SharedPreferencesActivity.isPurchased(requireContext(), itemKey);
        if (alreadyPurchased) {
            // 이미 산 상태라면 버튼 비활성화 처리
            button.setEnabled(false);
            button.setText("구매 완료");
            return;
        }

        button.setOnClickListener(v -> {
            if (currentCoins >= cost) {
                // --- 포인트 차감 ---
                currentCoins -= cost;
                SharedPreferencesActivity.saveCoins(requireContext(), currentCoins);

                // --- 구매 상태 저장 ---
                SharedPreferencesActivity.savePurchase(requireContext(), itemKey, true);

                // --- UI 업데이트(코인 텍스트 갱신 + 버튼 비활성화) ---
                updateCoinText();
                button.setEnabled(false);
                button.setText("구매 완료");

                // --- 스타일 적용 ---
                applyStyle(itemKey);

                // --- 포인트 차감 애니메이션 ---
                showPointChange(pointDeductedText, "-" + cost);

                Toast.makeText(getContext(), "스타일을 구매했어요!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "포인트가 부족해요!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // (8) 구매된 스타일 키(itemKey)에 따라 오버레이 ImageView에 해당 drawable을 세팅하고 보이게 함
    private void applyStyle(String itemKey) {
        int drawableRes;
        switch (itemKey) {
            case "window_design_1":
                drawableRes = R.drawable.window_design__1_;
                break;
            case "window_design_2":
                drawableRes = R.drawable.window_design__2;
                break;
            case "window_design_3":
                drawableRes = R.drawable.window_design__3;
                break;
            default:
                // 그 외 키가 들어오면 기본값 혹은 아무것도 표시 안 함
                overlayWindowImage.setVisibility(View.INVISIBLE);
                return;
        }

        // 실제로 오버레이 ImageView에 이미지 설정
        overlayWindowImage.setImageResource(drawableRes);
        overlayWindowImage.bringToFront(); // 필요하다면 다른 뷰 위로 올림
        overlayWindowImage.setVisibility(View.VISIBLE);
    }

    // 포인트 텍스트 갱신
    private void updateCoinText() {
        currentCoins +=100;
        coinText.setText("★ " + currentCoins);
    }

    // 포인트 증감 애니메이션 보여주기
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

    // 외부에서 포인트 추가하고 싶을 때 호출 가능
    public void addCoins(int amount) {
        currentCoins += amount;
        SharedPreferencesActivity.saveCoins(requireContext(), currentCoins);
        updateCoinText();
        showPointChange(pointAddedText, "+" + amount);
    }
}


