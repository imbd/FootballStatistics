package com.example.imbd.footballstats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TeamActivity extends Parent {

    public String teamRef = "";
    public String teamName = "";
    public int champNumber;
    public int teamNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        teamName = getIntent().getExtras().getString("teamName");
        teamRef = getIntent().getExtras().getString("teamRef");
        teamNumber = getIntent().getExtras().getInt("teamNumber");
        champNumber = getIntent().getExtras().getInt("champNumber");
        setTitle(teamNames[champNumber][teamNumber]);

    }

    public void statsButton(View v) {
        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }

        Intent intent = new Intent(TeamActivity.this, StatsActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("teamName", teamName);
        intent.putExtra("teamNumber", teamNumber);
        startActivity(intent);
    }


    public void graphsButton(View v) {
        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }

        Intent intent = new Intent(TeamActivity.this, GraphActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("teamName", teamName);
        intent.putExtra("teamNumber", teamNumber);
        intent.putExtra("graphType", 0);
        startActivity(intent);
    }

    public void diffButton(View v) {
        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }

        Intent intent = new Intent(TeamActivity.this, GraphActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("teamName", teamName);
        intent.putExtra("teamNumber", teamNumber);
        intent.putExtra("graphType", 1);
        startActivity(intent);
    }

    public void pieChartButton(View v) {
        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }

        Intent intent = new Intent(TeamActivity.this, PieChartsActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("teamName", teamName);
        intent.putExtra("teamNumber", teamNumber);
        startActivity(intent);
    }

    public void showPlayersPopupMenu(View v) {

        Intent intent = new Intent(TeamActivity.this, TeamPlayerStatsActivity.class);
        intent.putExtra("teamName", teamName);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("teamNumber", teamNumber);
        intent.putExtra("type", 0);
        startActivity(intent);

    }

    public void showScorersPopupMenu(View v) {

        Intent intent = new Intent(TeamActivity.this, TeamPlayerStatsActivity.class);
        intent.putExtra("teamName", teamName);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("teamNumber", teamNumber);
        intent.putExtra("type", 1);
        startActivity(intent);

    }

    public void showSchedule(View v) {

        Intent intent = new Intent(TeamActivity.this, ScheduleActivity.class);
        intent.putExtra("teamName", teamName);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("teamNumber", teamNumber);
        startActivity(intent);

    }
}
