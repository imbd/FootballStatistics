package com.example.imbd.footballstats;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ScheduleActivity extends Parent {

    int champNumber;
    int teamNumber;
    String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        champNumber = getIntent().getExtras().getInt("champNumber");
        teamNumber = getIntent().getExtras().getInt("teamNumber");
        teamName = getIntent().getExtras().getString("teamName");
        setTitle(teamName);
        TableLayout tableLayout = new TableLayout(this);
        TableRow titles = new TableRow(this);
        TextView week = new TextView(this);
        TextView team = new TextView(this);
        TextView score = new TextView(this);
        TextView date = new TextView(this);
        week.setText("Тур");
        team.setText("Соперник");
        score.setText("Счет");
        date.setText("Дата");
        week.setGravity(Gravity.CENTER_HORIZONTAL);
        team.setGravity(Gravity.CENTER_HORIZONTAL);
        score.setGravity(Gravity.CENTER_HORIZONTAL);
        date.setGravity(Gravity.CENTER_HORIZONTAL);
        titles.addView(week);
        titles.addView(team);
        titles.addView(score);
        titles.addView(date);
        tableLayout.addView(titles);
        int weeksNumber = (numberOfTeams[champNumber] - 1) * 2;
        int matches = numberOfTeams[champNumber] / 2;
        TextView[] wk = new TextView[weeksNumber];
        TextView[] sc = new TextView[weeksNumber];
        TextView[] op = new TextView[weeksNumber];
        TextView[] dt = new TextView[weeksNumber];
        TableRow[] tableRows = new TableRow[weeksNumber];
        for (int i = 0; i < weeksNumber; i++) {
            wk[i] = new TextView(this);
            sc[i] = new TextView(this);
            op[i] = new TextView(this);
            dt[i] = new TextView(this);
            wk[i].setText(String.valueOf(i + 1));
            wk[i].setGravity(Gravity.CENTER_HORIZONTAL);
            op[i].setGravity(Gravity.CENTER_HORIZONTAL);
            sc[i].setGravity(Gravity.CENTER_HORIZONTAL);
            dt[i].setGravity(Gravity.CENTER_HORIZONTAL);
            op[i].setTextSize(16);
            op[i].setTypeface(null, Typeface.BOLD);
            op[i].setTextColor(Color.BLACK);
            tableRows[i] = new TableRow(this);
            for (int j = 0; j < matches; j++) {
                Parent.MatchInfo match = results[champNumber][i][j];
                if (match.name1.equals(teamName)) {
                    op[i].setText(match.name2 + "(дома)");
                    sc[i].setText(match.goals1 + "-" + match.goals2);
                    dt[i].setText(match.date);

                    if (match.goals1.equals("-")) {
                        continue;
                    }
                    sc[i].setTextColor(Color.YELLOW);

                    if (Integer.valueOf(match.goals1) > Integer.valueOf(match.goals2)) {
                        sc[i].setTextColor(Color.GREEN);
                    }
                    if (Integer.valueOf(match.goals1) < Integer.valueOf(match.goals2)) {
                        sc[i].setTextColor(Color.RED);
                    }

                }
                if (match.name2.equals(teamName)) {
                    op[i].setText(match.name1 + "(в гостях)");
                    sc[i].setText(match.goals1 + "-" + match.goals2);
                    dt[i].setText(match.date);

                    if (match.goals1.equals("-")) {
                        continue;
                    }
                    sc[i].setTextColor(Color.YELLOW);

                    if (Integer.valueOf(match.goals1) > Integer.valueOf(match.goals2)) {
                        sc[i].setTextColor(Color.RED);
                    }
                    if (Integer.valueOf(match.goals1) < Integer.valueOf(match.goals2)) {
                        sc[i].setTextColor(Color.GREEN);
                    }
                }

            }
            tableRows[i].addView(wk[i]);
            tableRows[i].addView(op[i]);
            tableRows[i].addView(sc[i]);
            tableRows[i].addView(dt[i]);
            tableLayout.addView(tableRows[i]);
        }
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        ScrollView sv = new ScrollView(this);
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        sv.addView(tableLayout);
        hsv.addView(sv);
        setContentView(hsv);
    }
}
