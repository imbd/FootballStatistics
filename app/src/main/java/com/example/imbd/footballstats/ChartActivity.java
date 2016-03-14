package com.example.imbd.footballstats;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ChartActivity extends Parent {

    String champName;
    int champNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        champNumber = getIntent().getExtras().getInt("champNumber");
        champName = getIntent().getExtras().getString("champName");
        setTitle(champName);

        int numColumns = numberOfTeams[champNumber];
        int numSubcolumns = 1;
        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; j++) {
                int diff = teamStats[champNumber][i][5] - teamStats[champNumber][i][6];
                int color = Color.RED;
                if (diff >= 0) {
                    color = Color.GREEN;
                }
                values.add(new SubcolumnValue(diff, color));
            }

            axisValues.add(new AxisValue(i).setLabel(teamNames[champNumber][i]/*.substring(0, 3)*/));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(false));
            columns.get(i).setHasLabels(true);
        }

        ColumnChartData columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        ColumnChartView chartBottom = (ColumnChartView) findViewById(R.id.chart);
        chartBottom.setColumnChartData(columnData);

        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
                String text = String.valueOf(teamNames[champNumber][i]);
                Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
            }
        });

        chartBottom.setZoomType(ZoomType.HORIZONTAL);


    }
}
