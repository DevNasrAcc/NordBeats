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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adeel.nordbeats.music.R;
import com.adeel.nordbeats.music.adapter.SongsListingAdapter;
import com.adeel.nordbeats.music.model.SongsListing;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClubSongsActivity extends BaseActivity {

    private Button selectFileButton;

    ProgressDialog progress, progressVote, progressList;

    String clubId, clubName, clubAddress, clubTiming;

    SwipeMenuListView swipeListView;

    Boolean upVote = false, downVote = false;

    int voteCount, defaultVoteCount = 1;

    private String votingSystem[][];

    private SongsListingAdapter songsListingAdapter;

    final private List<SongsListing> songsListings = new ArrayList<>();

    /**
     * Initializing the drawer toolbar.
     *
     * Retrieve passed params within extraBundle which has been passed from previous activity
     * and send the param club_id to the function in this activity to fetch the stored clubs
     * from the web server.
     *
     * Also checking the sdk permission and/or asking for permission of read external storage and
     * then calling the function to choose file for upload.
     *
     * After every 10 seconds, loading the swipemenulistview by calling the rest api
     * to fetch the latest result
     *
     * @param savedInstanceState {@link Bundle}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_songs);
        initializeToolbar();

        Intent extraIntent = getIntent();
        Bundle extraBundle = extraIntent.getExtras();

        if (!extraBundle.isEmpty()) {
            clubId = extraBundle.getString("club_id");
        }

        getSongsList(clubId);

        selectFileButton = (Button) findViewById(R.id.selectFile);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        enable_button();

        final Handler handler = new Handler();
        final int delay = 10000;

        handler.postDelayed(new Runnable(){
            public void run(){
                updateSwipeMenuList();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    /**
     * Try catch the exceptions on clearing the list of songs and notified data into the adapte
     */
    private void updateSwipeMenuList() {
        try {
            songsListings.clear();
            songsListingAdapter.notifyDataSetChanged();

            getSongsList(clubId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing the AsyncTask to prevent the issues and exceptions because network related
     * things throws exceptions on main thread and also prevent to app crashing
     *
     * Request web service to vote up/down the song
     *
     * @param vote {@link String}
     * @param voteCount int
     * @param id {@link String}
     */
    private void votingClubSong(final String vote, final int voteCount, final String id) {
        @SuppressLint("StaticFieldLeak")
        class VotingClubSong extends AsyncTask<String, Void, String> {
            /**
             * Display the progress dialog box onPreExecute
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressVote = new ProgressDialog(ClubSongsActivity.this);
                progressVote.setTitle(vote.substring(0, 1).toUpperCase() + vote.substring(1) + " Voting...");
                progressVote.setMessage("You have voted " + vote + " for this song...");
                progressVote.show();
            }

            /**
             * Dismiss progress dialog and calling update listview
             *
             * @param response {@link String}
             */
            @Override
            protected void onPostExecute(String response) {
                progressVote.dismiss();

                updateSwipeMenuList();
            }

            /**
             * Posting voting information to web server which will be stored
             *
             * @param params {@link String}
             * @return String
             */
            @Override
            protected String doInBackground(String... params) {
                String text = "";
                BufferedReader reader = null;
                try {
                    /* Create data variable for sent values to server */
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("club_id", "UTF-8") + "=" + URLEncoder.encode(clubId, "UTF-8");
                    data += "&" + URLEncoder.encode("vote", "UTF-8") + "=" + URLEncoder.encode(vote, "UTF-8");
                    data += "&" + URLEncoder.encode("vote_count", "UTF-8") + "=" + voteCount;

                    /* Defined URL  where to send data */
                    URL url = new URL("http://nordbeats.000webhostapp.com/voting.php");

                    /* Send POST data request */
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    /* Get the server response */
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    /* Read Server Response */
                    while ((line = reader.readLine()) != null) {
                        /*  Append server response in string */
                        sb.append(line + "\n");
                    }

                    text = sb.toString();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                return text;
            }
        }

        /* Executing the new networking thread */
        VotingClubSong votingClubSong = new VotingClubSong();
        votingClubSong.execute(vote);
    }

    /**
     * Open/ask permission to open the gallery to upload the song
     */
    private void enable_button() {
        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(ClubSongsActivity.this)
                        .withRequestCode(10)
                        .start();

            }
        });
    }

    /**
     * Request permission result
     * @param requestCode int
     * @param permissions {@link NonNull {@link String[]}}
     * @param grantResults {@link NonNull int}
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

    /**
     * On activity result new thread with {@link Runnable} target to upload mp3 songs on the server.
     * Post mp3 files only to the servr by using OKhttp
     *
     * @param requestCode int
     * @param resultCode int
     * @param data {@link Intent}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        /* if result is equal to 10 which is an operation success */
        if(requestCode == 10 && resultCode == RESULT_OK){

            progress = new ProgressDialog(ClubSongsActivity.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    if (null == content_type || !content_type.equals("audio/mpeg")) {
                        progress.dismiss();
                        return;
                    }

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    /* Get file name of the uploaded file and replace all whitespaces with _ */
                    String file_name = file_path.substring(file_path.lastIndexOf("/") + 1).replaceAll("\\s","_");

                    /** Request body in a {@link MultipartBody} of okhttp3 */
                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("club_id", clubId)
                            .addFormDataPart("file_name", "club_" + clubId + "_" + file_name)
                            .addFormDataPart("type", content_type)
                            .addFormDataPart("uploaded_file", "club_" + clubId + "_" + file_name, file_body)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://nordbeats.000webhostapp.com//upload.php")
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        progress.dismiss();

                    } catch (IOException e) {
                        progress.dismiss();
                        e.printStackTrace();
                    }
                }
            });

            t.start();
        }
    }

    /**
     * Get the file extension
     * @param path {@link String}
     * @return
     */
    private String getMimeType(String path) {
        path = path.replaceAll("[-+^!@#$%&*(:);_,]","");
        String extension = path.substring(path.length() - 3);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    /**
     * Get the list of all songs within the club which is in a new thread instead of main thread
     * just to prevent memory leakage and app crashes and improving the performance.
     *
     * @param clubId {@link String}
     */
    private void getSongsList(final String clubId) {
        @SuppressLint("StaticFieldLeak")
        class ClubSongsList extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            /**
             * Adding options with icon on swiping the listview items and also added the actions
             * on them such as vote up/down
             *
             * @param response {@link String}
             */
            @Override
            protected void onPostExecute(String response) {
                swipeListView = (SwipeMenuListView) findViewById(R.id.songsListView);

                songsListingAdapter = new SongsListingAdapter(getApplicationContext(), songsListings);
                swipeListView.setAdapter(songsListingAdapter);

                SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {
                        // create "open" item
                        SwipeMenuItem upVoteItem = new SwipeMenuItem(getApplicationContext());
                        // set item background
                        upVoteItem.setBackground(new ColorDrawable(Color.rgb(5, 144,80)));
                        // set item width
                        upVoteItem.setWidth(160);
                        // set a icon
                        upVoteItem.setIcon(R.drawable.ic_vote_up);
                        // add to menu
                        menu.addMenuItem(upVoteItem);

                        // create "delete" item
                        SwipeMenuItem downVoteItem = new SwipeMenuItem(getApplicationContext());
                        // set item background
                        downVoteItem.setBackground(new ColorDrawable(Color.rgb(228,32, 17)));
                        // set item width
                        downVoteItem.setWidth(160);
                        // set a icon
                        downVoteItem.setIcon(R.drawable.ic_vote_down);
                        // add to menu
                        menu.addMenuItem(downVoteItem);
                    }
                };

                swipeListView.setMenuCreator(creator);

                swipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    /**
                     * On item click request server to update the songs votes
                     *
                     * @param position int
                     * @param menu {@link SwipeMenu}
                     * @param index int
                     * @return
                     */
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        final TextView songsListingsId = (TextView) findViewById(R.id.track_id);

                        SongsListing songsListingsItem = songsListings.get(position);
                        String songListItemId = songsListingsItem.getId().toString();

                        switch (index) {
                            case 0:
                                voteCount = defaultVoteCount;

                                if (upVote) {
                                    Toast.makeText(getApplicationContext(), "You cannot vote up for the same song again!", Toast.LENGTH_LONG).show();
                                    return true;
                                }

                                votingClubSong("up", voteCount, songListItemId);
                                upVote = true;
                                downVote = false;

                                Log.d("adeel", "Menu item vote up " + index);
                                break;
                            case 1:
                                voteCount = defaultVoteCount;

                                if (downVote) {
                                    Toast.makeText(getApplicationContext(), "You cannot vote down for the same song again!", Toast.LENGTH_LONG).show();
                                    return true;
                                }

                                votingClubSong("down", voteCount, songListItemId);
                                downVote = true;
                                upVote = false;

                                Log.d("adeel", "Menu item vote down " + index);
                                break;
                        }

                        /* False : close the menu; True : not close the menu */
                        return false;
                    }
                });
            }

            /**
             * Retrieve club songs lists from the server by calling the web servic via {@link URL}
             * and {@link URLConnection}
             *
             * @param params {@link String}
             * @return String
             */
            @Override
            protected String doInBackground(String... params) {
                String text = "";
                BufferedReader reader = null;
                try {
                    /* Create data variable for sent values to server */
                    String data = URLEncoder.encode("club_id", "UTF-8") + "=" + URLEncoder.encode(clubId, "UTF-8");

                    /* Defined URL  where to send data */
                    URL url = new URL("http://nordbeats.000webhostapp.com/club_songs.php");

                    /*  Send POST data request */
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    /* Get the server response */
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    /* Read Server Response */
                    while ((line = reader.readLine()) != null) {
                        JSONArray songList = new JSONArray(line);

                        for(int index = 0; index < songList.length(); index++) {
                            JSONObject jsonobject = (JSONObject) songList.get(index);
                            String id = jsonobject.optString("id");
                            String title = jsonobject.optString("title");
                            String picture = jsonobject.optString("picture");
                            String voteCount = jsonobject.optString("vote_count");
                            String clubId = jsonobject.optString("club_id");

                            /* Adding items into the ArrayList */
                            SongsListing item = new SongsListing(id, title, picture, voteCount, clubId);
                            songsListings.add(item);
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

                /* Show response on activity */
                return String.valueOf(true);
            }
        }

        ClubSongsList clubSongsList = new ClubSongsList();
        clubSongsList.execute(clubId);
    }
}
