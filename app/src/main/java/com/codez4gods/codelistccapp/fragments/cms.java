package com.codez4gods.codelistccapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codez4gods.codelistccapp.R;
import com.codez4gods.codelistccapp.customAdapter;
import com.codez4gods.codelistccapp.details;
import com.codez4gods.codelistccapp.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class cms extends Fragment {
    String TAG = "THID";

    ArrayList<model> dataModels;
    ListView listView;
    customAdapter adapter;
    String base_img_url = "http://www.codelist.cc";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(
                R.layout.frag_main, container, false);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    doit(root);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        //Inflate the layout for this fragment

        return root;
    }

    private void doit(View view) {
        int max;
        int i = 1;
        listView = view.findViewById(R.id.list);

        dataModels = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.codelist.cc/nulled-cms/").get();
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
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                adapter= new customAdapter(dataModels,getActivity());

                listView.setAdapter(adapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                model model= dataModels.get(position);

                Snackbar.make(view, model.getTitle()+"\n"+model.getContent()+" API: "+model.getDate(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                Intent i = new Intent(getActivity(), details.class);
                i.putExtra("link", model.getDate());
                //i.putExtra("desc", model.getContent());
                startActivity(i);

            }
        });
    }
}