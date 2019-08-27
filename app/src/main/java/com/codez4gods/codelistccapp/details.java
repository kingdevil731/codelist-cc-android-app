package com.codez4gods.codelistccapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class details extends AppCompatActivity {

    Document doc = null;
    String link = "";
    String TAG = "DEBUG";
    String t, image, ref, desc, demo_url = "";
    TextView titl, extras, linka, des;
    ImageView img;
    List<String> myArrayOfLinks = new ArrayList<String>();
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        titl = findViewById(R.id.title_details);
        extras = findViewById(R.id.extra_details);
        //linka = findViewById(R.id.links);
        des = findViewById(R.id.desc_details);
        img = findViewById(R.id.img_details);
        listView = findViewById(R.id.listv);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    doit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        link = getIntent().getStringExtra("link");
        //desc = getIntent().getStringExtra("desc");
        Log.d(TAG, link);

    }

    private void doit() {
        try {
            doc = Jsoup.connect(link).get();
            //Elements newsHeadlines = doc.select("#dle-content > div.full > div.full-news");
                t =  doc.select("#dle-content > div.full > h1").val("h1").text();
                // image link works
                //#dle-content > div.full > div.full-news > img
                image = "http://www.codelist.cc" + doc.select("#dle-content > div.full > div.full-news > img").attr("src");
                Log.d(TAG, "image: " + image);
                // link works
            for (Element el : doc.select("#dle-content > div.full > div.full-news > div.quote")) {
                // need learn more about text nodes..!!!!!
                for (TextNode node : el.textNodes()) {
                    myArrayOfLinks.add( node.text());
                }
            }


            for (Element el : doc.select("#dle-content > div.full > div.full-news")) {
                desc = el.childNodes().get(6).toString();
            }

                // demo url
                demo_url = doc.select("#dle-content > div.full > div.full-news > a:nth-child(6)").text();
                Log.d(TAG, "demo url: " + demo_url);
            // SET'S

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //title
                     titl.setText(t);
                    // description
                    des.setText(desc);
                    // demo url
                    extras.setText(demo_url);
                    // image
                    Picasso.get()
                            .load(image)
                            .into(img);
                    // urls
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            details.this,
                            R.layout.details_listview,
                            myArrayOfLinks );
                    listView.setAdapter(arrayAdapter);
                }
                    //linka.setText(ref);



        });
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        }
}
