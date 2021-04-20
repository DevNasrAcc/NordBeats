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
import com.adeel.nordbeats.music.model.LiveClub;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LiveClubsAdapter extends BaseAdapter {

  private final Context mContext;
  private final List<LiveClub> liveClubs;

  /**
   * @param context Context
   * @param liveClubs List
   */
  public LiveClubsAdapter(Context context, List<LiveClub> liveClubs) {
    this.mContext = context;
    this.liveClubs = liveClubs;
  }

  /**
   * Get count of the list
   * @return int
   */
  @Override
  public int getCount() {
    return liveClubs.size();
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
   * @return null|array
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
    final LiveClub liveClub = liveClubs.get(position);

    /* view holder pattern */
    if (convertView == null) {
      final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
      convertView = layoutInflater.inflate(R.layout.fragment_live_club, null);

      final TextView itemId = (TextView)convertView.findViewById(R.id.item_id);
      final TextView itemTitle = (TextView)convertView.findViewById(R.id.item_title);
      final TextView itemAddress = (TextView)convertView.findViewById(R.id.item_address);
      final TextView itemTiming = (TextView)convertView.findViewById(R.id.item_timing);
      final ImageView itemPicture = (ImageView)convertView.findViewById(R.id.item_picture);
      final ImageView itemLogo = (ImageView)convertView.findViewById(R.id.item_logo);

      final ViewHolder viewHolder = new ViewHolder(itemId, itemTitle, itemAddress, itemTiming, itemPicture, itemLogo);
      convertView.setTag(viewHolder);
    }

    final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
    viewHolder.itemId.setText(liveClub.getId());
    viewHolder.itemTitle.setText(liveClub.getTitle());
    viewHolder.itemAddress.setText(liveClub.getAddress());
    viewHolder.itemTiming.setText(liveClub.getTiming());

    Picasso.with(mContext).load(liveClub.getPicture()).into(viewHolder.itemPicture);
    Picasso.with(mContext).load(liveClub.getLogo()).into(viewHolder.itemLogo);

    return convertView;
  }

  private class ViewHolder {
    private final TextView itemId;
    private final TextView itemTitle;
    private final TextView itemAddress;
    private final TextView itemTiming;
    private final ImageView itemPicture;
    private final ImageView itemLogo;

    /**
     * @param itemId int
     * @param itemTitle String
     * @param itemAddress String
     * @param itemTiming String
     * @param itemPicture
     * @param itemLogo
     */
    public ViewHolder(TextView itemId, TextView itemTitle, TextView itemAddress, TextView itemTiming, ImageView itemPicture, ImageView itemLogo) {
      this.itemId = itemId;
      this.itemTitle = itemTitle;
      this.itemAddress = itemAddress;
      this.itemTiming = itemTiming;
      this.itemPicture = itemPicture;
      this.itemLogo = itemLogo;
    }
  }

}
