package com.example.gamepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StyleSlide4Fragment extends Fragment {

    private PieChart pieChart;
    private TextView rank1, rank2, rank3, overlayText;
    private Spinner monthSpinner;

    private int selectedYear;
    private int selectedMonth; // 1~12

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_style_slide4, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        rank1 = view.findViewById(R.id.rank1);
        rank2 = view.findViewById(R.id.rank2);
        rank3 = view.findViewById(R.id.rank3);
        overlayText = view.findViewById(R.id.overlayText);
        monthSpinner = view.findViewById(R.id.monthSpinner);

        pieChart.setExtraOffsets(10f, 10f, 10f, 10f);

        Calendar now = Calendar.getInstance();
        selectedYear = now.get(Calendar.YEAR);
        selectedMonth = now.get(Calendar.MONTH) + 1;

        // 1~12월 Spinner 세팅
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(i + "월");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setSelection(selectedMonth - 1);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                selectedMonth = position + 1;
                overlayText.setText(selectedMonth + "월 소비 비율");
                setupDonutChartAndRanking(selectedYear, selectedMonth);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 초기화 (앱 진입 시 현재월)
        overlayText.setText(selectedMonth + "월 소비 비율");
        setupDonutChartAndRanking(selectedYear, selectedMonth);

        return view;
    }

    private void setupDonutChartAndRanking(int year, int month) {
        Map<String, Float> categorySums = new LinkedHashMap<>();
        File file = new File(requireContext().getFilesDir(), "expenses.txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length != 5) continue;
                    int y = Integer.parseInt(parts[0].trim());
                    int m = Integer.parseInt(parts[1].trim());
                    String category = parts[3].trim();
                    float amount = Float.parseFloat(parts[4].trim());
                    if (y == year && m == month) {
                        float prev = categorySums.getOrDefault(category, 0f);
                        categorySums.put(category, prev + amount);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // PieEntry 리스트 생성 (카테고리 수만큼)
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> e : categorySums.entrySet()) {
            entries.add(new PieEntry(e.getValue(), e.getKey()));
        }
        if (entries.isEmpty()) {
            entries.add(new PieEntry(1f, "지출 없음"));
        }

        int[] colors = {
                ContextCompat.getColor(requireContext(), R.color.purple),
                ContextCompat.getColor(requireContext(), R.color.violet),
                ContextCompat.getColor(requireContext(), R.color.softviolet),
                ContextCompat.getColor(requireContext(), R.color.blue),
                ContextCompat.getColor(requireContext(), R.color.yellow),
        };
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            colorList.add(colors[i % colors.length]);
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorList);

        dataSet.setValueFormatter(new DefaultValueFormatter(0));
        dataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        dataSet.setValueTextSize(16f);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setTransparentCircleColor(ContextCompat.getColor(requireContext(), R.color.white));
        pieChart.setTransparentCircleAlpha(100);

        pieChart.setEntryLabelColor(ContextCompat.getColor(requireContext(), R.color.black));
        pieChart.setEntryLabelTextSize(14f);

        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);

        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate();

        // ----------- 랭킹 표시 -------------
        List<Map.Entry<String, Float>> sorted = new ArrayList<>(categorySums.entrySet());
        sorted.sort(new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> a, Map.Entry<String, Float> b) {
                return Float.compare(b.getValue(), a.getValue()); // 내림차순
            }
        });

        if (sorted.size() > 0)
            rank1.setText("1. " + sorted.get(0).getKey() + ": " + (int)(float)sorted.get(0).getValue());
        else
            rank1.setText("1. -");

        if (sorted.size() > 1)
            rank2.setText("2. " + sorted.get(1).getKey() + ": " + (int)(float)sorted.get(1).getValue());
        else
            rank2.setText("2. -");

        if (sorted.size() > 2)
            rank3.setText("3. " + sorted.get(2).getKey() + ": " + (int)(float)sorted.get(2).getValue());
        else
            rank3.setText("3. -");
    }
}
