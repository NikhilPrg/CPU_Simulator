package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FCFS");
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("selectedPreset", 1);
            editor.putInt("speed", 500);
            editor.commit();

            SharedPreferences.Editor editor2 = prefs.edit();
            editor2.putBoolean("firstTime", true);
            editor2.commit();
        }

        final ProcessRecordDataSupply dataSupply = new ProcessRecordDataSupply(this);
        if(dataSupply.getListOfIds() == null || dataSupply.getListOfIds().length == 0) {
            ArrayList<Process> preset = new ArrayList<Process>();
            preset.add(new Process("P0", 10 ,4));
            preset.add(new Process("P1", 5 , 2));
            preset.add(new Process("P2", 6 , 3));
            preset.add(new Process("P3", 3 , 2));
            preset.add(new Process("P4", 7 , 1));
            preset.add(new Process("P5", 4 , 1));
            preset.add(new Process("P6", 3 , 4));
            preset.add(new Process("P7", 5 , 6));
            preset.add(new Process("P8", 2 , 2));

            dataSupply.setToPresetList(preset, 1);

            preset.removeAll(preset);
            preset.add(new Process("P0", 1, 0));
            preset.add(new Process("P1", 2, 2));
            preset.add(new Process("P2", 3, 2));
            preset.add(new Process("P3", 4, 3));
            dataSupply.setToPresetList(preset, 2);

            preset.removeAll(preset);
            preset.add(new Process("P0", 6, 2));
            preset.add(new Process("P1", 3, 1));
            preset.add(new Process("P2", 2, 5));
            preset.add(new Process("P3", 9, 3));
            preset.add(new Process("P3", 4, 3));
            preset.add(new Process("P3", 2, 2));
            preset.add(new Process("P3", 7, 11));
            dataSupply.setToPresetList(preset, 3);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout ll = (LinearLayout) findViewById(R.id.main_fragment_container);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getSupportFragmentManager()
                .beginTransaction()
                .add(ll.getId(), new SimulationFragment(), "FCFS")
                .disallowAddToBackStack()
                .commit();

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        drawer.openDrawer(GravityCompat.START);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        LinearLayout ll = (LinearLayout) findViewById(R.id.main_fragment_container);
        ll.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        SimulationFragment temp = new SimulationFragment();
        ll.removeAllViews();

        if (id == R.id.nav_fcfs) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
            toolbar.setTitle("FCFS");
            temp.setSchedulingAlgorithm(1);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "FCFS")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_sjf) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
            toolbar.setTitle("SJF");
            temp.setSchedulingAlgorithm(2);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "SJF")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_srtf) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
            toolbar.setTitle("SRTF");
            temp.setSchedulingAlgorithm(3);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "SJF")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_rr) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
            toolbar.setTitle("Round Robin");
            temp.setSchedulingAlgorithm(4);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "RR")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_edit) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), new PresetsEditFragment(), "Edit")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_speed) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), new SpeedFragment(), "Speed")
                    .disallowAddToBackStack()
                    .commit();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
