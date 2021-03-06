package jason.xu.s991545529;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/*
    Jason Xu 991545529
    Section: Mondays 6:00pm-9:00pm
 */

public class JasonActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.jasonToolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getString(R.string.not_implemented), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.jasonDrawerLayout);
        NavigationView navigationView = findViewById(R.id.jasonNavView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.jasonNavHome, R.id.jasonNavDownload, R.id.jasonNavWebService)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.jasonNavHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        double longitude = 0;
        double latitude = 0;
        Geocoder geocoder;
        List<Address> user = null;

        // Tries to find latitude and longitude if the item in toolbar is clicked.
        if (id == R.id.jasonAppBarLocation) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, false);

                View contextView = (View) findViewById(R.id.jasonDrawerLayout);
                if (locationGPS != null) {
/*                    latit = locationGPS.getLatitude();
                    longit = locationGPS.getLongitude();
                    latitude = String.valueOf(latit);
                    longitude = String.valueOf(longit);
                    Log.d("LOCATION", "before snackbar");*/
                    geocoder = new Geocoder(this);
                    try {
                        user = geocoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                        latitude=(double)user.get(0).getLatitude();
                        longitude=(double)user.get(0).getLongitude();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    Snackbar.make(contextView, latitude + ", " + longitude, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(contextView, getResources().getString(R.string.location_error), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.jasonNavHostFragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // Alert dialog to send confirmation message for exiting application
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.alert_dialog_exit));

        // Exits application if yes is pressed
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", exit application
                finish();
            }
        });

        // Stays on application if no is pressed
        builder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog alert = builder.create();
        alert.show();
    }
}