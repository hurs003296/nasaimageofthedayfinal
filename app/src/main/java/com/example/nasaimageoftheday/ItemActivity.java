package com.example.nasaimageoftheday;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.net.MalformedURLException;
import java.net.URL;

public class ItemFragment extends Fragment {

    SQLiteDatabase db;

    /**
     * Receive itemData and inflate fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Open database
        DBOpener dbOpener = new DBOpener(getActivity());
        db = dbOpener.getWritableDatabase();

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_item, container, false);

        // Get bundled data
        Bundle itemData = getArguments();
        long id = itemData.getLong(DBOpener.COL_ID);
        String date = itemData.getString(DBOpener.COL_DATE);
        String title = itemData.getString(DBOpener.COL_TITLE);
        String explanation = itemData.getString(DBOpener.COL_EXPLANATION);
        String imageURL = itemData.getString(DBOpener.COL_IMAGE_URL);

        // Set TextViews with data
        TextView nasaItemDate = result.findViewById(R.id.nasaItemDate_fav);
        TextView nasaItemTitle = result.findViewById(R.id.nasaItemTitle_fav);
        TextView nasaItemExplanation = result.findViewById(R.id.nasaItemExplanation_fav);
        TextView nasaItemImageURL = result.findViewById(R.id.nasaItemImageURL_fav);
        nasaItemDate.setText(date);
        nasaItemTitle.setText(title);
        nasaItemExplanation.setText(explanation);
        nasaItemImageURL.setText(imageURL.toString());

        // Image redirect button
        Button imageRedirectButton = result.findViewById(R.id.imageRedirectButton_fav);
        imageRedirectButton.setOnClickListener( click -> {
            try {
                Intent browserRedirect = new Intent(Intent.ACTION_VIEW, Uri.parse(imageURL));
                startActivity(browserRedirect);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // On click of removeFavouriteButton, remove this item from the database
        Button removeButton = result.findViewById(R.id.removeFavouriteButton);
        removeButton.setOnClickListener( click -> {

            // Snackbar confirms delete
            FrameLayout itemFragment = result.findViewById(R.id.itemFragment);
            Snackbar.make(itemFragment, R.string.itemRemoved, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.okay, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            db.delete(DBOpener.TABLE_NAME,DBOpener.COL_ID + " = ?",
                                    new String[] {Long.toString(id)});
                        }})
                    .show();
        });

        return result;
    }
}