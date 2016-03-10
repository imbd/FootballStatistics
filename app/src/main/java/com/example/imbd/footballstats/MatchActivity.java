package com.example.imbd.footballstats;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;

public class MatchActivity extends Parent {

    static String champ;
    static int champNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        champNumber = getIntent().getExtras().getInt("champNumber");
        champ = getIntent().getExtras().getString("champName");
        setTitle(champ);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        int weeksNumber = (numberOfTeams[champNumber] - 1) * 2;
        String[] weeks = new String[weeksNumber];
        for (int i = 0; i < weeksNumber; i++) {
            weeks[i] = String.valueOf(i + 1) + " тур";
        }
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                weeks));
        spinner.setBackgroundColor(Color.RED);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);

        }
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("week", sectionNumber);
            args.putInt("champNumber", champNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_match, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            String text = "";
            int champNumber = getArguments().getInt("champNumber");
            int week = getArguments().getInt("week");
            week--;
            for (int i = 0; i < numberOfTeams[champNumber] / 2; i++) {
                text += results[champNumber][week][i].name1 + " - " + results[champNumber][week][i].name2 + " ";
                text += results[champNumber][week][i].goals1 + ":" + results[champNumber][week][i].goals2 + "\n\n";
            }
            textView.setTextSize(13);
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(text);
            return rootView;
        }
    }
}
