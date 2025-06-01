package com.example.gamepractice;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesActivity {
    private static final String PREF_NAME = "GamePrefs";
    private static final String KEY_COINS = "coins";

    public static void saveCoins(Context context, int coins) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_COINS, coins).apply();
    }

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
}
