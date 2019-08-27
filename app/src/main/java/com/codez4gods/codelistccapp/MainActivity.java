package com.codez4gods.codelistccapp;

import android.annotation.SuppressLint;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codez4gods.codelistccapp.fragments.cms;
import com.codez4gods.codelistccapp.fragments.main;
import com.codez4gods.codelistccapp.fragments.mobile;
import com.codez4gods.codelistccapp.fragments.plugin;
import com.codez4gods.codelistccapp.fragments.scripts;
import com.codez4gods.codelistccapp.utils.DBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "THID";

    ArrayList<model> dataModels;
    ListView listView;
    private static customAdapter adapter;
    public String base_img_url = "http://www.codelist.cc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragment(new main(),false,"main");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.frame_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    private void doit() {
        int max;
        int i = 1;
        listView= findViewById(R.id.list);

        dataModels = new ArrayList<>();

            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.codelist.cc/mobile").get();
                Log.d(TAG, "doit: " + doc.title());
                max = doc.getElementsByClass("short").size();
                System.out.println(max);
                for (i = 1; i <= max; i++) {
                    //#dle-content > div:nth-child(1)
                    Elements newsHeadlines = doc.select("#dle-content > div:nth-child(" + i + ")");
                    for (Element headline : newsHeadlines) {
                        // title works
                        String t =  headline.getElementsByClass("img-short").select("a").attr("title");
                        // image link works
                       //String src =  headline.getElementsByClass("img-short").select("a > img").attr("src");
                        // link works
                       String ref = headline.getElementsByClass("img-short").select("a").attr("href");
                        // description works
                       String desc =  headline.getElementsByClass("description").text();
                        Log.d(TAG, "doit: " + t + "" + ref + desc);
                        dataModels.add(new model("", t, desc, ref));
                        Log.d(TAG, "doit: " + dataModels);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                adapter= new customAdapter(dataModels,getApplicationContext());

                listView.setAdapter(adapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                model model= dataModels.get(position);

                Snackbar.make(view, model.getTitle()+"\n"+model.getContent()+" API: "+model.getDate(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                Intent i = new Intent(MainActivity.this, details.class);
                i.putExtra("link", model.getDate());
                //i.putExtra("desc", model.getContent());
                startActivity(i);

            }
        });
    }


    /**
     * A placeholder fragment containing a simple view.
     */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cms) {
            // Handle the camera action
            addFragment(new cms(),false,"cms");
        } else if (id == R.id.nav_scripts) {
            addFragment(new scripts(),false,"scripts");
        } else if (id == R.id.nav_apps) {
            addFragment(new mobile(),false,"mobile");
        } else if (id == R.id.nav_plugins) {
            addFragment(new plugin(),false,"plugin");
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
           AlertDialog.Builder alert = new AlertDialog.Builder(this).setMessage("Made By/ Feito Por " +
                   "\n \n Ruben Forner :)")
                   .setTitle("About/Acerca").setCancelable(true);
           alert.create().show();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    /*public void load(){
        listView= findViewById(R.id.list);

        dataModels = new ArrayList<>();

        dataModels.add(new model("Apple Pie", "Android 1.0", "1","September 23, 2008"));
        dataModels.add(new model("Marshmallow", "Android 6.0", "23","October 5, 2015"));

        adapter= new customAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                model model= dataModels.get(position);

                Snackbar.make(view, model.getTitle()+"\n"+model.getContent()+" API: "+model.getDate(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }*/
}
