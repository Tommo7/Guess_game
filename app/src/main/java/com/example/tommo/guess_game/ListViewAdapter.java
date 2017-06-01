package com.example.tommo.guess_game;

/**
 * Created by tommo on 31/05/2017.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
        imageLoader.clearCache();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView first_name;
        TextView last_name;
        //TextView fppg;
        ImageView person;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        first_name = (TextView) itemView.findViewById(R.id.first_name);
        last_name = (TextView) itemView.findViewById(R.id.last_name);
        //fppg = (TextView) itemView.findViewById(R.id.fppg);

        // Locate the ImageView in listview_item.xml
        person = (ImageView) itemView.findViewById(R.id.person);

        // Capture position and set results to the TextViews
        first_name.setText(resultp.get(DataListActivity.FIRST_NAME));
        last_name.setText(resultp.get(DataListActivity.LAST_NAME));
        //fppg.setText(resultp.get(DataListActivity.FPPG));
        // Capture position and set results to the ImageView
        // Passes images URL into ImageLoader.class
        imageLoader.DisplayImage(resultp.get(DataListActivity.IMAGE_URL), person);
        // Capture ListView item click
        /*itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data first name
                intent.putExtra("first_name", resultp.get(DataListActivity.FIRST_NAME));
                // Pass all data last name
                intent.putExtra("last_name", resultp.get(DataListActivity.LAST_NAME));
                // Pass all data fppg
                intent.putExtra("fppg",resultp.get(DataListActivity.FPPG));
                // Pass all data image
                intent.putExtra("image_url", resultp.get(DataListActivity.IMAGE_URL));
                // Start SingleItemView Class
                context.startActivity(intent);

            }
        });*/
        return itemView;
    }
}