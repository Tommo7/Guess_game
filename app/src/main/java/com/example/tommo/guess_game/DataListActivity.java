package com.example.tommo.guess_game;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DataListActivity extends AppCompatActivity {

    private String TAG = DataListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    private ListViewAdapter adapter;
    static String FIRST_NAME = "first_name";
    static String LAST_NAME = "last_name";
    static String FPPG = "fppg";
    static String IMAGE_URL = "image_url";

    private int j = 0;
    private boolean player1 = true;
    private boolean completed = false;
    private double points1;
    private double points2;
    private int winningPlayer = 0;
    private int score = 0;

    // URL to get contacts JSON
    //private static String url = "https://cdn.rawgit.com/liamjdouglas/bb40ee8721f1a9313c22c6ea0851a105/raw/6b6fc89d55ebe4d9b05c1469349af33651d7e7f1/Player.json";
    private static String url;
    ArrayList<HashMap<String, String>> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_list);

        playerList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetPlayers().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if((position == 0 && winningPlayer == 1) || (position == 1 && winningPlayer == 2))
                {
                    score++;
                    Toast.makeText(getApplicationContext(),
                            "Correct " + " Score: " + score + "\nFPPG1: " + points1 + "\nFPPG2: " + points2,
                            Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Incorrect" + "\nFPPG1: " + points1 + "\nFPPG2: " + points2,
                            Toast.LENGTH_LONG)
                            .show();
                }
                playerList.clear();
                adapter.notifyDataSetChanged();

                if(score >= 10)
                {
                    startActivity(new Intent(getApplicationContext(), EndGame.class));
                    finish();
                    System.exit(0);
                }

                j += 2;

                new GetPlayers().execute();
            }
        });
    }

    protected void GetData(){
        url = "https://cdn.rawgit.com/liamjdouglas/bb40ee8721f1a9313c22c6ea0851a105/raw/6b6fc89d55ebe4d9b05c1469349af33651d7e7f1/Player.json";
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray players = jsonObj.getJSONArray("players");

                // looping through Players
                for (int i = j; i < (j + 2); i++) {
                    if(i >= 59)
                    {
                        j = -1;
                        i = 0;
                    }

                    JSONObject c = players.getJSONObject(i);
                    String points = c.getString("fppg");
                    if(player1)
                    {
                        points1 = Double.parseDouble(points);
                    }
                    else
                    {
                        points2 = Double.parseDouble(points);
                        completed = true;
                    }

                    JSONObject images = c.getJSONObject("images");
                    JSONObject def = images.getJSONObject("default");

                    // tmp hash map for single player
                    HashMap<String, String> player = new HashMap<>();

                    // adding each child node to HashMap key => value
                    player.put("first_name", c.getString("first_name"));
                    player.put("last_name", c.getString("last_name"));
                    player.put("fppg", c.getString("fppg"));
                    player.put("image_url", def.getString("url"));

                    // adding player to player list
                    playerList.add(player);

                    player1 = !player1;

                    if(completed)
                    {
                        if(points1 > points2)
                        {
                            winningPlayer = 1;
                        }
                        else
                        {
                            winningPlayer = 2;
                        }

                        completed = false;
                    }
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetPlayers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(DataListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            GetData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            adapter = new ListViewAdapter(DataListActivity.this, playerList);

            lv.setAdapter(adapter);
        }

    }
}