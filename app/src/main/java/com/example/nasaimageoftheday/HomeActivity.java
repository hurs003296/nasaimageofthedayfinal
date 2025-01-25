package com.example.nasaimageoftheday;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**
         * Set toolbar and NavigationDrawer
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.homeToolbar);

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
         * TextView declarations
         */
        TextView nameWelcome = findViewById(R.id.nameWelcome);
        TextView bodyWelcome = findViewById(R.id.bodyWelcome);

        /**
         * Get previous intent and set TextViews with name and body values
         */
        Intent dataReceived = getIntent();
        String nameReceived = dataReceived.getStringExtra("name");
        String bodyReceived = dataReceived.getStringExtra("body");

        String welcomeName = getString(R.string.welcome);
        String welcomeNameFormatted = String.format(welcomeName, nameReceived);
        String welcomeBody = getString(R.string.bodyWelcome);
        String welcomeBodyFormatted = String.format(welcomeBody, bodyReceived);
        nameWelcome.setText(welcomeNameFormatted);
        bodyWelcome.setText(welcomeBodyFormatted);

        /**
         * When this page is loaded without clicking on titleButton, load SharedPreferences into TextViews
         */
        if (nameReceived == null) {
            setTextView("Name", nameWelcome);
        }
        if (bodyReceived == null) {
            setTextView("Body", bodyWelcome);
        }

        /**
         * Button delcarations
         */
        Button dateButton = findViewById(R.id.dateButton);
        Button favouritesButton = findViewById(R.id.favouritesButton);

        dateButton.setOnClickListener( click -> {
            Intent searchPage = new Intent(this, SearchActivity.class);
            startActivity(searchPage);
        });

        favouritesButton.setOnClickListener( click -> {
            Intent favouritesPage = new Intent(this, FavouritesActivity.class);
            startActivity(favouritesPage);
        });
    }

    /**
     * Grab preference from SharedPreferences if it exists
     */
    private void setTextView(String preference, TextView textView) {

        SharedPreferences preferences = getSharedPreferences(preference, Context.MODE_PRIVATE);
        String savedString = preferences.getString(preference, "Preference not found");
        if (!(savedString.contains("Preference not found"))) {
            if (preference.equals("Name")) {
                String string = String.format(getString(R.string.welcome), savedString);
                textView.setText(string);
            } else if (preference.equals("Body")) {
                String string = String.format(getString(R.string.bodyWelcome), savedString);
                textView.setText(string);
            }
        } else {
            textView.setText("");
        }
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
                .setMessage(R.string.homeHelp)
                .setPositiveButton(R.string.dismiss, (click, arg) -> { });
        alertDialogBuilder.create().show();

        return true;
    }
}