package com.example.zehra.ybu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Zehra on 4.05.2018.
 */

public class Food extends Fragment {
    private View v;
    private ListView food;
    private ArrayList<String> food_list = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter ;
    private ViewFlipper v_flipper;
    public Food() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.food_list, container, false);
        food = v.findViewById(R.id.food);
        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,food_list);
        v_flipper = v.findViewById(R.id.imgFlipper);

        int slides[] = {R.drawable.food_slide_1,R.drawable.food_slide_2,R.drawable.food_slide_3};

        for (int image: slides) {
            setV_flipper(image);
        }
        new getFood().execute();


            return v;
    }

    private void setV_flipper(int image){

        ImageView imageView = new ImageView(v.getContext());
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(v.getContext(),android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(v.getContext(),android.R.anim.slide_out_right);

    }

    private class getFood extends AsyncTask{
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            final String http = "http://ybu.edu.tr/sks/";
            food_list.removeAll(food_list);
            try {
                Document doc = Jsoup.connect(http).timeout(30*1000).get();

                for( Element element : doc.select("font") )
                {
                    food_list.add(element.text());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            food.setAdapter(arrayAdapter);

        }
    }
}
