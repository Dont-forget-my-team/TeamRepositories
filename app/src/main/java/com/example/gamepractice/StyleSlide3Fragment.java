package com.example.gamepractice;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StyleSlide3Fragment extends Fragment {
    private RecyclerView calendarRecyclerView;
    private Button btnPrevMonth, btnNextMonth, btnInputTransaction;
    private Button btnShowExpenses;
    private TextView textYearMonth;

    private Calendar currentCalendar = Calendar.getInstance();
    private List<CalendarDate> currentDates = new ArrayList<>();
    private int selectedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_style_slide3, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2) fragment_style_slide4.xml 안에 있는 ID들로 findViewById
        btnPrevMonth        = view.findViewById(R.id.btnPrevMonth);
        btnNextMonth        = view.findViewById(R.id.btnNextMonth);
        textYearMonth       = view.findViewById(R.id.textYearMonth);
        calendarRecyclerView= view.findViewById(R.id.recyclerViewCalendar);
        btnInputTransaction = view.findViewById(R.id.btnInputTransaction);
        btnShowExpenses     = view.findViewById(R.id.btnShowExpenses);

        // RecyclerView 레이아웃매니저
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 7));

        // 초기 버튼 비활성화
        btnInputTransaction.setEnabled(false);
        btnShowExpenses.setEnabled(false);

        // 이전/다음 월 버튼 리스너
        btnPrevMonth.setOnClickListener(v -> changeMonth(-1));
        btnNextMonth.setOnClickListener(v -> changeMonth(+1));

        // 지출 입력 버튼 리스너
        btnInputTransaction.setOnClickListener(v -> showExpenseDialog());
        btnShowExpenses.setOnClickListener(v -> showAllExpensesDialog());

        // 3) 캘린더뷰 최초 갱신
        updateCalendarView();
    }

    private void changeMonth(int offset) {
        currentCalendar.add(Calendar.MONTH, offset);
        resetSelection();
        updateCalendarView();
    }

    private void resetSelection() {
        selectedPosition = -1;
        btnInputTransaction.setEnabled(false);
        btnShowExpenses.setEnabled(false);
    }

    private void showExpenseDialog() {
        if (selectedPosition < 0) return;
        CalendarDate date = currentDates.get(selectedPosition);

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        //Spinner 생성: 카테고리 선택
        Spinner categorySpinner = new Spinner(requireContext());
        // string-array 리소스에서 어댑터 생성
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.expense_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        layout.addView(categorySpinner);
        // 금액 입력 EditText
        EditText inputExpense = new EditText(requireContext());
        inputExpense.setHint("지출 금액 (숫자만)");
        inputExpense.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(inputExpense);
        //지출 추가 다이얼로그
        new AlertDialog.Builder(requireContext())
                .setTitle(date.day + "일")
                .setView(layout)
                .setPositiveButton("추가", (dialog, which) -> {
                    String amountText = inputExpense.getText().toString().trim();
                    if (!amountText.isEmpty()) {
                        int amt = Integer.parseInt(amountText);
                        // 스피너에서 선택된 카테고리
                        String category = (String) categorySpinner.getSelectedItem();
                        // "카테고리:금액" 형식으로 저장
                        date.expensesWithCategory.add(category + ":" + amt);

                        // 달력 셀의 합계 텍스트 갱신
                        calendarRecyclerView.getAdapter().notifyItemChanged(selectedPosition);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void showAllExpensesDialog() {
        // 전체 달력의 모든 날짜에서 “expensesWithCategory”를 모아서
        // 날짜별로 구분하여 보여주도록 할 수 있지만, 여기서는 예시로
        // “선택된 날짜 하나”의 내역만 보여줍니다.
        if (selectedPosition < 0) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("지출내역")
                    .setMessage("선택된 날짜가 없습니다.")
                    .setPositiveButton("확인", null)
                    .show();
            return;
        }

        CalendarDate date = currentDates.get(selectedPosition);
        if (date.expensesWithCategory.isEmpty()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle(date.day + "일 지출내역")
                    .setMessage("지출 내역이 없습니다.")
                    .setPositiveButton("확인", null)
                    .show();
            return;
        }

        // 지출 내역 문자열 생성
        StringBuilder sb = new StringBuilder();
        int sum = 0;
        for (int i = 0; i < date.expensesWithCategory.size(); i++) {
            String entry = date.expensesWithCategory.get(i); // "식비:5000"
            String[] parts = entry.split(":");
            String category = parts[0];
            int amt = 0;
            if (parts.length == 2) {
                try {
                    amt = Integer.parseInt(parts[1]);
                } catch (NumberFormatException ignored) {
                }
            }
            sum += amt;
            sb.append(i + 1)
                    .append(". [")
                    .append(category)
                    .append("]")
                    .append(amt)
                    .append("\n");
        }
        sb.append("\n총 지출: ").append(sum);

        // 다이얼로그로 표시
        new AlertDialog.Builder(requireContext())
                .setTitle(date.day + "일 지출내역")
                .setMessage(sb.toString())
                .setPositiveButton("확인", null)
                .show();
    }
    private void updateCalendarView() {
        // 년/월 표시
        textYearMonth.setText(
                currentCalendar.get(Calendar.YEAR) + "년 " +
                        (currentCalendar.get(Calendar.MONTH) + 1) + "월"
        );

        // 날짜 리스트 생성
        currentDates = generateDates(currentCalendar);

        // CalendarAdapter 초기화
        CalendarAdapter adapter = new CalendarAdapter(
                currentDates,
                requireContext(),
                position -> {
                    // 이전 선택 해제
                    if (selectedPosition >= 0) {
                        currentDates.get(selectedPosition).isSelected = false;
                        calendarRecyclerView.getAdapter().notifyItemChanged(selectedPosition);
                    }
                    // 새 선택
                    currentDates.get(position).isSelected = true;
                    calendarRecyclerView.getAdapter().notifyItemChanged(position);
                    selectedPosition = position;

                    btnInputTransaction.setEnabled(true);
                    btnShowExpenses.setEnabled(true);
                }
        );
        calendarRecyclerView.setAdapter(adapter);
    }



    private List<CalendarDate> generateDates(Calendar base) {
        List<CalendarDate> dates = new ArrayList<>();
        Calendar cal = (Calendar) base.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDow = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDow; i++)
            dates.add(new CalendarDate(0, false));
        for (int d = 1; d <= maxDay; d++)
            dates.add(new CalendarDate(d, true));
        while (dates.size() < 42)
            dates.add(new CalendarDate(0, false));
        return dates;
    }
}

