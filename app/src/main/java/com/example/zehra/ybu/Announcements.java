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

public class Announcements extends Fragment {
    private View v;
    private ArrayList<String> announcement_links = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> announcement_list = new ArrayList<>();
    private ListView announcements ;
    private final String http = "http://ybu.edu.tr/muhendislik/bilgisayar/";
    public Announcements( ) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.announcements,container,false);
        announcements = v.findViewById(R.id.announcements);
        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,announcement_list);

        new getAnnouncements().execute();



        announcements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(http+announcement_links.get(position))));
            }
        });
        return v ;
    }

    private class getAnnouncements extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            announcement_list.removeAll(announcement_list);
            try {
                Document doc = Jsoup.connect(http).timeout(30*1000).get();

                for( Element element : doc.select("div[class=cncItem]") )
                {
                    if(element.parent().parent().hasClass("contentAnnouncements")){
                        announcement_list.add(element.text().toString());
                        announcement_links.add(element.child(0).child(1).attr("href"));
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
            announcements.setAdapter(arrayAdapter);

        }
    }
}
