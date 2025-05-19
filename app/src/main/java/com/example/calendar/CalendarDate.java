package com.example.calendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarDate {
    public int day;
    public boolean isCurrentMonth;
    public boolean isSelected;
    public List<Integer> expenses;

    public CalendarDate(int day, boolean isCurrentMonth) {
        this.day = day;
        this.isCurrentMonth = isCurrentMonth;
        this.expenses = new ArrayList<>();
    }
    public String getTotalExpenseString() {
        int total = 0;
        for (int e : expenses) {
            total += e;
        }
        return total == 0 ? "" : "-₩" + total;
    }
}
