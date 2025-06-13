package com.example.gamepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView rank1, rank2, rank3;

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

        pieChart.setExtraOffsets(10f, 10f, 10f, 10f);

        setupDonutChartAndRanking();

        return view;
    }

    /** expenses.txt에서 데이터 읽어 차트 + 상위3 랭킹 표시 */
    private void setupDonutChartAndRanking() {
        Map<String, Float> categorySums = new LinkedHashMap<>();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;

        File file = new File(requireContext().getFilesDir(), "expenses.txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length != 5) continue;
                    int y = Integer.parseInt(parts[0].trim());
                    int m = Integer.parseInt(parts[1].trim());
                    int d = Integer.parseInt(parts[2].trim());
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

        // PieDataSet 및 색상 (colors.xml에서 필요한 만큼 색상 추가)
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

        // ----------- 랭킹 텍스트뷰 동적 표시 (Comparator 명시적 사용) -------------
        List<Map.Entry<String, Float>> sorted = new ArrayList<>(categorySums.entrySet());
        sorted.sort(new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> a, Map.Entry<String, Float> b) {
                // 내림차순 (많은 금액이 먼저)
                return Float.compare(b.getValue(), a.getValue());
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
