package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends BaseActivity {

    /**
     * Global variables
     */
    NASAItem nasaItem;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Open database
        DBOpener dbOpener = new DBOpener(this);
        db = dbOpener.getWritableDatabase();

        /**
         * Initialize NasaAPI AsyncTask with date chosen
         */
        // Receive and transform date into URL
        Intent dataReceived = getIntent();
        String date = dataReceived.getStringExtra("date");
        String apiURL = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + date;

        // Start AsyncTask with formatted URL
        NasaAPI nasaAPI = new NasaAPI();
        nasaAPI.execute(apiURL);

        /**
         * Set toolbar and NavigationDrawer
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.resultToolbar);

        // Drawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Navigation View
        NavigationView navigation = findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

        /**
         * Save NASAItem to database on clicking Save button
         */
        Button saveFavouriteButton = findViewById(R.id.saveFavouriteButton);
        saveFavouriteButton.setOnClickListener( click -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(DBOpener.COL_DATE, nasaItem.getDate());
            newRowValues.put(DBOpener.COL_TITLE, nasaItem.getTitle());
            newRowValues.put(DBOpener.COL_EXPLANATION, nasaItem.getExplanation());
            newRowValues.put(DBOpener.COL_IMAGE_URL, nasaItem.getImageURL().toString());

            // Change button and make toast
            saveFavouriteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.nasa_red));
            saveFavouriteButton.setText("Saved");
            Toast.makeText(this, "Saved to Favourites", Toast.LENGTH_SHORT).show();
            try {
                db.insertOrThrow(DBOpener.TABLE_NAME, null, newRowValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Alert Dialog on clicking help button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        alertDialogBuilder.setTitle(R.string.help)
                .setMessage(R.string.resultHelp)
                .setPositiveButton(R.string.dismiss, (click, arg) -> { });
        alertDialogBuilder.create().show();
        return true;
    }

    /**
     * Queries NASA API in the background
     */
    private class NasaAPI extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {

            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                // Create JSON object from result
                JSONObject nasaJSON = new JSONObject(result);

                // Cast attributes into a new NASAItem object
                String date = nasaJSON.getString("date");
                String title = nasaJSON.getString("title");
                String explanation = nasaJSON.getString("explanation");
                URL imageURL = new URL(nasaJSON.getString("hdurl"));
                nasaItem = new NASAItem(date, title, explanation, imageURL);
                Log.i("doInBackground", nasaItem.getTitle() + nasaItem.getExplanation() + nasaItem.getImageURL());

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fill progress bar with for loop
            for (int i = 0; i < 100; i+=3) {
                try {
                    publishProgress(i);
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setProgress(values[0]);

        }

        /**
         * Update TextViews with nasaItem attributes, dismiss progress bar
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView nasaItemDate = findViewById(R.id.nasaItemDate);
            TextView nasaItemTitle = findViewById(R.id.nasaItemTitle);
            TextView nasaItemExplanation = findViewById(R.id.nasaItemExplanation);
            TextView nasaItemImageURL = findViewById(R.id.nasaItemImageURL);
            nasaItemDate.setText(nasaItem.getDate());
            nasaItemTitle.setText(nasaItem.getTitle());
            nasaItemExplanation.setText(nasaItem.getExplanation());
            nasaItemImageURL.setText((nasaItem.getImageURL().toString()));

            // Update imageRedirectButton with imageURL
            Button imageRedirectButton = findViewById(R.id.imageRedirectButton);
            imageRedirectButton.setOnClickListener( click -> {
                try {
                    Intent browserRedirect = new Intent(Intent.ACTION_VIEW, Uri.parse(nasaItem.getImageURL().toString()));
                    startActivity(browserRedirect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}