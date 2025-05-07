package com.example.calendar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    RecyclerView calendarRecyclerView;
    Calendar currentCalendar = Calendar.getInstance();
    List<CalendarDate> currentDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarRecyclerView = findViewById(R.id.recyclerViewCalendar);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));

        Button btnPrev = findViewById(R.id.btnPrevMonth);
        Button btnNext = findViewById(R.id.btnNextMonth);
        TextView yearMonthText = findViewById(R.id.textYearMonth);

        btnPrev.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateCalendarView();
        });

        btnNext.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateCalendarView();
        });

        updateCalendarView();
    }

    private void updateCalendarView() {
        TextView yearMonthText = findViewById(R.id.textYearMonth);
        yearMonthText.setText(currentCalendar.get(Calendar.YEAR) + "년 " +
                (currentCalendar.get(Calendar.MONTH) + 1) + "월");

        currentDates = getCalendarDates(currentCalendar);
        calendarRecyclerView.setAdapter(new CalendarAdapter(currentDates, this, (position, income,expense) -> {
            currentDates.get(position).income = income;
            currentDates.get(position).expense = expense;
            calendarRecyclerView.getAdapter().notifyItemChanged(position);
        }));
    }

    private List<CalendarDate> getCalendarDates(Calendar calendar) {
        List<CalendarDate> dates = new ArrayList<>();

        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++)
            dates.add(new CalendarDate(0, false, "", ""));

        for (int i = 1; i <= maxDay; i++) {
            dates.add(new CalendarDate(i, true, "", ""));
        }

        while (dates.size() < 42)
            dates.add(new CalendarDate(0, false, "", ""));

        return dates;
    }
}