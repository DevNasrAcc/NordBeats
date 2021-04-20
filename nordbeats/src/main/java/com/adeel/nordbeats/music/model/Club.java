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

public class Club {
  private final int name;
  private final int author;
  private final int imageResource;
  private boolean isFavorite = false;
  private final String imageUrl;

  /**
   * @param name int
   * @param author int
   * @param imageResource int
   * @param imageUrl String
   */
  public Club(int name, int author, int imageResource, String imageUrl) {
    this.name = name;
    this.author = author;
    this.imageResource = imageResource;
    this.imageUrl = imageUrl;
  }

  /**
   * @return int
   */
  public int getName() {
    return name;
  }

  /**
   * @return int
   */
  public int getAuthor() {
    return author;
  }

  /**
   * @return int
   */
  public int getImageResource() {
    return imageResource;
  }

  /**
   * @return boolean
   */
  public boolean getIsFavorite() {
    return isFavorite;
  }

  /**
   * @param isFavorite boolean
   */
  public void setIsFavorite(boolean isFavorite) {
    this.isFavorite = isFavorite;
  }

  public void toggleFavorite() {
    isFavorite = !isFavorite;
  }

  /**
   * @return String
   */
  public String getImageUrl() {
    return imageUrl;
  }
}
