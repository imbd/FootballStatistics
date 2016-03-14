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

public class ChampStatisticsActivity extends Parent {
    int champNumber;
    String champ;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_statistics);
        champNumber = getIntent().getExtras().getInt("champNumber");
        champ = getIntent().getExtras().getString("champName");
        type = getIntent().getExtras().getInt("type");
        setTitle(champ);
        TableLayout tableLayout = new TableLayout(this);
        TableRow titles = new TableRow(this);
        tableLayout.setStretchAllColumns(true);
        TextView title1 = new TextView(this);
        title1.setText("Игрок");
        title1.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title2 = new TextView(this);
        switch (type) {
            case 2:
                title2.setText("Голы");
                break;
            case 3:
                title2.setText("Голевые передачи");
                break;
            case 1:
                title2.setText("Полезность");
                break;
            case 4:
                title2.setText("Надежность");
                break;
        }
        title2.setGravity(Gravity.CENTER_HORIZONTAL);
        titles.addView(title1);
        titles.addView(title2);
        tableLayout.addView(titles);
        TableRow[] tableRow = new TableRow[SHOWN_NUMBER];
        for (int i = 0; i < SHOWN_NUMBER; i++) {
            tableRow[i] = new TableRow(this);
        }
        Button[] names = new Button[SHOWN_NUMBER];
        TextView[] stats = new TextView[SHOWN_NUMBER];
        final PlayerInfo[] sortScorers = new PlayerInfo[numberOfPlayersInChamp[champNumber]];
        int num = 0;
        for (int i = 0; i < numberOfTeams[champNumber]; i++) {
            for (int j = 0; j < numberOfPlayers[champNumber][i]; j++) {
                PlayerInfo np = new PlayerInfo(i, j, 0);
                if (type == 2 || type == 3) {
                    np = new PlayerInfo(i, j, (double) playerStats[champNumber][i][j][type]);
                }
                if (type == 1) {
                    if (playerStats[champNumber][i][j][0] != 0) {
                        np = new PlayerInfo(i, j, (double) (playerStats[champNumber][i][j][2] + playerStats[champNumber][i][j][3]) / (double) playerStats[champNumber][i][j][0]);
                    } else {
                        np = new PlayerInfo(i, j, 0);
                    }
                }
                if (type == 4) {
                    if (playerStats[champNumber][i][j][4] != 0) {
                        double res = ((double) playerStats[champNumber][i][j][4] / (double) playerStats[champNumber][i][j][0]);
                        np = new PlayerInfo(i, j, res);
                    } else {
                        np = new PlayerInfo(i, j, Double.MAX_VALUE);
                    }
                }
                sortScorers[num] = np;
                num++;
            }
        }

        final int TYPE = type;
        Arrays.sort(sortScorers, new Comparator<PlayerInfo>() {
            @Override
            public int compare(PlayerInfo n1, PlayerInfo n2) {
                if (TYPE != 4) {
                    return n1.stat > n2.stat ? -1 : n1.stat == n2.stat ? 0 : 1;
                } else {
                    return n1.stat > n2.stat ? 1 : n1.stat == n2.stat ? 0 : -1;
                }
            }
        });

        for (int i = 0; i < SHOWN_NUMBER; i++) {
            int tn = sortScorers[i].teamNumber;
            int pn = sortScorers[i].playerNumber;
            names[i] = new Button(this);
            names[i].setId(i);
            names[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = v.getId();
                    int teamNumber = sortScorers[num].teamNumber;
                    int playerNumber = sortScorers[num].playerNumber;
                    String playerName = playerNames[champNumber][teamNumber][playerNumber];
                    Intent intent = new Intent(ChampStatisticsActivity.this, PlayerStatsActivity.class);
                    intent.putExtra("teamName", teamNames[champNumber][teamNumber]);
                    intent.putExtra("playerName", playerName);
                    intent.putExtra("champNumber", champNumber);
                    for (int j = 0; j < PLAYER_STATS_NUMBER; j++) {
                        intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][playerNumber][j]);

                    }
                    startActivity(intent);
                }
            });
            names[i].setBackgroundResource(R.drawable.players_buttons);
            names[i].setText(playerNames[champNumber][tn][pn]);
            tableRow[i].addView(names[i]);
            int res;
            double res2;
            stats[i] = new TextView(this);
            stats[i].setTextSize(16);
            stats[i].setGravity(Gravity.CENTER_HORIZONTAL);
            stats[i].setTypeface(null, Typeface.BOLD);
            stats[i].setTextColor(Color.BLACK);
            if (type == 2 || type == 3) {
                res = (int) sortScorers[i].stat;
                stats[i].setText(String.valueOf(res));
            } else {
                res2 = Math.round(sortScorers[i].stat * 100.0) / 100.0;
                stats[i].setText(String.valueOf(res2));
            }
            tableRow[i].addView(stats[i]);

        }
        for (int i = 0; i < SHOWN_NUMBER; i++) {
            tableLayout.addView(tableRow[i]);
        }
        tableLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        ScrollView sv = new ScrollView(this);
        sv.addView(tableLayout);
        setContentView(sv);

    }
}
