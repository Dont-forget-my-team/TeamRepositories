package com.example.gamepractice;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesActivity {
    private static final String PREF_NAME = "GamePrefs";
    private static final String KEY_COINS = "coins";

    private static final String KEY_SELECTED_WINDOW_STYLE = "selected_window_style";
    private static final String KEY_SELECTED_SOFA_STYLE = "selected_sofa_style";
    private static final String KEY_SELECTED_WALLPAPER_STYLE = "selected_wallpaper_style";
    private static final String KEY_SELECTED_CARPET_STYLE = "selected_carpet_style";
    private static final String KEY_SELECTED_WALLHANGER_STYLE = "selected_wallhanger_style";

    // ✅ 테스트 초기화
    public static void resetForTesting(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(KEY_COINS, 500);

        // 각 아이템 구매 초기화
        editor.putBoolean("window_design_1", false);
        editor.putBoolean("window_design_2", false);
        editor.putBoolean("window_design_3", false);

        editor.putBoolean("sofa_design_1", false);
        editor.putBoolean("sofa_design_2", false);
        editor.putBoolean("sofa_design_3", false);

        editor.putBoolean("carpet_design_1", false);
        editor.putBoolean("carpet_design_2", false);
        editor.putBoolean("carpet_design_3", false);

        editor.putBoolean("wallhanger_design_1", false);
        editor.putBoolean("wallhanger_design_2", false);
        editor.putBoolean("wallhanger_design_3", false);

        editor.putBoolean("wallpaper_design_1", false);
        editor.putBoolean("wallpaper_design_2", false);
        editor.putBoolean("wallpaper_design_3", false);

        // 선택 초기화
        editor.remove(KEY_SELECTED_WINDOW_STYLE);
        editor.remove(KEY_SELECTED_SOFA_STYLE);
        editor.remove(KEY_SELECTED_CARPET_STYLE);
        editor.remove(KEY_SELECTED_WALLHANGER_STYLE);
        editor.remove(KEY_SELECTED_WALLPAPER_STYLE);

        editor.apply();
    }

    // 코인 저장/불러오기
    public static void saveCoins(Context context, int coins) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_COINS, coins).apply();
    }

    public static int loadCoins(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_COINS, 0);
    }

    // 구매 저장/불러오기
    public static void savePurchase(Context context, String itemKey, boolean purchased) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(itemKey, purchased).apply();
    }

    public static boolean isPurchased(Context context, String itemKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(itemKey, false);
    }

    // ✅ 창문
    public static void saveSelectedWindowStyle(Context context, String itemKey) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_SELECTED_WINDOW_STYLE, itemKey).apply();
    }

    public static String getSelectedWindowStyle(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_SELECTED_WINDOW_STYLE, null);
    }

    public static boolean isCurrentWindowStyle(Context context, String itemKey) {
        return itemKey.equals(getSelectedWindowStyle(context));
    }

    // ✅ 소파
    public static void saveSelectedSofaStyle(Context context, String itemKey) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_SELECTED_SOFA_STYLE, itemKey).apply();
    }

    public static String getSelectedSofaStyle(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_SELECTED_SOFA_STYLE, null);
    }

    public static boolean isCurrentSofaStyle(Context context, String itemKey) {
        return itemKey.equals(getSelectedSofaStyle(context));
    }

    // ✅ 카펫
    public static void saveSelectedCarpetStyle(Context context, String itemKey) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_SELECTED_CARPET_STYLE, itemKey).apply();
    }

    public static String getSelectedCarpetStyle(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_SELECTED_CARPET_STYLE, null);
    }

    public static boolean isCurrentCarpetStyle(Context context, String itemKey) {
        return itemKey.equals(getSelectedCarpetStyle(context));
    }

    // ✅ 벽걸이
    public static void saveSelectedWallhangerStyle(Context context, String itemKey) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_SELECTED_WALLHANGER_STYLE, itemKey).apply();
    }

    public static String getSelectedWallhangerStyle(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_SELECTED_WALLHANGER_STYLE, null);
    }

    public static boolean isCurrentWallhangerStyle(Context context, String itemKey) {
        return itemKey.equals(getSelectedWallhangerStyle(context));
    }

    // ✅ 벽지
    public static void saveSelectedWallpaperStyle(Context context, String itemKey) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putString(KEY_SELECTED_WALLPAPER_STYLE, itemKey).apply();
    }

    public static String getSelectedWallpaperStyle(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(KEY_SELECTED_WALLPAPER_STYLE, null);
    }

    public static boolean isCurrentWallpaperStyle(Context context, String itemKey) {
        return itemKey.equals(getSelectedWallpaperStyle(context));
    }
}



