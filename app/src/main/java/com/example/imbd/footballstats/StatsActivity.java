package com.example.imbd.footballstats;

import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends Parent {

    int teamNumber, champNumber;
    String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        teamNumber = getIntent().getExtras().getInt("teamNumber");
        champNumber = getIntent().getExtras().getInt("champNumber");
        teamName = getIntent().getExtras().getString("teamName");
        setTitle(teamName);
        TextView tv = (TextView) findViewById(R.id.textView5);
        String text = "";
        for (int j = 0; j < TEAM_STATS_NUMBER; j++) {
            text += TEAM_STATS[j] + ": " + teamStats[champNumber][teamNumber][j] + "\n\n";
        }
        tv.setText(text);
    }
}
