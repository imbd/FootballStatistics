package com.example.imbd.footballstats;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.util.Arrays;
import java.util.Comparator;

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
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        for (int i = 0; i < numberOfPlayers[champNumber][teamNumber]; i++) {
            popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, playerNames[champNumber][teamNumber][i]);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int num = item.getItemId();
                Intent intent = new Intent(TeamActivity.this, PlayerStatsActivity.class);
                intent.putExtra("teamRef", teamRef);
                intent.putExtra("teamName", teamName);
                intent.putExtra("playerName", playerNames[champNumber][teamNumber][num]);
                intent.putExtra("champNumber", champNumber);
                for (int j = 0; j < PLAYER_STATS_NUMBER; j++) {
                    intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][num][j]);
                }

                startActivity(intent);
                return true;
            }
        });
        popupMenu.show();
    }

    public void showScorersPopupMenu(View v) {

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

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        for (int i = 0; i < numberOfPlayers[champNumber][teamNumber]; i++) {
            popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, sortPlayers[i].name + "(" + sortPlayers[i].goals + ")");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int num = item.getItemId();
                num = sortPlayers[num].number;
                Intent intent = new Intent(TeamActivity.this, PlayerStatsActivity.class);
                intent.putExtra("teamRef", teamRef);
                intent.putExtra("teamName", teamName);
                intent.putExtra("playerName", playerNames[champNumber][teamNumber][num]);
                intent.putExtra("champNumber", champNumber);
                for (int j = 0; j < PLAYER_STATS_NUMBER; j++) {
                    intent.putExtra(String.valueOf(j), playerStats[champNumber][teamNumber][num][j]);
                }

                startActivity(intent);
                return true;
            }
        });

        popupMenu.show();
    }

    public void showSchedule(View v) {

        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);
        int num = -1;
        int weeksNumber = (numberOfTeams[champNumber] - 1) * 2;
        int matches = numberOfTeams[champNumber] / 2;
        for (int i = 0; i < weeksNumber; i++) {
            for (int j = 0; j < matches; j++) {
                MatchInfo match = results[champNumber][i][j];
                if (!match.goals1.equals("-")) {
                    continue;
                }
                if (match.name1.equals(teamName)) {
                    num++;
                    popupMenu.getMenu().add(Menu.NONE, num, Menu.NONE, match.name2 + "(дома)");
                }
                if (match.name2.equals(teamName)) {
                    num++;
                    popupMenu.getMenu().add(Menu.NONE, num, Menu.NONE, match.name1 + "(в гостях)");
                }

            }
        }
        popupMenu.show();
    }
}
