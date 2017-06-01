package com.example.tommo.guess_game;

/**
 * Created by tommo on 31/05/2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleItemView extends Activity {
    // Declare Variables
    String first_name;
    String last_name;
    //String fppg;
    String image_url;
    String position;
    ImageLoader imageLoader = new ImageLoader(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_single_item_view);

        Intent i = getIntent();
        // Get the result of first name
        first_name = i.getStringExtra("first_name");
        // Get the result of last name
        last_name = i.getStringExtra("last_name");
        // Get the result of fppg
        //fppg = i.getStringExtra("fppg");
        // Get the result of image
        image_url = i.getStringExtra("image_url");

        // Locate the TextViews in singleitemview.xml
        TextView txtfirstname = (TextView) findViewById(R.id.first_name);
        TextView txtlastname = (TextView) findViewById(R.id.last_name);
        //TextView txtfppg = (TextView) findViewById(R.id.fppg);

        // Locate the ImageView in singleitemview.xml
        ImageView imgplayer = (ImageView) findViewById(R.id.person);

        // Set results to the TextViews
        txtfirstname.setText(first_name);
        txtlastname.setText(last_name);
        //txtfppg.setText(fppg);

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(image_url, imgplayer);
    }
}