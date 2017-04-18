package com.kurama.nikhil.cpusimulator;

import android.os.Bundle;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FCFS");
        setSupportActionBar(toolbar);

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
            Toast.makeText(this, "Dev. by Nikhil Pragna", Toast.LENGTH_LONG).show();
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
            toolbar.setTitle("FCFS");
            temp.setSchedulingAlgorithm(1);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "FCFS")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_sjf) {
            toolbar.setTitle("SJF");
            temp.setSchedulingAlgorithm(2);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "SJF")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_srtf) {
            toolbar.setTitle("SRTF");
            temp.setSchedulingAlgorithm(3);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "SJF")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_rr) {
            toolbar.setTitle("Round Robin");
            temp.setSchedulingAlgorithm(4);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), temp, "RR")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_edit) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(ll.getId(), new EditFragment(), "Edit")
                    .disallowAddToBackStack()
                    .commit();
        } else if (id == R.id.nav_speed) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
