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

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.adeel.nordbeats.music.adapter.ClubsAdapter;
import com.adeel.nordbeats.music.R;
import com.adeel.nordbeats.music.model.Club;

import java.util.ArrayList;
import java.util.List;

public class ClubsActivity extends BaseActivity {

  /** {@link String} */
  private static final String favoritedClubNamesKey = "favoritedClubNamesKey";

  /**
   *
   * @param savedInstanceState {@link Bundle}
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_clubs);
    initializeToolbar();

    GridView gridView = (GridView)findViewById(R.id.gridview);
    final ClubsAdapter clubsAdapter = new ClubsAdapter(this, clubs);
    gridView.setAdapter(clubsAdapter);

    /* GridView item click listener */
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      /**
       * @param parent {@link AdapterView}
       * @param view {@link View}
       * @param position int
       * @param id long
       */
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Club club = clubs[position];
        club.toggleFavorite();
        clubsAdapter.notifyDataSetChanged();
      }
    });
  }

  /**
   * @param outState {@link Bundle}
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    final List<Integer> favoritedClubNames = new ArrayList<>();
    for (Club club : clubs) {
      if (club.getIsFavorite()) {
        favoritedClubNames.add(club.getName());
      }
    }

    outState.putIntegerArrayList(favoritedClubNamesKey, (ArrayList)favoritedClubNames);
  }

  /**
   * @param savedInstanceState {@link Bundle}
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    final List<Integer> favoritedClubNames =
      savedInstanceState.getIntegerArrayList(favoritedClubNamesKey);

    for (int clubName : favoritedClubNames) {
      for (Club club : clubs) {
        if (club.getName() == clubName) {
          club.setIsFavorite(true);
          break;
        }
      }
    }
  }

  /** {@link com.adeel.nordbeats.music.model.Club} */
  private Club[] clubs = {
      new Club(R.string.abc_an_amazing_alphabet_book, R.string.dr_seuss, R.drawable.abc,
        "http://e-cdn-images.deezer.com/images/cover/924e939604bd9ec081c576f1df165361/200x200-000000-80-0-0.jpg"),
      new Club(R.string.are_you_my_mother, R.string.dr_seuss, R.drawable.areyoumymother,
        "https://i.pinimg.com/736x/e1/b1/af/e1b1af6c6fd8d6e4f409c43122a00eba--vinyl-collection-cd-cover.jpg"),
      new Club(R.string.where_is_babys_belly_button, R.string.karen_katz, R.drawable.whereisbabysbellybutton,
        "https://i.pinimg.com/originals/d9/57/c6/d957c6fa72bbb017061e7d6ea58da577.jpg"),
      new Club(R.string.on_the_night_you_were_born, R.string.nancy_tillman, R.drawable.onthenightyouwereborn,
        "https://i.pinimg.com/236x/5f/2a/c2/5f2ac20f3c325d5cef8c1b93e42a6869--cd-cover-album-cover.jpg"),
      new Club(R.string.hand_hand_fingers_thumb, R.string.dr_seuss, R.drawable.handhandfingersthumb,
        "https://i.pinimg.com/236x/5b/b6/26/5bb626ea7d1c786cbd5da3cbb9ae615c.jpg"),
      new Club(R.string.the_very_hungry_caterpillar, R.string.eric_carle, R.drawable.theveryhungrycaterpillar,
        "https://i.pinimg.com/236x/7a/1b/3e/7a1b3e123736731460d13494d356176f--bethel-music-brave-new-world.jpg"),
      new Club(R.string.the_going_to_bed_book, R.string.sandra_boynton, R.drawable.thegoingtobedbook,
        "https://i.pinimg.com/736x/82/77/3b/82773b20617dda43456ccfee9ee82aca--best-album-covers-book-covers.jpg"),
      new Club(R.string.oh_baby_go_baby, R.string.dr_seuss, R.drawable.ohbabygobaby,
        "https://i.pinimg.com/736x/4c/c8/c0/4cc8c0a061fb4e7e8f3d901da2f9862b--laser-wip.jpg"),
      new Club(R.string.the_tooth_book, R.string.dr_seuss, R.drawable.thetoothbook,
        "http://e-cdn-images.deezer.com/images/cover/7a31bfc380e9131d66dfa6a01cfe4518/200x200-000000-80-0-0.jpg"),
      new Club(R.string.one_fish_two_fish_red_fish_blue_fish, R.string.dr_seuss, R.drawable.onefish,
        "http://zamabeats.com/wp-content/uploads/2016/12/cropped-cropped-Zama-Beats-Music-Logo.png"),
      new Club(R.string.the_going_to_bed_book, R.string.sandra_boynton, R.drawable.thegoingtobedbook,
              "https://i.pinimg.com/736x/82/77/3b/82773b20617dda43456ccfee9ee82aca--best-album-covers-book-covers.jpg"),
      new Club(R.string.oh_baby_go_baby, R.string.dr_seuss, R.drawable.ohbabygobaby,
              "https://i.pinimg.com/736x/4c/c8/c0/4cc8c0a061fb4e7e8f3d901da2f9862b--laser-wip.jpg"),
      new Club(R.string.on_the_night_you_were_born, R.string.nancy_tillman, R.drawable.onthenightyouwereborn,
              "https://i.pinimg.com/236x/5f/2a/c2/5f2ac20f3c325d5cef8c1b93e42a6869--cd-cover-album-cover.jpg"),
  };
}