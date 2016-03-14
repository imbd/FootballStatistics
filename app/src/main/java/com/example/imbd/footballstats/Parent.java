package com.example.imbd.footballstats;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.lang.Math;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Parent extends AppCompatActivity {

    protected final static int NUMBER_OF_CHAMPS = 10;
    protected final static int MAX_TEAMS_NUMBER = 20;
    protected final static int MATCHES_NUMBER = 10;
    protected final static int MAX_PLAYERS_NUMBER = 50;
    protected final static int MAX_WEEKS_NUMBER = 38;
    protected final static int PLAYER_STATS_NUMBER = 5;
    protected final static int TEAM_STATS_NUMBER = 8;
    protected final static int SHOWN_NUMBER = 30;
    protected final String[] STATS = {"Сыгранные матчи", "Минут на поле", "Голы (с пенальти)", "Голевые передачи", "Пропущенные мячи"};
    protected final String[] PRINT_STATS = {"Сыгранные матчи", "Минут на поле", "Голы", "Голевые передачи", "Пропущенные мячи"};
    protected final String[] TEAM_STATS = {"Место", "Сыграно матчей", "Побед", "Ничьих", "Поражений", "Забитые", "Пропущенные", "Очки"};

    protected static int[][][][] playerStats = new int[NUMBER_OF_CHAMPS][MAX_TEAMS_NUMBER][MAX_PLAYERS_NUMBER][PLAYER_STATS_NUMBER];
    protected static String[][][] playerNames = new String[NUMBER_OF_CHAMPS][MAX_TEAMS_NUMBER][MAX_PLAYERS_NUMBER];
    protected static String[][] teamNames = new String[NUMBER_OF_CHAMPS][MAX_TEAMS_NUMBER];
    protected static MatchInfo[][][] results = new MatchInfo[NUMBER_OF_CHAMPS][MAX_WEEKS_NUMBER][MATCHES_NUMBER];
    protected static String[][] teamRefs = new String[NUMBER_OF_CHAMPS][MAX_TEAMS_NUMBER];
    protected static int[][][] teamStats = new int[NUMBER_OF_CHAMPS][MAX_TEAMS_NUMBER][TEAM_STATS_NUMBER];
    protected static int[] numberOfTeams = new int[NUMBER_OF_CHAMPS];
    protected static int[][] numberOfPlayers = new int[NUMBER_OF_CHAMPS][MAX_TEAMS_NUMBER];
    protected static int[] numberOfPlayersInChamp = new int[NUMBER_OF_CHAMPS];
    protected static boolean[] menu = new boolean[NUMBER_OF_CHAMPS];
    protected static boolean[] wasInterrupted = new boolean[NUMBER_OF_CHAMPS];
    protected static boolean wasShowed = false;
    protected static boolean oldData = false;
    protected static boolean noInternet = false;
    protected ProgressDialog progress;

    protected class Score {

        public String name;
        public int goals;
        public int number;

        public Score(String name, int goals, int number) {
            this.name = name;
            this.goals = goals;
            this.number = number;
        }
    }

    protected class MatchInfo {

        public String name1;
        public String name2;
        public String goals1;
        public String goals2;
        public String date;

        public MatchInfo(String name1, String name2, String goals1, String goals2, String date) {
            this.name1 = name1;
            this.goals1 = goals1;
            this.name2 = name2;
            this.goals2 = goals2;
            this.date = date;
        }
    }

    protected class PlayerInfo {

        public int teamNumber;
        public int playerNumber;
        public double stat;

        public PlayerInfo(int teamNumber, int playerNumber, double stat) {
            this.teamNumber = teamNumber;
            this.playerNumber = playerNumber;
            this.stat = stat;
        }
    }

    protected class TeamInfo {

        public int points;
        public int teamNumber;

        public TeamInfo(int points, int teamNumber) {
            this.points = points;
            this.teamNumber = teamNumber;
        }
    }

    AlertDialog.Builder buildAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder;
    }

    public void showAlertExplanation() {

        AlertDialog.Builder builder = buildAlert();
        builder.setTitle(R.string.noInternetWarning)
                .setMessage(R.string.internetExplanation);
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showAlertSuggestion() {
        AlertDialog.Builder builder = buildAlert();
        builder.setTitle(R.string.noInternetWarning)
                .setMessage((getString(R.string.internetSuggestion)));
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showAlertNoInternet() {

        AlertDialog.Builder builder = buildAlert();
        builder.setTitle(R.string.noInternetWarning)
                .setMessage(R.string.internetTryAgain);
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showAlertHaveInternet() {

        AlertDialog.Builder builder = buildAlert();
        builder.setTitle("Интернет есть")
                .setMessage("Ура!");
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static void deleteAllFilesFolder(String path) {
        for (File myFile : new File(path).listFiles())
            if (myFile.isFile())
                myFile.delete();
    }


    public static boolean isInternetAvailable() {
        noInternet = false;
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("Connection", "close");

            return (urlc.getResponseCode() == 200);
        } catch (Exception e) {
            noInternet = true;
            e.printStackTrace();
            return false;
        }
    }

    class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            isInternetAvailable();
            return "Ready";
        }
    }

    public void internetCheck() {
        noInternet = false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<String> future = executor.submit(new Task());
                Boolean wasTerm = false;
                try {
                    future.get(4, TimeUnit.SECONDS);
                } catch (Exception e) {
                    future.cancel(true);
                    wasTerm = true;
                    noInternet = true;
                }
                executor.shutdownNow();
                if (wasTerm) {
                    noInternet = true;
                }
            }
        });
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            noInternet = true;
            e.printStackTrace();
        }

    }

    public void makeRefs(int champNumber, final String champ, final String ref, final String type, int index) {

        try {
            String all_file = "";

            String path = getFilesDir() + File.separator + champ + ".xml";

            if (type.equals("table")) {
                path = getFilesDir() + File.separator + champ + "_table.xml";
            }
            if (type.equals("matches")) {
                path = getFilesDir() + File.separator + champ + "_matches.xml";
            }
            boolean noNeedToUseURL = false;
            boolean existed = false;
            File file = new File(path);
            if (file.exists()) {
                existed = true;
                Date lastModified = new Date(file.lastModified());
                Date date = new Date();
                if (date.getTime() - lastModified.getTime() <= 1000 * 60 * 60) {
                    noNeedToUseURL = true;
                }
            }
            String address = "";
            switch (type) {
                case "loadteams":
                    address = ref + "teams.html";
                    break;
                case "teamstats":
                    address = ref.replaceAll("result", "pstat");
                    break;
                case "table":
                    address = ref + "table/all.html";
                    break;
                case "matches":
                    address = ref + "calendar.html";
                    break;
                default:
                    break;
            }

            if (!noNeedToUseURL && existed && noInternet) {
                oldData = true;
                noNeedToUseURL = true;
            }
            if (noNeedToUseURL) {

                all_file = (new Scanner(new File(path))).useDelimiter("\\A").next();
            } else {
                URL url = null;
                if (!wasShowed)
                    internetCheck();
                if (noInternet || wasShowed) {
                    wasInterrupted[champNumber] = true;
                    return;
                }
                try {
                    url = new URL(address);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    wasInterrupted[champNumber] = true;
                }

                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    all_file = streamToString(inputStream);

                } catch (Exception e) {
                    e.printStackTrace();
                    wasInterrupted[champNumber] = true;
                    return;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            switch (type) {
                case "loadteams":
                    makeFile(champNumber, champ, all_file, "loadteams", -1);
                    break;
                case "teamstats":
                    makeFile(champNumber, champ, all_file, "teamstats", index);
                    break;
                case "table":
                    makeFile(champNumber, champ + "_table", all_file, "table", -2);
                    break;
                case "matches":
                    makeFile(champNumber, champ + "_matches", all_file, "matches", -2);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeFile(int champNumber, String filename, String data, String type, int index) {
        try {
            String path = getFilesDir() + File.separator + filename + ".xml";
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            String neededData = "";
            switch (type) {
                case "loadteams":
                    neededData = cleaningOfChampionshipTeamsFile(champNumber, data, filename);
                    break;
                case "teamstats":
                    neededData = cleaningOfTeamStatsFile(data);
                    break;
                case "table":
                    neededData = cleaningOfTableFile(data);
                    break;
                case "matches":
                    neededData = cleaningOfMatchesFile(data);
                    break;
                default:
                    break;
            }

            OutputStreamWriter oswriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            oswriter.write(neededData);
            oswriter.flush();
            oswriter.close();
            switch (type) {
                case "loadteams":
                    parseChampionshipTeamsFile(champNumber, file);
                    break;
                case "teamstats":
                    parseTeamStatsFile(champNumber, file, index);
                    break;
                case "table":
                    parseTableFile(champNumber, file);
                    break;
                case "matches":
                    parseMatchesFile(champNumber, file);
                    break;

                default:
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static String streamToString(InputStream s) {
        Scanner scanner = new Scanner(s);
        scanner.useDelimiter("\\A");
        return scanner.next();
    }

    public static String cleaningOfTeamStatsFile(String s) {
        String t = "<?xml version=" + '"' + "1.0" + '"' + " encoding=" + '"' + "utf-8" + '"' + "?>";
        t += "<start>";
        int first = s.indexOf("<tr d");
        int last = s.lastIndexOf("/tr>");
        t += s.substring(first, last + 4);
        t += "</start>";
        return t;
    }

    public String cleaningOfChampionshipTeamsFile(int champNumber, String s, String champ) {
        String t = "<?xml version=" + '"' + "1.0" + '"' + " encoding=" + '"' + "utf-8" + '"' + "?>";
        t += "<start>";
        int first, last, from = 0;
        while (true) {
            first = s.indexOf("<strong", from);
            last = s.indexOf("/strong>", from);
            from = last + 1;
            if (first == -1)
                break;
            t += s.substring(first, last + 8);
        }
        makeTeamRefFile(champNumber, s, champ);
        t += "</start>";
        return t;
    }


    public void makeTeamRefFile(int champNumber, String s, String champ) {

        boolean noNeedToParse = false;
        String path = getFilesDir() + File.separator + champ + "_teamrefs.xml";
        File file = new File(path);
        Date lastModified = new Date(file.lastModified());
        Date date = new Date();
        if (date.getTime() - lastModified.getTime() <= 1000 * 60 * 60 * 24 * 7) {
            noNeedToParse = true;
        }
        String t = "<?xml version=" + '"' + "1.0" + '"' + " encoding=" + '"' + "utf-8" + '"' + "?>";
        if (noNeedToParse) {
            try {
                t = (new Scanner(new File(path))).useDelimiter("\\A").next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            t += "<start>";
            int from = 0, first, last;
            while (true) {
                from = s.indexOf("width:", from);
                if (from == -1)
                    break;
                first = s.indexOf("<", from);
                last = s.indexOf(">", first);
                t += s.substring(first, last + 1);
                t += "</a>";
                from = last + 1;
            }
            t += "</start>";
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osWriter.write(t);
            osWriter.flush();
            osWriter.close();
            parseTeamRefFile(champNumber, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Document buildDoc(File xmlFile) {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            FileInputStream in = new FileInputStream(xmlFile);
            Document doc = dBuilder.parse(in);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void parseTeamStatsFile(int champNumber, File xmlFile, int index) {

        try {

            Document doc = buildDoc(xmlFile);
            NodeList nList = doc.getElementsByTagName("tr");
            int number = nList.getLength();
            numberOfPlayers[champNumber][index] = number;
            numberOfPlayersInChamp[champNumber] += number;
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < PLAYER_STATS_NUMBER; j++) {
                    playerStats[champNumber][index][i][j] = 0;
                }
                Node current = nList.item(i);
                NodeList child = current.getChildNodes();
                int childNumber = child.getLength();
                for (int k = 0; k < childNumber; k++) {
                    Node curNode = child.item(k);
                    if (curNode.getNodeType() == Node.ELEMENT_NODE) {
                        String text = curNode.getTextContent();
                        Element element = (Element) curNode;
                        if (element.hasAttribute("title")) {
                            String attr = element.getAttribute("title");
                            for (int j = 0; j < PLAYER_STATS_NUMBER; j++) {
                                if (STATS[j].equals(attr)) {
                                    playerStats[champNumber][index][i][j] = 0;
                                    if (text.length() != 0) {
                                        if (text.indexOf('(') != -1) {
                                            text = text.substring(0, text.indexOf('(') - 1);
                                            text.replaceAll(getString(R.string.space), "");
                                        }
                                        playerStats[champNumber][index][i][j] = Integer.valueOf(text);
                                    }
                                }
                            }
                        }
                        if (element.hasAttribute("sortOrder")) {
                            playerNames[champNumber][index][i] = text;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseChampionshipTeamsFile(int champNumber, File xmlFile) {

        try {
            Document doc = buildDoc(xmlFile);
            NodeList nList = doc.getElementsByTagName("strong");
            int number = nList.getLength();
            numberOfTeams[champNumber] = number;
            for (int i = 0; i < number; i++) {
                Node current = nList.item(i);
                teamNames[champNumber][i] = current.getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseTeamRefFile(int champNumber, File xmlFile) {

        try {
            Document doc = buildDoc(xmlFile);
            NodeList nList = doc.getElementsByTagName("a");
            int number = nList.getLength();
            numberOfTeams[champNumber] = number;
            for (int i = 0; i < number; i++) {
                Node current = nList.item(i);
                Element curElement = (Element) current;
                if (curElement.hasAttribute("href")) {
                    teamRefs[champNumber][i] = "http://www.championat.com" + curElement.getAttribute("href");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String cleaningOfTableFile(String s) {
        String t = "<?xml version=" + '"' + "1.0" + '"' + " encoding=" + '"' + "utf-8" + '"' + "?>";
        t += "<start>";
        int first = s.indexOf("<tr>");
        int last = s.lastIndexOf("/tr>");
        t += s.substring(first, last + 4);
        t += "</start>";

        return t;
    }

    public void parseTableFile(int champNumber, File xmlFile) {

        try {

            Document doc = buildDoc(xmlFile);
            NodeList nList = doc.getElementsByTagName("tr");
            int number = nList.getLength();
            for (int i = 2; i < number; i++) {
                Node current = nList.item(i);
                Element curElement = (Element) current;
                NodeList td = curElement.getElementsByTagName("td");
                int teamNumber = -1;
                String teamName = ((Element) td.item(3)).getElementsByTagName("a").item(0).getTextContent();
                for (int j = 0; j < numberOfTeams[champNumber]; j++) {
                    if (teamNames[champNumber][j].equals(teamName)) {
                        teamNumber = j;
                        break;
                    }
                }
                teamStats[champNumber][teamNumber][0] = Integer.valueOf(td.item(0).getTextContent());
                teamStats[champNumber][teamNumber][1] = Integer.valueOf(((Element) td.item(4)).getElementsByTagName("strong").item(0).getTextContent());
                teamStats[champNumber][teamNumber][2] = Integer.valueOf(td.item(5).getTextContent());
                teamStats[champNumber][teamNumber][3] = Integer.valueOf(td.item(6).getTextContent());
                teamStats[champNumber][teamNumber][4] = Integer.valueOf(td.item(7).getTextContent());
                teamStats[champNumber][teamNumber][5] = Integer.valueOf(((Element) td.item(8)).getElementsByTagName("span").item(0).getTextContent());
                teamStats[champNumber][teamNumber][6] = Integer.valueOf(((Element) td.item(8)).getElementsByTagName("span").item(2).getTextContent());
                teamStats[champNumber][teamNumber][7] = Integer.valueOf(((Element) td.item(9)).getElementsByTagName("strong").item(0).getTextContent());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String cleaningOfMatchesFile(String s) {
        String t = "<?xml version=" + '"' + "1.0" + '"' + " encoding=" + '"' + "utf-8" + '"' + "?>";
        t += "<start>";
        int first = s.indexOf("<tbody>");
        int last = s.lastIndexOf("/tbody>");
        t += s.substring(first, last + 7);
        t += "</start>";
        t.replace("<tbody>", "");
        t.replace("</tbody>", "");
        return t;
    }

    public void parseMatchesFile(int champNumber, File xmlFile) {

        int[] curNumber = new int[MAX_WEEKS_NUMBER];
        try {

            Document doc = buildDoc(xmlFile);
            NodeList nList = doc.getElementsByTagName("tr");
            int number = nList.getLength();
            for (int i = 0; i < number; i++) {
                Node current = nList.item(i);
                Element curElement = (Element) current;
                NodeList td = curElement.getElementsByTagName("td");

                if (td.getLength() < 5) {
                    continue;
                }
                int week = Integer.valueOf(td.item(1).getTextContent());
                week--;
                String date = (td.item(2)).getTextContent();
                date = date.trim();
                if (date.contains(",")) {
                    date = date.substring(0, date.indexOf(","));
                }
                String firstTeam = ((Element) td.item(3)).getElementsByTagName("a").item(0).getTextContent();
                String secondTeam = ((Element) td.item(3)).getElementsByTagName("a").item(1).getTextContent();
                Element el = (Element) ((Element) td.item(4)).getElementsByTagName("a").item(0);
                String score1 = el.getElementsByTagName("span").item(0).getTextContent();
                String score2 = el.getElementsByTagName("span").item(1).getTextContent();
                results[champNumber][week][curNumber[week]] = new MatchInfo(firstTeam, secondTeam, score1, score2, date);
                curNumber[week]++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
