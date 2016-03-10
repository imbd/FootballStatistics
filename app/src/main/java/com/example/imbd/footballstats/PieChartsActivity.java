package com.example.imbd.footballstats;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartsActivity extends Parent {

    String teamName;
    int champNumber;
    int teamNumber;
    boolean isTime = true;
    final static int[] colors = new int[]{Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.GRAY, Color.BLACK, Color.WHITE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_charts);
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(21);
        textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);

        champNumber = getIntent().getExtras().getInt("champNumber");
        teamNumber = getIntent().getExtras().getInt("teamNumber");
        teamName = getIntent().getExtras().getString("teamName");
        setTitle(teamName);

        makePie(isTime);


    }

    public void makePie(final boolean time) {

        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setText(R.string.diffKeepersTime);
        if (!time) {
            textView.setText(R.string.diffKeepersGoals);
        }
        int numValues = numberOfPlayers[champNumber][teamNumber];
        List<SliceValue> values = new ArrayList<>();
        final String[] names = new String[numValues];
        int num = -1;
        for (int i = 0; i < numValues; i++) {
            if (playerStats[champNumber][teamNumber][i][4] != 0) {
                num++;
                names[num] = playerNames[champNumber][teamNumber][i];
                int k = 1;
                if (!time) {
                    k = 4;
                }
                SliceValue sv = new SliceValue((float) playerStats[champNumber][teamNumber][i][k], colors[num]);
                values.add(sv);
            }

        }
        PieChartData data = new PieChartData(values);
        PieChartView chart = (PieChartView) findViewById(R.id.pie_chart);
        data.setHasLabels(true);
        data.setHasLabelsOutside(false);
        chart.setPieChartData(data);

        chart.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                String ending = "";
                if (time) {
                    ending = " минут";
                }
                String text = names[i] + " - " + (int) sliceValue.getValue() + ending;
                Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    public void changePie(View v) {

        isTime = !isTime;
        makePie(isTime);
    }
}
