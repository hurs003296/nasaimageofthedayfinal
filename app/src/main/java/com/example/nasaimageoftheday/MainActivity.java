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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends BaseActivity {

    /**
     * Global SharedPreferences
     */
    SharedPreferences namePreferences = null;
    SharedPreferences bodyPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Set toolbar and NavigationDrawer
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.mainToolbar);

        // Drawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Navigation View
        NavigationView navigation = findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);


        // Sets edit values to saved SharedPreferences values if they exist
        setEditTexts();

        EditText nameEdit = findViewById(R.id.nameInputEdit);
        EditText bodyEdit = findViewById(R.id.bodyInputEdit);
        Button titleButton = findViewById(R.id.titleButton);

        // Start HomeActivity with name and body values
        titleButton.setOnClickListener( click -> {
            String nameValue = nameEdit.getText().toString();
            String bodyValue = bodyEdit.getText().toString();
            Log.i("onclick", nameValue);
            Log.i("onclick", bodyValue);
            saveSharedPrefs(nameValue, bodyValue);
            Intent homePage = new Intent(this, HomeActivity.class)
                    .putExtra("name", nameValue)
                    .putExtra("body", bodyValue);
            startActivity(homePage);
        });
    }

    /**
     * Grab name and body from SharedPreferences if they exist
     */
    private void setEditTexts() {

        EditText nameEdit = findViewById(R.id.nameInputEdit);
        EditText bodyEdit = findViewById(R.id.bodyInputEdit);

        namePreferences = getSharedPreferences("Name", Context.MODE_PRIVATE);
        bodyPreferences = getSharedPreferences("Body", Context.MODE_PRIVATE);
        String savedNameString = namePreferences.getString("Name", "Name not found");
        String savedBodyString = bodyPreferences.getString("Body", "Body not found");
        if (!(savedNameString.contains("Name not found"))) {
            nameEdit.setText(savedNameString);
        }
        if (!(savedBodyString.contains("Body not found"))) {
            bodyEdit.setText(savedBodyString);
        }
    }

    /**
     * Save EditText inputs to SharedPreferences
     *
     * @param nameString
     * @param bodyString
     */
    private void saveSharedPrefs(String nameString, String bodyString) {
        SharedPreferences.Editor nameEditor = namePreferences.edit();
        SharedPreferences.Editor bodyEditor = bodyPreferences.edit();
        nameEditor.putString("Name", nameString);
        bodyEditor.putString("Body", bodyString);
        nameEditor.commit();
        bodyEditor.commit();
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
                .setMessage(R.string.mainHelp)
                .setPositiveButton(R.string.dismiss, (click, arg) -> { });
        alertDialogBuilder.create().show();

        return true;
    }
}