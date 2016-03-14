package com.example.imbd.footballstats;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

public class TeamPlayerStatsActivity extends Parent {

    int type;
    int champNumber;
    int teamNumber;
    String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_player_stats);
        champNumber = getIntent().getExtras().getInt("champNumber");
        teamNumber = getIntent().getExtras().getInt("teamNumber");
        teamName = getIntent().getExtras().getString("teamName");
        type = getIntent().getExtras().getInt("type");
        setTitle(teamName);
        TableLayout tableLayout = new TableLayout(this);
        TableRow titles = new TableRow(this);
        tableLayout.setStretchAllColumns(true);
        TextView title1 = new TextView(this);
        title1.setText("Игрок");
        title1.setGravity(Gravity.CENTER_HORIZONTAL);
        titles.addView(title1);

        int number = numberOfPlayers[champNumber][teamNumber];
        TableRow[] tableRows = new TableRow[number];
        Button[] names = new Button[number];
        TextView[] goals = new TextView[number];
        if (type == 0) {
            tableLayout.addView(titles);
            for (int i = 0; i < number; i++) {
                names[i] = new Button(this);
                tableRows[i] = new TableRow(this);
                names[i].setText(playerNames[champNumber][teamNumber][i]);
                names[i].setId(i);
                names[i].setBackgroundResource(R.drawable.players_buttons);
                names[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int playerNumber = v.getId();
                        String playerName = playerNames[champNumber][teamNumber][playerNumber];
                        Intent intent = new Intent(TeamPlayerStatsActivity.this, PlayerStatsActivity.class);
                        intent.putExtra("teamName", teamNames[champNumber][teamNumber]);
                        intent.putExtra("playerName", playerName);
                        intent.putExtra("champNumber", champNumber);
                        for (int j = 0; j < PLAYER_STATS_NUMBER; j++) {
                            intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][playerNumber][j]);

                        }
                        startActivity(intent);
                    }
                });
                tableRows[i].addView(names[i]);
                tableLayout.addView(tableRows[i]);
            }
            tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            ScrollView sv = new ScrollView(this);
            sv.addView(tableLayout);
            setContentView(sv);
            return;
        }
        TextView title2 = new TextView(this);
        title2.setText("Голы");
        title2.setGravity(Gravity.CENTER_HORIZONTAL);
        titles.addView(title2);
        tableLayout.addView(titles);

        final Score[] sortPlayers = new Score[numberOfPlayers[champNumber][teamNumber]];
        for (int i = 0; i < numberOfPlayers[champNumber][teamNumber]; i++) {
            Score newPlayer = new Score(playerNames[champNumber][teamNumber][i], playerStats[champNumber][teamNumber][i][2], i);
            sortPlayers[i] = newPlayer;
        }
        Arrays.sort(sortPlayers, new Comparator<Score>() {
            @Override
            public int compare(Score n1, Score n2) {
                return n1.goals > n2.goals ? -1 : n1.goals == n2.goals ? 0 : 1;
            }
        });
        for (int i = 0; i < number; i++) {
            names[i] = new Button(this);
            goals[i] = new TextView(this);
            tableRows[i] = new TableRow(this);
            int num = sortPlayers[i].number;
            goals[i].setText(String.valueOf(sortPlayers[i].goals));
            goals[i].setTextSize(16);
            goals[i].setGravity(Gravity.CENTER_HORIZONTAL);
            goals[i].setTypeface(null, Typeface.BOLD);
            goals[i].setTextColor(Color.BLACK);
            names[i].setText(playerNames[champNumber][teamNumber][num]);
            names[i].setId(num);
            names[i].setBackgroundResource(R.drawable.players_buttons);
            names[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int playerNumber = v.getId();
                    String playerName = playerNames[champNumber][teamNumber][playerNumber];
                    Intent intent = new Intent(TeamPlayerStatsActivity.this, PlayerStatsActivity.class);
                    intent.putExtra("teamName", teamNames[champNumber][teamNumber]);
                    intent.putExtra("playerName", playerName);
                    intent.putExtra("champNumber", champNumber);
                    for (int j = 0; j < PLAYER_STATS_NUMBER; j++) {
                        intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][playerNumber][j]);

                    }
                    startActivity(intent);
                }
            });
            tableRows[i].addView(names[i]);
            tableRows[i].addView(goals[i]);
            tableLayout.addView(tableRows[i]);
        }
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        ScrollView sv = new ScrollView(this);
        sv.addView(tableLayout);
        setContentView(sv);
    }
}
