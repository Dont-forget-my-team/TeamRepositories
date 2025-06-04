package com.example.gamepractice;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesActivity {
    private static final String PREF_NAME = "GamePrefs";
    private static final String KEY_COINS = "coins";
    private static final String KEY_SELECTED_WINDOW_STYLE = "selected_window_style";
    private static final String KEY_SELECTED_SOFA_STYLE = "selected_sofa_style";

    // ✅ [테스트용] 매번 초기화: 500포인트 + 구매 초기화
    public static void resetForTesting(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 초기 코인 지급
        editor.putInt(KEY_COINS, 500);

        // 창문 구매 초기화
        editor.putBoolean("window_design_1", false);
        editor.putBoolean("window_design_2", false);
        editor.putBoolean("window_design_3", false);

        // 소파 구매 초기화
        editor.putBoolean("sofa_design_1", false);
        editor.putBoolean("sofa_design_2", false);
        editor.putBoolean("sofa_design_3", false);

        // 선택된 스타일 초기화
        editor.remove(KEY_SELECTED_WINDOW_STYLE);
        editor.remove(KEY_SELECTED_SOFA_STYLE);

        editor.apply();
    }

    // 🔸 코인 저장
    public static void saveCoins(Context context, int coins) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_COINS, coins).apply();
    }

    // 🔸 코인 불러오기
    public static int loadCoins(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_COINS, 0);
    }

    // 🔸 구매 여부 저장
    public static void savePurchase(Context context, String itemKey, boolean purchased) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(itemKey, purchased).apply();
    }

    // 🔸 구매 여부 불러오기
    public static boolean isPurchased(Context context, String itemKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(itemKey, false);
    }

    // 🔸 현재 창문 스타일 저장
    public static void saveSelectedWindowStyle(Context context, String itemKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_SELECTED_WINDOW_STYLE, itemKey).apply();
    }

    // 🔸 현재 창문 스타일 불러오기
    public static String getSelectedWindowStyle(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_SELECTED_WINDOW_STYLE, null);
    }

    // ✅ 추가: 현재 창문 스타일 확인
    public static boolean isCurrentWindowStyle(Context context, String itemKey) {
        return itemKey.equals(getSelectedWindowStyle(context));
    }

    // 🔸 현재 소파 스타일 저장
    public static void saveSelectedSofaStyle(Context context, String itemKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_SELECTED_SOFA_STYLE, itemKey).apply();
    }

    // 🔸 현재 소파 스타일 불러오기
    public static String getSelectedSofaStyle(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_SELECTED_SOFA_STYLE, null);
    }

    // ✅ 추가: 현재 소파 스타일 확인
    public static boolean isCurrentSofaStyle(Context context, String itemKey) {
        return itemKey.equals(getSelectedSofaStyle(context));
    }
}


