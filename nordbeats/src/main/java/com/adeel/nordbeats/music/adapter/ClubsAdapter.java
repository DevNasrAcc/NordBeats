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
import com.adeel.nordbeats.music.model.Club;
import com.squareup.picasso.Picasso;

public class ClubsAdapter extends BaseAdapter {

  private final Context mContext;
  private final Club[] clubs;

  /**
   * @param context Context
   * @param clubs
   */
  public ClubsAdapter(Context context, Club[] clubs) {
    this.mContext = context;
    this.clubs = clubs;
  }

  /**
   * Get count of the list
   * @return int
   */
  @Override
  public int getCount() {
    return clubs.length;
  }

  /**
   * Retrieve item/club id
   * @param position int
   * @return long
   */
  @Override
  public long getItemId(int position) {
    return 0;
  }

  /**
   * Get item
   * @param position int
   * @return null
   */
  @Override
  public Object getItem(int position) {
    return null;
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
    final Club club = clubs[position];

    /* View holder pattern */
    if (convertView == null) {
      final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
      convertView = layoutInflater.inflate(R.layout.fragment_club, null);

      final ImageView imageViewCoverArt = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
      final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
      final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_book_author);
      final ImageView imageViewFavorite = (ImageView)convertView.findViewById(R.id.imageview_favorite);

      final ViewHolder viewHolder = new ViewHolder(nameTextView, authorTextView, imageViewCoverArt, imageViewFavorite);
      convertView.setTag(viewHolder);
    }

    final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
    viewHolder.nameTextView.setText(mContext.getString(club.getName()));
    viewHolder.authorTextView.setText(mContext.getString(club.getAuthor()));
    viewHolder.imageViewFavorite.setImageResource(club.getIsFavorite() ? R.drawable.star_enabled : R.drawable.star_disabled);

    Picasso.with(mContext).load(club.getImageUrl()).into(viewHolder.imageViewCoverArt);

    return convertView;
  }

  private class ViewHolder {
    private final TextView nameTextView;
    private final TextView authorTextView;
    private final ImageView imageViewCoverArt;
    private final ImageView imageViewFavorite;

    /**
     * @param nameTextView String
     * @param authorTextView String
     * @param imageViewCoverArt
     * @param imageViewFavorite
     */
    public ViewHolder(TextView nameTextView, TextView authorTextView, ImageView imageViewCoverArt, ImageView imageViewFavorite) {
      this.nameTextView = nameTextView;
      this.authorTextView = authorTextView;
      this.imageViewCoverArt = imageViewCoverArt;
      this.imageViewFavorite = imageViewFavorite;
    }
  }

}
