package com.example.project_7_1

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.project_7_1.databinding.ActivityMainBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.pieChart.setUsePercentValues(true)


        val dataList = listOf(
            PieEntry(10F, "예시2"),
            PieEntry(30F, "건강"),
            PieEntry(25F, "교통"),
            PieEntry(35F, "여가"),
            PieEntry(10F, "예시1"),
            PieEntry(50F, "식비"),
            PieEntry(10F, "예시3")
        )

        // 색상 및 데이터셋 구성
        val dataSet = PieDataSet(dataList, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(this, R.color.pastel_rainbow1),
            ContextCompat.getColor(this, R.color.pastel_rainbow2),
            ContextCompat.getColor(this, R.color.pastel_rainbow3),
            ContextCompat.getColor(this, R.color.pastel_rainbow4),
            ContextCompat.getColor(this, R.color.pastel_rainbow5),
            ContextCompat.getColor(this, R.color.pastel_rainbow6),
            ContextCompat.getColor(this, R.color.pastel_rainbow7)
        )
        dataSet.valueTextSize = 16F
        dataSet.setDrawValues(false)

        val pieData = PieData(dataSet)


        binding.pieChart.apply {
            data = pieData
            description.isEnabled = false
            legend.isEnabled = false
            isRotationEnabled = true
            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
        }


        val sortedList = dataList.sortedByDescending { it.value }
        val selectedDataList = sortedList.take(3)

        binding.bestItem1.text = "1st: ${selectedDataList[0].label} -> ${selectedDataList[0].value}"
        binding.bestItem2.text = "2nd: ${selectedDataList[1].label} -> ${selectedDataList[1].value}"
        binding.bestItem3.text = "3rd: ${selectedDataList[2].label} -> ${selectedDataList[2].value}"
    }
}
