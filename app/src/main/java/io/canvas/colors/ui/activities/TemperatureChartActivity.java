package io.canvas.colors.ui.activities;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.canvas.colors.R;
import io.canvas.colors.databinding.ActivityTemperatureChartBinding;

public class TemperatureChartActivity extends AppCompatActivity {

    ActivityTemperatureChartBinding binding;

    List<Entry> entries = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_temperature_chart);

        Entry c1e1 = new Entry(0f, 10f); // 0 == quarter 1
        entries.add(c1e1);
        Entry c1e2 = new Entry(1f, 140f); // 1 == quarter 2 ...
        entries.add(c1e2);
        Entry c1e3 = new Entry(2f, 80f); // 1 == quarter 2 ...
        entries.add(c1e3);
        Entry c1e4 = new Entry(3f, 120f); // 1 == quarter 2 ...
        entries.add(c1e4);
        Entry c1e5 = new Entry(4f, 100f);
        entries.add(c1e5);
        Entry c1e6 = new Entry(5f, 110f);
        entries.add(c1e6);

        LineDataSet dataSet = new LineDataSet(entries, "Humidity"); // add entries to dataset
        dataSet.setValueTextColor(R.color.fontColor);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(3f);
        dataSet.disableDashedLine();
        dataSet.setDrawFilled(true);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setFillDrawable(getResources().getDrawable(R.drawable.humidity_gradient));
        dataSet.disableDashedHighlightLine();

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        binding.lineChart.setScaleYEnabled(false);
        binding.lineChart.setScaleXEnabled(true);
        binding.lineChart.setData(lineData);
        binding.lineChart.setBorderColor(getResources().getColor(R.color.online));
        binding.lineChart.invalidate(); // refresh

    }
}
