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

package com.adeel.nordbeats.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeel.nordbeats.music.R;
import com.adeel.nordbeats.music.model.SongsListing;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongsListingAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<SongsListing> songsListings;

    /**
     * @param context Context
     * @param songsListings List
     */
    public SongsListingAdapter(Context context, List<SongsListing> songsListings) {
        this.mContext = context;
        this.songsListings = songsListings;
    }

    /**
     * @return int
     */
    @Override
    public int getCount() {
      return songsListings.size();
    }

    /**
     * @param position int
     * @return long
     */
    @Override
    public long getItemId(int position) {
      return 0;
    }

    /**
     * @param position int
     * @return object
     */
    @Override
    public Object getItem(int position) {
      return this;
    }

    /**
     * Append all the items with in the listview to render it on the screen
     * @param position int
     * @param convertView View
     * @param parent ViewGroup
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SongsListing songsListing = songsListings.get(position);

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.songs_list_item, null);

            final ImageView songsListingsPicture = (ImageView)convertView.findViewById(R.id.track_image);
            final TextView songsListingsId = (TextView)convertView.findViewById(R.id.track_id);
            final TextView songsListingsTitle = (TextView)convertView.findViewById(R.id.track_title);
            final TextView songsListingsCount = (TextView)convertView.findViewById(R.id.track_count);
            final TextView songsListingsClubId = (TextView)convertView.findViewById(R.id.track_club_id);

            final ViewHolder viewHolder = new ViewHolder(songsListingsId, songsListingsTitle, songsListingsPicture, songsListingsCount, songsListingsClubId);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();

        viewHolder.songsListingsId.setText(songsListing.getId());
        viewHolder.songsListingsTitle.setText(songsListing.getTitle());
        viewHolder.songsListingsCount.setText(songsListing.getVoteCount());
        viewHolder.songsListingsClubId.setText(songsListing.getClubId());

        Picasso.with(mContext).load(songsListing.getPicture()).into(viewHolder.songsListingsPicture);

        return convertView;
    }

    private class ViewHolder {
        private final TextView songsListingsId;
        private final TextView songsListingsTitle;
        private final TextView songsListingsCount;
        private final TextView songsListingsClubId;
        private final ImageView songsListingsPicture;

        /**
         * @param songsListingsId int
         * @param songsListingsTitle String
         * @param songsListingsPicture String
         * @param songsListingsCount int
         * @param songsListingsClubId int
         */
        public ViewHolder(TextView songsListingsId, TextView songsListingsTitle, ImageView songsListingsPicture, TextView songsListingsCount, TextView songsListingsClubId) {
            this.songsListingsId = songsListingsId;
            this.songsListingsTitle = songsListingsTitle;
            this.songsListingsPicture = songsListingsPicture;
            this.songsListingsCount = songsListingsCount;
            this.songsListingsClubId = songsListingsClubId;
        }
    }
}