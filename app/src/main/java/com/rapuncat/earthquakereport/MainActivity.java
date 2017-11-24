package com.rapuncat.earthquakereport;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String address =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    EarthQuakeAdapter earth_adapter;
    ListView earthquakeListView;
    private TextView mEmptyStateTextView;
    private ListView drawerList;
    String[] leftMenuItems;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private CharSequence drawerTitle;
    private CharSequence title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setListView();
        networkCheck();

        setSlidingMenu(savedInstanceState);
        registerForContextMenu(earthquakeListView);

    }

    public void setListView(){
        earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(new EarthQuakeAdapter(this, new ArrayList<EarthQuake>()));

        mEmptyStateTextView = (TextView) findViewById(R.id.text);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
    }

    public void setSlidingMenu(Bundle savedInstanceState)
    {
        title =  getTitle();
        drawerTitle = "Earthquake Report";
        leftMenuItems = getResources().getStringArray(R.array.left_menu_names);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.drawerList);

        drawerLayout.setDrawerShadow(R.drawable.aaa,
                GravityCompat.START);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, leftMenuItems));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, // Host Activity
                drawerLayout, // layout container for navigation drawer, // Open Drawer Description
                R.string.drawer_open, // Open Drawer Description
                R.string.drawer_close) // Close Drawer Description

        {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        class DrawerItemClickListener implements
                ListView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                selectItem(position);}}
        if (savedInstanceState == null) {
            selectItem(0);
        }

    }
    public void networkCheck()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        boolean isConnected = connMgr != null &&
                networkInfo.isConnectedOrConnecting();


        if (isConnected) {
            new EarthQuakeTask().execute(address);


        } else {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText("Check your connectivity");
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.open_website:
                openWebsite(earth_adapter.getItem(info.position).getUrl());
                return true;
            case R.id.open_map:
                String location = earth_adapter.getItem(info.position).getLocation();
                double latitude = earth_adapter.getItem(info.position).getLatitude();
                double longitude = earth_adapter.getItem(info.position).getLongitude();
                openMap(location,latitude,longitude);
                return true;

            default:
                return super.onContextItemSelected(item);

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

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        else if(id == R.id.refresh)
        {
            new EarthQuakeTask().execute(address);
        }
        else if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openWebsite(String url)
    {
        Uri earthquakeUri = Uri.parse(url);

        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

        startActivity(websiteIntent);
    }
    private void openMap(String location,double latitude, double longitude)
    {
//        GoogleMap googleMap = new GoogleMap();
        Intent mapIntent = new Intent(MainActivity.this,MapsActivity.class);
        Bundle b = new Bundle();

        b.putString("location",location);
        b.putDouble("latitude",latitude);
        b.putDouble("longitude",longitude);

        mapIntent.putExtras(b);

        startActivity(mapIntent);


    }

    // Click Event of navigation drawer item click
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
            Intent webIntent = new Intent(MainActivity.this,WebSitesActivity.class);
            Bundle b = new Bundle();
            String [] webLinks = getResources().getStringArray(R.array.website_names);

            b.putString("url",webLinks[position]);
            b.putString("title",leftMenuItems[position]);
            webIntent.putExtras(b);

            startActivity(webIntent);
        }
    }

    private void selectItem(int position) {

        // Set Fragment text accordingly
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        args.putString("name", leftMenuItems[position]);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameContainer,fragment).commit();

        // Update Title on action bar
        drawerList.setItemChecked(position, true);
       //  drawerLayout.closeDrawer(drawerList);


    }

    @Override
    public void setTitle(CharSequence title) {
         getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

     /* Asynctask for UI listview updates */
    private class EarthQuakeTask extends AsyncTask<String, Void, List<EarthQuake>> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected List<EarthQuake> doInBackground(String... address) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String limit = sharedPrefs.getString(
                    getString(R.string.limit),
                    getString(R.string.default_limit));
            String max_magnitude = sharedPrefs.getString(
                    getString(R.string.max_magnitude),
                    getString(R.string.default_max_magnitude));

            String min_magnitude = sharedPrefs.getString(
                    getString(R.string.min_magnitude),
                    getString(R.string.default_min_magnitude));


            Uri baseUri = Uri.parse(address[0]);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter("format", "geojson");
            uriBuilder.appendQueryParameter("limit", limit );
            uriBuilder.appendQueryParameter("orderby", "time");
            uriBuilder.appendQueryParameter("maxmagnitude", max_magnitude);
            uriBuilder.appendQueryParameter("minmagnitude", min_magnitude);

            Log.d("URL ",uriBuilder.toString());

            List<EarthQuake> earthQuakes = Connection.fetchEarthquakeData(uriBuilder.toString());

            return earthQuakes;

        }

        @Override
        protected void onPostExecute(List<EarthQuake> earthQuakes) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (earthQuakes != null && !earthQuakes.isEmpty()) {

                earth_adapter = new EarthQuakeAdapter(getBaseContext(), (ArrayList<EarthQuake>) earthQuakes);
                earthquakeListView = (ListView) findViewById(R.id.list);
                earthquakeListView.setAdapter(earth_adapter);

            }
            else{
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText("No data available");
             }



        }
    }




}