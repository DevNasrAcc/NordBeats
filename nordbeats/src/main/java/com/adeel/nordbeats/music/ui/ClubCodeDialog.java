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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adeel.nordbeats.music.R;

public class ClubCodeDialog extends DialogFragment {
    EditText entryCode;
    Button accessCode, cancelCode;

    static String dialogBoxTitle;

    public ClubCodeDialog() {

    }

    /**
     * Set the title of a dialog box
     * @param title String
     */
    public void setDialogTitle(String title) {
        dialogBoxTitle = title;
    }

    /**
     * Render dialog box and perform actions on button click
     * @param inflater
     * @param container
     * @param saveInstanceState
     * @return View
     */
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState) {
        View view = inflater.inflate(
                R.layout.dialog_club_songs_login, container);

        entryCode = (EditText) view.findViewById(R.id.entry_code);
        accessCode = (Button) view.findViewById(R.id.access_code);
        cancelCode = (Button) view.findViewById(R.id.cancel_code);

        accessCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();

                if (entryCode.getText().toString().equals("nordbeats")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("club_id", ((LiveClubsActivity) getContext()).getStringCache("currentClub","club"));

                    Intent bundleIntent = new Intent(getActivity(), ClubSongsActivity.class);
                    bundleIntent.putExtras(bundle);
                    startActivity(bundleIntent);
                }
                else {
                    Toast.makeText(getActivity(), "Please enter the correct code!", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().setCanceledOnTouchOutside(true);

        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        getDialog().setTitle(dialogBoxTitle);
        return view;
    }
}