package com.example.nasaimageoftheday;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Inflate toolbar_menu into Toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * On clicking help button - overridden in each activity for custom message
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }

    /**
     * On clicking NavigationDrawer items
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        String toolbarTitle = (String)getSupportActionBar().getTitle();

        switch(item.getItemId()) {
            case R.id.navMain:
                if (!toolbarTitle.contains("Main")) {
                    Intent mainPage = new Intent(this, MainActivity.class);
                    startActivity(mainPage);
                }
                break;
            case R.id.navHome:
                if (!toolbarTitle.contains("Home")) {
                    Intent homePage = new Intent(this, HomeActivity.class);
                    startActivity(homePage);
                }
                break;
            case R.id.navSearch:
                if (!toolbarTitle.contains("Search")) {
                    Intent searchPage = new Intent(this, SearchActivity.class);
                    startActivity(searchPage);
                }
                break;
            case R.id.navFavourites:
                if (!toolbarTitle.contains("Favourites")) {
                    Intent favouritesPage = new Intent(this, FavouritesActivity.class);
                    startActivity(favouritesPage);
                }
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }


}