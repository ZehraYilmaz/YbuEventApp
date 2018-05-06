package com.example.zehra.ybu;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Zehra on 4.05.2018.
 */

public class News extends Fragment {
    private View v;
    private ListView news;
    private ArrayList<String> new_links = new ArrayList<>();
    private ArrayList<String> new_list = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter ;
    private final String http = "http://ybu.edu.tr/muhendislik/bilgisayar/";
    public News() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.news,container,false);

        news = v.findViewById(R.id.news);
        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,new_list);

        new getNews().execute();

        news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(http+new_links.get(position))));
            }
        });
        return v ;
    }

    private class getNews extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {

            new_list.removeAll(new_list);
            try {
                Document doc = Jsoup.connect(http).timeout(30*1000).get();

                for( Element element : doc.select("div[class=cncItem]") )
                {
                    if(element.parent().parent().hasClass("contentNews")){
                       new_list.add(element.text().toString());
                        new_links.add(element.child(0).child(1).attr("href"));

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            news.setAdapter(arrayAdapter);

        }
    }
}
