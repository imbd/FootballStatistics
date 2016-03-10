package com.example.imbd.footballstats;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class GraphActivity extends Parent {

    String teamName;
    int champNumber;
    int teamNumber;
    int graphType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        champNumber = getIntent().getExtras().getInt("champNumber");
        teamNumber = getIntent().getExtras().getInt("teamNumber");
        graphType = getIntent().getExtras().getInt("graphType");
        teamName = getIntent().getExtras().getString("teamName");
        setTitle(teamName);

        ArrayList<PointValue> myData = new ArrayList<>();
        myData.add(new PointValue(0, 0));
        int num = 0;
        int weeksNumber = (numberOfTeams[champNumber] - 1) * 2;
        int matches = numberOfTeams[champNumber] / 2;
        for (int i = 0; i < weeksNumber; i++) {
            for (int j = 0; j < matches; j++) {
                MatchInfo match = results[champNumber][i][j];
                int points;
                if (match.goals1.equals("-")) {
                    continue;
                }
                int goals1 = Integer.valueOf(match.goals1), goals2 = Integer.valueOf(match.goals2);
                float was;

                was = myData.get(num).getY();
                if (match.name1.equals(teamName)) {
                    points = goals1 > goals2 ? 3 : goals1 == goals2 ? 1 : 0;
                    if (graphType == 1) {
                        points = goals1 - goals2;
                    }
                    myData.add(new PointValue(num + 1, was + points));
                    num++;
                }
                if (match.name2.equals(teamName)) {
                    points = goals1 < goals2 ? 3 : goals1 == goals2 ? 1 : 0;
                    if (graphType == 1) {
                        points = goals2 - goals1;
                    }
                    myData.add(new PointValue(num + 1, was + points));
                    num++;
                }
            }
        }


        Line line = new Line(myData);
        line.setColor(Color.RED);
        if (graphType == 1) {
            line.setColor(Color.BLUE);
        }
        line.setHasPoints(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData(lines);
        LineChartView chart = (LineChartView) findViewById(R.id.graph);
        Axis axisX = new Axis();
        Axis axisY = new Axis();
        axisX.setHasLines(true);
        axisY.setHasLines(true);
        axisX.setTextColor(Color.BLACK);
        axisY.setTextColor(Color.BLACK);
        axisX.setName("Туры");
        if (graphType == 0) {
            axisY.setName("Очки");
        } else {
            axisY.setName("Разница мячей");
        }
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        chart.setLineChartData(data);


        chart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {

                if (graphType == 1) {
                    String text = "Разница: " + (int) pointValue.getY() + ". Тур: " + (int) pointValue.getX();
                    Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
                    return;
                }

                String word1 = " очков";
                String word2 = " туров";
                int points = (int) pointValue.getY();
                if (points % 10 == 1 && points % 100 != 11) {
                    word1 = " очко";
                }
                if (points % 10 >= 2 && points % 10 <= 4 && ((points / 10) % 10 != 1)) {
                    word1 = " очка";
                }
                int weeks = (int) pointValue.getX();
                if (weeks % 10 == 1 && weeks % 100 != 11) {
                    word2 = " тура";
                }
                String text = points + word1 + " после " + weeks + word2;
                Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
            }
        });
    }

}
