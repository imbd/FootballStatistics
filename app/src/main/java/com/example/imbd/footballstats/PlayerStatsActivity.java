package com.example.imbd.footballstats;

import android.os.Bundle;
import android.widget.TextView;


public class PlayerStatsActivity extends Parent {
    String teamName = "";
    String playerName = "";
    int champNumber;
    int[] stats = new int[PLAYER_STATS_NUMBER];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);
        teamName = getIntent().getExtras().getString("teamName");
        playerName = getIntent().getExtras().getString("playerName");
        champNumber = getIntent().getExtras().getInt("champNumber");
        for (int i = 0; i < PLAYER_STATS_NUMBER; i++) {
            stats[i] = getIntent().getExtras().getInt(String.valueOf(i));
        }
        setTitle(playerName + '(' + teamName + ')');
        TextView tv = (TextView) findViewById(R.id.textView);
        String all_text = "";
        if (stats[4] == 0) {
            for (int i = 0; i < 4; i++) {
                all_text += (PRINT_STATS[i] + ": " + stats[i] + "\n\n");
            }
            if (stats[2] != 0) {
                double res = (double) stats[1] / (double) stats[2];
                all_text += "Минут на гол: " + Math.round(res * 100.0) / 100.0 + "\n\n";

            }
            if (stats[3] != 0) {
                double res = (double) stats[1] / (double) stats[3];
                all_text += "Минут на голевую передачу: " + Math.round(res * 100.0) / 100.0 + "\n\n";

            }
        } else {

            for (int i = 0; i < PLAYER_STATS_NUMBER; i++) {
                if (i == 2 || i == 3) {
                    continue;
                }
                all_text += (PRINT_STATS[i] + ": " + stats[i] + "\n\n");
            }
            if (stats[0] != 0) {
                double res = (double) stats[4] / (double) stats[0];
                all_text += "В среднем пропущено: " + Math.round(res * 100.0) / 100.0 + "\n\n";
            }
        }
        if (stats[0] == 0) {
            all_text = (PRINT_STATS[0] + ": " + stats[0] + "\n\n");
        }
        tv.setText(all_text);
    }
}
