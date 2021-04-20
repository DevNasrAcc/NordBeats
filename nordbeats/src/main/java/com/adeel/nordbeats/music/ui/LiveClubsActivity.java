/*
 * Copyright (C) 2018 NordBeats Music Application By Muhammad Adeel
 *
 *******************************************************************************
 * AUTHORS:
 *******************************************************************************
 * Muhammad Adeel       <muhammad_adeel91@yahoo.com>
 * Syed Muhammad Razi
 * Danyal Akhtar
 *
 * Permission is only granted for the university and feel free to distribute
 * the copy of the original version to others with the limitation of not
 * selling or copying the code without adding their own copyright on the top
 * of the original copyright content. It also contains the code of "The Android
 * Open Source Project" under the Apache License, you can find the Apache
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * The Software is provided "as is", without warranty of any kind, express or
 * implied, including but not limited to the warranties of merchantability,
 * fitness for a particular purpose and noninfringement. In no event shall the
 * authors or copyright holders b liable for any claim, damages or other
 * liability, whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other dealings in
 * the software.
 */

package com.adeel.nordbeats.music.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.adeel.nordbeats.music.R;
import com.adeel.nordbeats.music.adapter.LiveClubsAdapter;
import com.adeel.nordbeats.music.model.LiveClub;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class LiveClubsActivity extends BaseActivity {

    ProgressDialog progress;
    private List<LiveClub> liveClubs = new ArrayList<>();
    GridView liveClubsGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_clubs);
        initializeToolbar();

        getClubsList();
    }

    /**
     * Fetch club list from the server
     */
    private void getClubsList() {
        @SuppressLint("StaticFieldLeak")
        class ClubsList extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progress = new ProgressDialog(LiveClubsActivity.this);
                progress.setTitle("NordBeats");
                progress.setMessage("Please wait...");
                progress.show();
            }

            @Override
            protected void onPostExecute(String response) {
                liveClubsGridView = (GridView) findViewById(R.id.liveClubsGridView);
                final LiveClubsAdapter liveClubsAdapter = new LiveClubsAdapter(getApplicationContext(), liveClubs);
                liveClubsGridView.setAdapter(liveClubsAdapter);

                liveClubsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedClubId = liveClubs.get(position).getId();

                        setStringCache("currentClub", "club", selectedClubId);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        ClubCodeDialog inputNameDialog = new ClubCodeDialog();
                        inputNameDialog.setCancelable(false);
                        inputNameDialog.setDialogTitle("Enter Code");
                        inputNameDialog.show(fragmentManager, "Club Code Dialog");
                    }
                });

                progress.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                String text = "";
                BufferedReader reader = null;
                try {
                    // Defined URL  where to send data
                    URL url = new URL("http://nordbeats.000webhostapp.com/clubs.php");
                    // Send POST data request
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);

                    // Get the server response
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        JSONArray songList = new JSONArray(line);

                        for(int index = 0; index < songList.length(); index++) {
                            JSONObject jsonobject = (JSONObject) songList.get(index);
                            String id = jsonobject.optString("id");
                            String title = jsonobject.optString("name");
                            String address = jsonobject.optString("address");
                            String timing = jsonobject.optString("timing");
                            String picture = jsonobject.optString("picture");
                            String logo = jsonobject.optString("logo");

                            LiveClub item = new LiveClub(id, title, address, timing, picture, logo);
                            liveClubs.add(item);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                // Show response on activity
                return String.valueOf(true);
            }
        }

        ClubsList votingClubSong = new ClubsList();
        votingClubSong.execute();
    }

    /**
     * @param sharedpreferencename {@link String}
     * @param sharedpreferenceitemtext {@link String}
     * @param sharedpreferenceitemdata {@link String}
     */
    public void setStringCache(String sharedpreferencename, String sharedpreferenceitemtext, String sharedpreferenceitemdata) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedpreferenceitemtext, sharedpreferenceitemdata).apply();
    }

    /**
     * @param sharedpreferencename {@link String}
     * @param sharedpreferenceitemtext {@link String}
     * @return
     */
    public String getStringCache(String sharedpreferencename, String sharedpreferenceitemtext) {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedpreferencename, Context.MODE_PRIVATE);
        return sharedPreferences.getString(sharedpreferenceitemtext, "clear");
    }
}