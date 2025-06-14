package com.example.gamepractice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StyleSlide3Fragment extends Fragment {
    private RecyclerView calendarRecyclerView;
    private Button btnPrevMonth, btnNextMonth, btnInputTransaction;
    private Button btnShowExpenses;
    private Button btnEditExpense;
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
        btnEditExpense = view.findViewById(R.id.btnEditExpense);
        btnShowExpenses     = view.findViewById(R.id.btnShowExpenses);

        // RecyclerView 레이아웃매니저
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 7));

        // 초기 버튼 비활성화
        btnInputTransaction.setEnabled(false);
        btnEditExpense.setEnabled(false);
        btnShowExpenses.setEnabled(false);

        // 이전/다음 월 버튼 리스너
        btnPrevMonth.setOnClickListener(v -> changeMonth(-1));
        btnNextMonth.setOnClickListener(v -> changeMonth(+1));

        // 지출 입력 버튼 리스너
        btnInputTransaction.setOnClickListener(v -> showExpenseDialog());
        btnEditExpense.setOnClickListener(v -> showEditExpenseDialog());
        btnShowExpenses.setOnClickListener(v -> showAllExpensesDialog());

        // 3) 캘린더뷰 최초 갱신
        updateCalendarView();
        // 1일이면 자동 점수 정산
        checkAndSaveWeeklyScore();
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
    //매달 1일 점수 정산
    private void checkAndSaveWeeklyScore() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != Calendar.MONDAY) return; // 월요일만 정산

        // 지난주 기간
        Calendar lastMonday = (Calendar) today.clone();
        lastMonday.add(Calendar.DATE, -7);
        Calendar lastSunday = (Calendar) today.clone();
        lastSunday.add(Calendar.DATE, -1);

        // 지난주 주차
        int year = lastMonday.get(Calendar.YEAR);
        int week = lastMonday.get(Calendar.WEEK_OF_YEAR);

        // 목표금액
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int targetAmount = Integer.parseInt(prefs.getString("target_amount", "0"));

        // 지난주 지출 합계
        int sum = 0;
        List<ExpenseRecord> allRecords = loadExpensesFromFile();
        for (ExpenseRecord rec : allRecords) {
            Calendar c = Calendar.getInstance();
            c.set(rec.year, rec.month - 1, rec.day);
            if (!c.before(lastMonday) && !c.after(lastSunday)) {
                sum += rec.amount;
            }
        }

        // 점수 계산
        int score = 0;
        if (sum <= targetAmount && targetAmount > 0) {
            score = (int)(((targetAmount - sum) * 1000.0) / targetAmount);
        }

        // 파일에 기록
        String record = year + "," + week + "," + score + "\n";
        try (FileOutputStream fos = requireContext().openFileOutput("score.txt", Context.MODE_APPEND)) {
            fos.write(record.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //지출추가
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
                        saveExpenseToFile(
                                currentCalendar.get(Calendar.YEAR),
                                currentCalendar.get(Calendar.MONTH) + 1,
                                date.day,
                                category,
                                amt
                        );
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }
    //지출 수정하기
    private void showEditExpenseDialog() {
        if (selectedPosition < 0) return;
        CalendarDate date = currentDates.get(selectedPosition);
        List<String> list = date.expensesWithCategory;

        if (list.isEmpty()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle(date.day + "일 지출내역 수정")
                    .setMessage("수정할 내역이 없습니다.")
                    .setPositiveButton("확인", null)
                    .show();
            return;
        }

        String[] items = list.toArray(new String[0]);
        new AlertDialog.Builder(requireContext())
                .setTitle(date.day + "일 지출내역 선택")
                .setItems(items, (dialog, which) -> {
                    // which: 선택한 항목 인덱스
                    String selected = list.get(which);
                    String[] parts = selected.split(":");
                    String oldCategory = parts[0];
                    String oldAmount = (parts.length > 1) ? parts[1] : "";

                    // 수정/삭제 다이얼로그
                    LinearLayout layout = new LinearLayout(requireContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50, 20, 50, 20);

                    // 카테고리 스피너
                    Spinner spinner = new Spinner(requireContext());
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.expense_categories,
                            android.R.layout.simple_spinner_item
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    // 기존 카테고리 설정
                    int catIdx = adapter.getPosition(oldCategory);
                    if (catIdx >= 0) spinner.setSelection(catIdx);

                    layout.addView(spinner);

                    // 금액 EditText
                    EditText amountEdit = new EditText(requireContext());
                    amountEdit.setHint("지출 금액 (숫자만)");
                    amountEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    amountEdit.setText(oldAmount);
                    layout.addView(amountEdit);

                    new AlertDialog.Builder(requireContext())
                            .setTitle("수정/삭제")
                            .setView(layout)
                            .setPositiveButton("수정", (d, w) -> {
                                String newCategory = (String) spinner.getSelectedItem();
                                String newAmountStr = amountEdit.getText().toString().trim();
                                if (!newAmountStr.isEmpty()) {
                                    int newAmount = Integer.parseInt(newAmountStr);

                                    // 리스트에서 값 수정
                                    list.set(which, newCategory + ":" + newAmount);
                                    // 파일 내용 갱신
                                    updateExpenseFile();
                                    calendarRecyclerView.getAdapter().notifyItemChanged(selectedPosition);
                                }
                            })
                            .setNegativeButton("취소", null)
                            .setNeutralButton("삭제", (d, w) -> {
                                // 리스트에서 삭제
                                list.remove(which);
                                updateExpenseFile();
                                calendarRecyclerView.getAdapter().notifyItemChanged(selectedPosition);
                            })
                            .show();
                })
                .setNegativeButton("닫기", null)
                .show();
    }
    //지출내역 보기
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
    //업데이트 캘린더
    private void updateCalendarView() {
        // 년/월 표시
        textYearMonth.setText(
                currentCalendar.get(Calendar.YEAR) + "년 " +
                        (currentCalendar.get(Calendar.MONTH) + 1) + "월"
        );

        // 날짜 리스트 생성
        currentDates = generateDates(currentCalendar);
        // 파일에서 전체 내역 불러오기
        List<ExpenseRecord> allRecords = loadExpensesFromFile();
        // 현재 달의 내역만 달력 데이터에 추가
        int y = currentCalendar.get(Calendar.YEAR);
        int m = currentCalendar.get(Calendar.MONTH) + 1;
        for (CalendarDate d : currentDates) {
            d.expensesWithCategory.clear(); // 매번 리셋
            if (d.isCurrentMonth) {
                for (ExpenseRecord rec : allRecords) {
                    if (rec.year == y && rec.month == m && rec.day == d.day) {
                        d.expensesWithCategory.add(rec.category + ":" + rec.amount);
                    }
                }
            }
        }

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
                    btnEditExpense.setEnabled(true);
                    btnShowExpenses.setEnabled(true);
                }
        );
        calendarRecyclerView.setAdapter(adapter);
    }
    //데이터를 파일에 저장
    private void saveExpenseToFile(int year, int month, int day, String category, int amount) {
        String record = year + "," + month + "," + day + "," + category + "," + amount + "\n";
        try (FileOutputStream fos = requireContext().openFileOutput("expenses.txt", Context.MODE_APPEND)) {
            fos.write(record.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //파일 업데이트(수정)
    private void updateExpenseFile() {
        // 1. 파일에 저장된 기존 모든 내역 불러오기
        List<ExpenseRecord> allRecords = loadExpensesFromFile();

        // 2. 현재 달(currentDates)의 변경사항을 allRecords에 반영
        int y = currentCalendar.get(Calendar.YEAR);
        int m = currentCalendar.get(Calendar.MONTH) + 1;

        // (1) 현재 달 기록 중복 삭제
        allRecords.removeIf(rec -> rec.year == y && rec.month == m);

        // (2) 현재 달의 최신 데이터 추가
        for (CalendarDate date : currentDates) {
            if (!date.isCurrentMonth) continue;
            for (String entry : date.expensesWithCategory) {
                String[] parts = entry.split(":");
                if (parts.length != 2) continue;
                String category = parts[0];
                int amount = 0;
                try {
                    amount = Integer.parseInt(parts[1]);
                } catch (NumberFormatException ignore) {}
                allRecords.add(new ExpenseRecord(y, m, date.day, category, amount));
            }
        }

        // 3. 파일을 전체 새로 씀
        StringBuilder sb = new StringBuilder();
        for (ExpenseRecord rec : allRecords) {
            sb.append(rec.year).append(",")
                    .append(rec.month).append(",")
                    .append(rec.day).append(",")
                    .append(rec.category).append(",")
                    .append(rec.amount).append("\n");
        }
        try (FileOutputStream fos = requireContext().openFileOutput("expenses.txt", Context.MODE_PRIVATE)) {
            fos.write(sb.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //파일 불러오기(앱 실행시)
    private List<ExpenseRecord> loadExpensesFromFile() {
        List<ExpenseRecord> result = new ArrayList<>();
        File file = new File(requireContext().getFilesDir(), "expenses.txt");
        if (!file.exists()) return result;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5) continue;
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                String category = parts[3];
                int amount = Integer.parseInt(parts[4]);
                result.add(new ExpenseRecord(year, month, day, category, amount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class ExpenseRecord {
        public int year, month, day, amount;
        public String category;
        public ExpenseRecord(int year, int month, int day, String category, int amount) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.category = category;
            this.amount = amount;
        }
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

