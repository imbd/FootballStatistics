package com.example.imbd.footballstats;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

public class ScrollingActivity extends Parent {

    int champNumber;
    String champ;
    private static int COLUMNS_NUMBER = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        champNumber = getIntent().getExtras().getInt("champNumber");
        champ = getIntent().getExtras().getString("champName");
        setTitle(champ);

        final TeamInfo[] sortTeams = new TeamInfo[numberOfTeams[champNumber]];
        for (int i = 0; i < numberOfTeams[champNumber]; i++) {
            TeamInfo newTeam = new TeamInfo(teamStats[champNumber][i][7], i);
            sortTeams[i] = newTeam;
        }
        Arrays.sort(sortTeams, new Comparator<TeamInfo>() {
            @Override
            public int compare(TeamInfo team1, TeamInfo team2) {
                return team1.points > team2.points ? -1 : team1.points == team2.points ? 0 : 1;
            }
        });
        TableLayout tableLayout = new TableLayout(this);
        int teamNumber = numberOfTeams[champNumber];
        TableRow titles = new TableRow(this);
        tableLayout.setStretchAllColumns(true);
        TextView[] columnTitle = new TextView[COLUMNS_NUMBER];
        for (int i = 0; i < COLUMNS_NUMBER; i++) {
            columnTitle[i] = new TextView(this);
            switch (i) {

                case 0:
                    columnTitle[i].setText("Команда");
                    break;
                case 1:
                    columnTitle[i].setText("Матчи");
                    break;
                case 2:
                    columnTitle[i].setText("Поб");
                    break;
                case 3:
                    columnTitle[i].setText("Нич");
                    break;
                case 4:
                    columnTitle[i].setText("Пор");
                    break;
                case 5:
                    columnTitle[i].setText("Разница");
                    break;
                case 6:
                    columnTitle[i].setText("Очки");
                    break;
            }
            columnTitle[i].setTextSize(16);
            columnTitle[i].setGravity(Gravity.CENTER_HORIZONTAL);
            titles.addView(columnTitle[i]);
        }
        tableLayout.addView(titles);
        TableRow[] tableRow = new TableRow[teamNumber];
        for (int i = 0; i < teamNumber; i++) {
            tableRow[i] = new TableRow(this);
        }

        TextView[][] columns = new TextView[teamNumber][COLUMNS_NUMBER - 1];
        Button[] teamButtons = new Button[teamNumber];
        for (int i = 0; i < teamNumber; i++) {
            teamButtons[i] = new Button(this);
            teamButtons[i].setBackgroundResource(R.drawable.champ_buttons);
            teamButtons[i].setText(String.valueOf(teamNames[champNumber][sortTeams[i].teamNumber]));
            teamButtons[i].setId(i);
            teamButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    Intent intent = new Intent(ScrollingActivity.this, TeamActivity.class);
                    intent.putExtra("teamName", teamNames[champNumber][sortTeams[id].teamNumber]);
                    intent.putExtra("teamNumber", sortTeams[id].teamNumber);
                    intent.putExtra("champNumber", champNumber);
                    startActivity(intent);

                }
            });
            tableRow[i].addView(teamButtons[i]);

            for (int j = 0; j < COLUMNS_NUMBER - 1; j++) {
                columns[i][j] = new TextView(this);
                columns[i][j] = new TextView(this);
                columns[i][j].setTextSize(16);
                columns[i][j].setGravity(Gravity.CENTER_HORIZONTAL);
                columns[i][j].setTypeface(null, Typeface.BOLD);
                columns[i][j].setTextColor(Color.BLACK);
                switch (j) {

                    case 0:
                        columns[i][j].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][1]));
                        break;
                    case 1:
                        columns[i][j].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][2]));
                        break;
                    case 2:
                        columns[i][j].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][3]));
                        break;
                    case 3:
                        columns[i][j].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][4]));
                        break;
                    case 4:
                        columns[i][j].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][5]) + "-" + String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][6]));
                        break;
                    case 5:
                        columns[i][j].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][7]));
                        break;
                }
                tableRow[i].addView(columns[i][j]);
            }
        }

        for (int i = 0; i < teamNumber; i++) {
            tableLayout.addView(tableRow[i]);
        }
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        ScrollView sv = new ScrollView(this);
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        sv.addView(tableLayout);
        hsv.addView(sv);
        setContentView(hsv);

    }

}
