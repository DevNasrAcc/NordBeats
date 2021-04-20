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

package com.adeel.nordbeats.music.model;

public class SongsListing {
    private final String id;
    private final String title;
    private final String picture;
    private final String voteCount;
    private final String clubId;

    /**
     * @param id String
     * @param title String
     * @param picture String
     * @param voteCount String
     * @param clubId String
     */
    public SongsListing(String id, String title, String picture, String voteCount, String clubId) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.voteCount = voteCount;
        this.clubId = clubId;
    }

    /**
     * @return String
     */
    public String getId() {
    return id;
    }

    /**
     * @return String
     */
    public String getTitle() {
    return title;
    }

    /**
     * @return String
     */
    public String getPicture() {
    return picture;
    }

    /**
     * @return String
     */
    public String getVoteCount() {
    return voteCount;
    }

    /**
     * @return String
     */
    public String getClubId() {
    return clubId;
  }
}
