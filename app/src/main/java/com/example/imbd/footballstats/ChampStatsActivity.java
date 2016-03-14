package com.example.imbd.footballstats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChampStatsActivity extends Parent {

    int champNumber;
    String champ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_stats);

        champNumber = getIntent().getExtras().getInt("champNumber");
        champ = getIntent().getExtras().getString("champName");
        setTitle(champ);


    }

    public void tableButton(View v) {

        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }

        Intent intent = new Intent(ChampStatsActivity.this, ScrollingActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("champName", champ);
        startActivity(intent);
    }

    public void matchesButton(View v) {

        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }

        Intent intent = new Intent(ChampStatsActivity.this, MatchActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("champName", champ);
        startActivity(intent);
    }

    public void graphsButton(View v) {
        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }

        Intent intent = new Intent(ChampStatsActivity.this, ChartActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("champName", champ);
        startActivity(intent);
    }

    public void showScorers(View v) {
        Button button = (Button) findViewById(v.getId());
        int type = 0;
        switch (button.getId()) {
            case R.id.button6:
                type = 2;
                break;
            case R.id.button7:
                type = 3;
                break;
            case R.id.button8:
                type = 1;
                break;
            case R.id.button9:
                type = 4;
                break;
            case R.id.button10:
                break;
            default:
                break;
        }

        if (numberOfPlayersInChamp[champNumber] == 0) {
            showAlertSuggestion();
            return;
        }
        Intent intent = new Intent(ChampStatsActivity.this, ChampStatisticsActivity.class);
        intent.putExtra("champNumber", champNumber);
        intent.putExtra("champName", champ);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
