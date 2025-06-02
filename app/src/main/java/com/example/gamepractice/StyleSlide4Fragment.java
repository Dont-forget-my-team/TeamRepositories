package com.example.gamepractice;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;

public class StyleSlide4Fragment extends Fragment {

    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_style_slide4, container, false);

        // XML에 선언된 PieChart 뷰를 찾아온다
        pieChart = view.findViewById(R.id.pieChart);

        // 차트 주변에 여백을 좀 더 주어 전체 크기를 키우는 효과를 줌
        pieChart.setExtraOffsets(10f, 10f, 10f, 10f);

        // 데이터 세팅 메서드 호출
        setupDonutChart();

        return view;
    }

    private void setupDonutChart() {
        // 1) PieEntry 리스트 생성 (3개 항목으로 변경)
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "A"));
        entries.add(new PieEntry(30f, "B"));
        entries.add(new PieEntry(30f, "C"));
        // (합계 = 100)

        // 2) PieDataSet 생성 및 스타일 지정
        PieDataSet dataSet = new PieDataSet(entries, "");
        // colors.xml에 정의한 색상 사용
        int colorPurple = ContextCompat.getColor(requireContext(), R.color.purple);
        int colorViolet = ContextCompat.getColor(requireContext(), R.color.violet);
        int colorSoftViolet = ContextCompat.getColor(requireContext(), R.color.softviolet);

        dataSet.setColors(new int[]{
                colorPurple,     // 보라
                colorViolet,     // 보랏빛
                colorSoftViolet, // 연보라
        });

        // 값을 정수(소수점 없이)로 표시하도록 Formatter 지정
        dataSet.setValueFormatter(new DefaultValueFormatter(0));
        dataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        dataSet.setValueTextSize(16f);  // 숫자를 좀 더 크게

        // 3) PieData로 감싸기
        PieData data = new PieData(dataSet);

        // 4) 차트 속성 및 데이터 연결
        pieChart.setData(data);
        pieChart.setUsePercentValues(false);

        // 도넛 형태: 중앙 구멍 크기를 작게 줄임 (기존 60f → 45f)
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(30f);              // 중앙 구멍 반경(%)
        pieChart.setTransparentCircleRadius(30f); // 투명 원 반경(%)
        pieChart.setTransparentCircleColor(
                ContextCompat.getColor(requireContext(), R.color.white));
        pieChart.setTransparentCircleAlpha(100);

        // 라벨(Entry 텍스트) 색상/크기 설정
        pieChart.setEntryLabelColor(
                ContextCompat.getColor(requireContext(), R.color.black));
        pieChart.setEntryLabelTextSize(14f); // 라벨도 약간 키움

        // 설명 텍스트 제거
        Description desc = new Description();
        desc.setText("");
        pieChart.setDescription(desc);

        // 범례 숨기기
        pieChart.getLegend().setEnabled(false);

        // 최종적으로 차트 갱신
        pieChart.invalidate();
    }
}
