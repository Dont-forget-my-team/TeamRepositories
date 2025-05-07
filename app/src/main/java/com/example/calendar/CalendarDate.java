package com.example.calendar;

public class CalendarDate {
    public int day;
    public boolean isCurrentMonth;
    public String expense;
    public String income;

    public CalendarDate(int day, boolean isCurrentMonth, String expense, String income) {
        this.day = day;
        this.isCurrentMonth = isCurrentMonth;
        this.expense = expense;
        this.income = income;
    }
}
