package com.example.teamproject_main;

import android.os.Bundle;
import android.widget.Button;  // ← import 추가
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.example.project_7_1.R;

import java.util.ArrayList;
import java.util.List;

public class NextActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        // 1) 차트 초기화 (이전 코드 복사)
        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.getDescription().setEnabled(false);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(50f, "식비"));
        entries.add(new PieEntry(35f, "여가"));
        entries.add(new PieEntry(30f, "건강"));
        entries.add(new PieEntry(20f, "교통"));
        entries.add(new PieEntry(5f,  "예시3"));

        PieDataSet dataSet = new PieDataSet(entries, "지출 분포");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();

        // 2) 끝내기 버튼 클릭 시 이전 화면으로 돌아가기
        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(v -> finish());
    }
}
