package com.example.calendar;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarMain extends AppCompatActivity {
    RecyclerView calendarRecyclerView;
    Calendar currentCalendar = Calendar.getInstance();
    List<CalendarDate> currentDates;
    int selectedPosition = -1;
    Button inputButton;
    TextView textSelectedDate;
    TextView textExpenseDetail;
    TextView textExpenseTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_source);

        calendarRecyclerView = findViewById(R.id.recyclerViewCalendar);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));

        Button btnPrev = findViewById(R.id.btnPrevMonth);
        Button btnNext = findViewById(R.id.btnNextMonth);
        TextView yearMonthText = findViewById(R.id.textYearMonth);
        inputButton = findViewById(R.id.btnInputTransaction);
        textSelectedDate = findViewById(R.id.textSelectedDate);
        textExpenseDetail = findViewById(R.id.textExpenseDetail);
        textExpenseTotal = findViewById(R.id.textExpenseTotal);

        inputButton.setEnabled(false);

        btnPrev.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            resetSelection();
            updateCalendarView();
        });

        btnNext.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            resetSelection();
            updateCalendarView();
        });

        inputButton.setOnClickListener(v -> {
            if (selectedPosition == -1) return;
            CalendarDate date = currentDates.get(selectedPosition);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            EditText inputExpense = new EditText(this);
            inputExpense.setHint("지출 (숫자 입력)");
            inputExpense.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(inputExpense);

            new AlertDialog.Builder(this)
                    .setTitle(date.day + "일 내역 입력")
                    .setView(layout)
                    .setPositiveButton("확인", (dialog, which) -> {
                        String input = inputExpense.getText().toString();
                        if (!input.isEmpty()) {
                            int amt = Integer.parseInt(input);
                            date.expenses.add(amt);  // 개별 지출 저장
                        }

                        calendarRecyclerView.getAdapter().notifyItemChanged(selectedPosition);
                        updateDetailList(date);
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });

        updateCalendarView();
    }

    private void resetSelection() {
        // 월이 바뀌면 이전 선택 취소
        selectedPosition = -1;
        inputButton.setEnabled(false);
        textSelectedDate.setText("선택된 날짜: 없음");
        textExpenseDetail.setText("지출 내역: 없음");
        textExpenseTotal.setText("총 지출: 없음");
    }

    private void updateCalendarView() {
        // 1) 년/월 텍스트 업데이트
        TextView yearMonthText = findViewById(R.id.textYearMonth);
        yearMonthText.setText(currentCalendar.get(Calendar.YEAR) + "년 " +
                (currentCalendar.get(Calendar.MONTH) + 1) + "월");

        // 2) 날짜 리스트 생성
        currentDates = getCalendarDates(currentCalendar);

        // 3) 어댑터 한 번만 생성
        CalendarAdapter adapter = new CalendarAdapter(currentDates, this, position -> {
            // 이전 선택 해제
            if (selectedPosition != -1) {
                currentDates.get(selectedPosition).isSelected = false;
                ((CalendarAdapter)calendarRecyclerView.getAdapter())
                        .notifyItemChanged(selectedPosition);
            }
            // 새 선택 설정
            currentDates.get(position).isSelected = true;
            ((CalendarAdapter)calendarRecyclerView.getAdapter())
                    .notifyItemChanged(position);
            selectedPosition = position;

            inputButton.setEnabled(true);
            textSelectedDate.setText("선택된 날짜: " + currentDates.get(position).day + "일");
            updateDetailList(currentDates.get(position));
        });

        // 4) RecyclerView에 어댑터 설정
        calendarRecyclerView.setAdapter(adapter);
    }

    private void updateDetailList(CalendarDate date) {
        if (date.expenses.isEmpty()) {
            textExpenseDetail.setText("지출 내역: 없음");
            textExpenseTotal.setText("총 지출: 없음");
        } else {
            StringBuilder sb = new StringBuilder();
            int sum = 0;
            sb.append("지출 내역:\n");
            for (int i = 0; i < date.expenses.size(); i++) {
                sum += date.expenses.get(i);
                sb.append((i+1) + ". ₩" + date.expenses.get(i) + "\n");
            }
            textExpenseDetail.setText(sb.toString());
            textExpenseTotal.setText("총 지출: ₩" + sum);
        }
    }


    private List<CalendarDate> getCalendarDates(Calendar calendar) {
        List<CalendarDate> dates = new ArrayList<>();

        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++)
            dates.add(new CalendarDate(0, false));

        for (int i = 1; i <= maxDay; i++) {
            dates.add(new CalendarDate(i, true));
        }

        while (dates.size() < 42)
            dates.add(new CalendarDate(0, false));

        return dates;
    }
}