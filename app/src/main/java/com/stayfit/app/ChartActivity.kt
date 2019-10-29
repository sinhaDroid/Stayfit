package com.stayfit.app

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

import java.util.ArrayList

/**
 * Created by maddi on 3/1/2016.
 */
class ChartActivity : AppCompatActivity() {

    private// Jan
    // Feb
    // Mar
    // Apr
    // May
    // Jun
    // Jan
    // Feb
    // Mar
    // Apr
    // May
    // Jun
    val dataSet: ArrayList<BarDataSet>
        get() {
            var dataSets: ArrayList<BarDataSet>? = null

            val valueSet1 = ArrayList<BarEntry>()
            val v1e1 = BarEntry(110.000f, 0)
            valueSet1.add(v1e1)
            val v1e2 = BarEntry(40.000f, 1)
            valueSet1.add(v1e2)
            val v1e3 = BarEntry(60.000f, 2)
            valueSet1.add(v1e3)
            val v1e4 = BarEntry(30.000f, 3)
            valueSet1.add(v1e4)
            val v1e5 = BarEntry(90.000f, 4)
            valueSet1.add(v1e5)
            val v1e6 = BarEntry(100.000f, 5)
            valueSet1.add(v1e6)

            val valueSet2 = ArrayList<BarEntry>()
            val v2e1 = BarEntry(150.000f, 0)
            valueSet2.add(v2e1)
            val v2e2 = BarEntry(90.000f, 1)
            valueSet2.add(v2e2)
            val v2e3 = BarEntry(120.000f, 2)
            valueSet2.add(v2e3)
            val v2e4 = BarEntry(60.000f, 3)
            valueSet2.add(v2e4)
            val v2e5 = BarEntry(20.000f, 4)
            valueSet2.add(v2e5)
            val v2e6 = BarEntry(80.000f, 5)
            valueSet2.add(v2e6)

            val barDataSet1 = BarDataSet(valueSet1, "Brand 1")
            barDataSet1.color = Color.rgb(0, 155, 0)
            val barDataSet2 = BarDataSet(valueSet2, "Brand 2")
            barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS)

            dataSets = ArrayList()
            dataSets.add(barDataSet1)
            dataSets.add(barDataSet2)
            return dataSets
        }

    private val xAxisValues: ArrayList<String>
        get() {
            val xAxis = ArrayList<String>()
            xAxis.add("JAN")
            xAxis.add("FEB")
            xAxis.add("MAR")
            xAxis.add("APR")
            xAxis.add("MAY")
            xAxis.add("JUN")
            return xAxis
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        val chart = findViewById<View>(R.id.chart) as BarChart

        val data = BarData()
        chart.data = data
        chart.setDescription("My Chart")
        chart.animateXY(2000, 2000)
        chart.invalidate()
    }
}