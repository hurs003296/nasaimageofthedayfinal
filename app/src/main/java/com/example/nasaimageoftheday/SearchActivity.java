package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SearchActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /**
         * Set toolbar and NavigationDrawer
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.searchToolbar);

        // Drawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Navigation View
        NavigationView navigation = findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);

        // Open DatePicker with datePickerButton
        Button button = findViewById(R.id.datePickerButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date picker");
            }
        });
    }

    /**
     * When date is chosen in the DatePicker, format the date and pass it to ResultActivity
     * @param datePicker
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        String chosenDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        Intent resultPage = new Intent(this, ResultActivity.class)
                .putExtra("date", chosenDate);
        startActivity(resultPage);
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
                .setMessage(R.string.searchHelp)
                .setPositiveButton(R.string.dismiss, (click, arg) -> { });
        alertDialogBuilder.create().show();

        return true;
    }
}