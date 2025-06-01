package com.example.gamepractice;

import java.util.ArrayList;
import java.util.List;

public class CalendarDate {
    public int day;
    public boolean isCurrentMonth;
    public boolean isSelected;
    public List<String> expensesWithCategory;

    public CalendarDate(int day, boolean isCurrentMonth) {
        this.day = day;
        this.isCurrentMonth = isCurrentMonth;
        this.expensesWithCategory = new ArrayList<>();
    }
    public String getTotalExpenseString() {
        int total = 0;
        for (String s : expensesWithCategory) {
            // s 예시: "식비:5000" → 금액만 파싱
            String[] parts = s.split(":");
            if (parts.length == 2) {
                try {
                    total += Integer.parseInt(parts[1]);
                } catch (NumberFormatException ignored) {}
            }
        }
        return total == 0 ? "" : "-" + total;
    }
}
