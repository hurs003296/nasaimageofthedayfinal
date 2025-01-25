package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class FavouritesActivity extends BaseActivity {

    /**
     * Globals
     */
    private ArrayList<NASAItem> itemArray = new ArrayList<>();
    private ArrayList<String> titleArray = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        /**
         * Set toolbar and NavigationDrawer
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.favouritesToolbar);

        // Drawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Navigation View
        NavigationView navigation = findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

        loadDBData();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBOpener.TABLE_NAME + ";", null);
        printCursor(cursor);

        /**
         * Set adapter on ListView
         */
        ListView favouritesListView = findViewById(R.id.favouritesListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titleArray);
        favouritesListView.setAdapter(adapter);

        // On item click, send item details to EmptyActivity
        favouritesListView.setOnItemClickListener((list, item, position, id) -> {
            Bundle itemData = new Bundle();
            NASAItem nasaItem = itemArray.get(position);
            itemData.putLong(DBOpener.COL_ID, nasaItem.getId());
            itemData.putString(DBOpener.COL_DATE, nasaItem.getDate());
            itemData.putString(DBOpener.COL_TITLE, nasaItem.getTitle());
            itemData.putString(DBOpener.COL_EXPLANATION, nasaItem.getExplanation());
            itemData.putString(DBOpener.COL_IMAGE_URL, nasaItem.getImageURL().toString());

            Intent newActivity = new Intent(this, EmptyActivity.class);
            newActivity.putExtras(itemData);
            startActivity(newActivity);
        });

        // Refresh adapter on refreshButton click
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener( click -> {
            adapter.notifyDataSetChanged();
        });
    }

    public void loadDBData() {
        DBOpener dbOpener = new DBOpener(this);
        db = dbOpener.getWritableDatabase();
        String[] columns = {DBOpener.COL_ID, DBOpener.COL_DATE, DBOpener.COL_TITLE, DBOpener.COL_EXPLANATION, DBOpener.COL_IMAGE_URL};

        Cursor results = db.query(false, DBOpener.TABLE_NAME, columns,
                null, null, null, null, null, null);

        // Column indexes
        int idColIndex = results.getColumnIndex(DBOpener.COL_ID);
        int dateColIndex = results.getColumnIndex(DBOpener.COL_DATE);
        int titleColIndex = results.getColumnIndex(DBOpener.COL_TITLE);
        int explanationColIndex = results.getColumnIndex(DBOpener.COL_EXPLANATION);
        int imageURLColIndex = results.getColumnIndex(DBOpener.COL_IMAGE_URL);

        // Grab data from columns, add resulting NASAItem to dateArray
        while (results.moveToNext()) {
            try {
                Long id = results.getLong(idColIndex);
                String date = results.getString(dateColIndex);
                String title = results.getString(titleColIndex);
                String explanation = results.getString(explanationColIndex);
                URL imageURL = new URL(results.getString(imageURLColIndex));

                itemArray.add(new NASAItem(id, date, title, explanation, imageURL));
                titleArray.add(date + " | " + title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Print console to database for bug-checking
     * @param cursor
     */
    public void printCursor(Cursor cursor) {
        String[] columns = cursor.getColumnNames();

        int idColIndex = cursor.getColumnIndex(DBOpener.COL_ID);
        int dateColIndex = cursor.getColumnIndex(DBOpener.COL_DATE);
        int titleColIndex = cursor.getColumnIndex(DBOpener.COL_TITLE);
        int explanationColIndex = cursor.getColumnIndex(DBOpener.COL_EXPLANATION);
        int imageURLColIndex = cursor.getColumnIndex(DBOpener.COL_IMAGE_URL);

        System.out.println("# of columns: " + columns.length);
        System.out.println("Columns: " + Arrays.toString(columns));
        System.out.println("# of rows: " + cursor.getCount());
        while (cursor.moveToNext()) {
            System.out.println("ID: " + cursor.getLong(idColIndex));
            System.out.println("Date: " + cursor.getString(dateColIndex));
            System.out.println("Title: " + cursor.getString(titleColIndex));
            System.out.println("Explanation: " + cursor.getString(explanationColIndex));
            System.out.println("ImageURL: " + cursor.getString(imageURLColIndex));
            System.out.println();
        }
    }

    /**
     * Notify adapter of dataset change on resume
     */
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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
                .setMessage(R.string.favouritesHelp)
                .setPositiveButton(R.string.dismiss, (click, arg) -> { });
        alertDialogBuilder.create().show();

        return true;
    }
}