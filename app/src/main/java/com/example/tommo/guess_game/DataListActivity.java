package com.example.tommo.guess_game;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    // URL to get contacts JSON
    private static String url = "https://cdn.rawgit.com/liamjdouglas/bb40ee8721f1a9313c22c6ea0851a105/raw/6b6fc89d55ebe4d9b05c1469349af33651d7e7f1/Player.json";

    ArrayList<HashMap<String, String>> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_list);

        playerList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetPlayers().execute();
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
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray players = jsonObj.getJSONArray("players");

                    // looping through All Players
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject c = players.getJSONObject(i);

                        String first_name = c.getString("first_name");
                        String last_name = c.getString("last_name");
                        String fppg = c.getString("fppg");
                        //String address = c.getString("address");
                        //String gender = c.getString("gender");

                        JSONObject images = c.getJSONObject("images");
                        JSONObject def = images.getJSONObject("default");
                        String image_url = def.getString("url");

                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject("phone");
                        //String mobile = phone.getString("mobile");
                        //String home = phone.getString("home");
                        //String office = phone.getString("office");

                        // tmp hash map for single player
                        HashMap<String, String> player = new HashMap<>();

                        // adding each child node to HashMap key => value
                        player.put("first_name", first_name);
                        player.put("last_name", last_name);
                        player.put("fppg", fppg);
                        //player.put("mobile", mobile);

                        // adding player to player list
                        playerList.add(player);
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
            ListAdapter adapter = new SimpleAdapter(
                    DataListActivity.this, playerList,
                    R.layout.list_item, new String[]{"first_name", "last_name",
                    "fppg"}, new int[]{R.id.first_name,
                    R.id.last_name, R.id.fppg});

            lv.setAdapter(adapter);
        }

    }
}
