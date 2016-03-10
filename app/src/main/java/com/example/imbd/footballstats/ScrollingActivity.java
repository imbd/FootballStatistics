package com.example.imbd.footballstats;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

public class ScrollingActivity extends Parent {

    int champNumber;
    String champ;

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
        tableLayout.setStretchAllColumns(true);

        TextView player = new TextView(this);
        TextView team = new TextView(this);
        TextView score = new TextView(this);
        player.setText("Матчи");
        team.setText("Команда");
        score.setText("Очки");
        player.setTextSize(16);
        team.setTextSize(16);
        score.setTextSize(16);
        player.setGravity(Gravity.CENTER_HORIZONTAL);
        team.setGravity(Gravity.CENTER_HORIZONTAL);
        score.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow first = new TableRow(this);
        first.addView(team);
        first.addView(player);
        first.addView(score);
        tableLayout.addView(first);
        int teamNumber = numberOfTeams[champNumber];
        TableRow[] tableRow = new TableRow[teamNumber];
        for (int i = 0; i < teamNumber; i++) {
            tableRow[i] = new TableRow(this);
        }

        TextView[] pl = new TextView[teamNumber];
        for (int i = 0; i < teamNumber; i++) {
            pl[i] = new TextView(this);
        }

        Button[] tm = new Button[teamNumber];
        for (int i = 0; i < teamNumber; i++) {
            tm[i] = new Button(this);
            tm[i].setBackgroundResource(R.drawable.champ_buttons);
        }

        TextView[] gl = new TextView[teamNumber];
        for (int i = 0; i < teamNumber; i++) {
            gl[i] = new TextView(this);
        }

        for (int i = 0; i < teamNumber; i++) {
            tableRow[i].addView(tm[i]);
            tableRow[i].addView(pl[i]);
            tableRow[i].addView(gl[i]);
        }

        for (int i = 0; i < teamNumber; i++) {
            pl[i].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][1]));
            pl[i].setTextSize(16);
            pl[i].setGravity(Gravity.CENTER_HORIZONTAL);
            pl[i].setTypeface(null, Typeface.BOLD);
            pl[i].setTextColor(0xFF000000);
        }

        for (int i = 0; i < teamNumber; i++) {
            tm[i].setText(teamNames[champNumber][sortTeams[i].teamNumber]);
        }
        for (int i = 0; i < teamNumber; i++) {
            gl[i].setText(String.valueOf(teamStats[champNumber][sortTeams[i].teamNumber][7]));
            gl[i].setTextSize(16);
            gl[i].setGravity(Gravity.CENTER_HORIZONTAL);
            gl[i].setTypeface(null, Typeface.BOLD);
            gl[i].setTextColor(0xFF000000);
        }

        for (int i = 0; i < teamNumber; i++) {
            tableLayout.addView(tableRow[i]);
        }
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        ScrollView sv = new ScrollView(this);

        sv.addView(tableLayout);
        setContentView(sv);

    }
}
