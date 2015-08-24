package ua.com.yakovchuk.reminder.app.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.fragment.CreateMindFragment;
import ua.com.yakovchuk.reminder.app.fragment.ListFragment;
import ua.com.yakovchuk.reminder.app.fragment.MainFragment;
import ua.com.yakovchuk.reminder.app.fragment.ViewMindFragment;
import ua.com.yakovchuk.reminder.app.interfaces.Message;

public class MainActivity extends AppCompatActivity implements Message {

    private ListFragment listFragment;
    private ViewMindFragment viewMindFragment;
    private MainFragment mainFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private CreateMindFragment createMindFragment;
    private boolean exit;
    private boolean isFirstRun;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private android.support.v7.app.ActionBar actionBar;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private Address lastAddress;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSystemBarColor(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        listFragment = new ListFragment();
        viewMindFragment = new ViewMindFragment();
        createMindFragment = new CreateMindFragment();
        mainFragment = new MainFragment();
        drawerLayout = new DrawerLayout(getApplicationContext());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                toolbar,
                R.string.opened,
                R.string.closed
        );
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isfirstrun", true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (isConnected()) {
                    try {
                        TextView textView = (TextView) findViewById(R.id.textView4);
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        textView.setText(addresses.get(0).getCountryName() +
                                ", "+ addresses.get(0).getLocality() +
                                ", " + addresses.get(0).getAdminArea() +
                                ", " + addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    TextView textView = (TextView) findViewById(R.id.textView4);
                    textView.setText("You`re offline!");
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                TextView textView = (TextView) findViewById(R.id.textView4);
                textView.setText("GPS is disabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                TextView textView = (TextView) findViewById(R.id.textView4);
                textView.setText("Searching for location");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Auto-generated method stub
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        if (isFirstRun) {
            toMainFragment();
        } else {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).
                    edit().putBoolean("isfirstrun", false).commit();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.list_item:
                        toListViewFragment();
                        menuItem.setChecked(true);
                        break;
                    case R.id.main_item:
                        menuItem.setChecked(true);
                        toMainFragment();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public void toMainFragment() {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        transaction.replace(R.id.container, mainFragment, mainFragment.TAG);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFloatingButtonAction(View view) {
        toCreateMindFragment();
    }

    public void toListViewFragment() {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        transaction.replace(R.id.container, listFragment, listFragment.TAG);
        transaction.commit();
    }

    public void toCreateMindFragment() {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right,
                R.anim.slide_in_left, R.anim.slide_in_right);
        transaction.replace(R.id.container, createMindFragment, createMindFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setSystemBarColor(Activity activity) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.MaterialIndigo700));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (listFragment.isVisible()) {
            menu.setGroupVisible(R.id.search_group, true);
        }
        if (createMindFragment.isVisible()) {
            menu.setGroupVisible(R.id.ok_group, true);
        }
        if (viewMindFragment.isVisible()) {
            menu.setGroupVisible(R.id.delete_group, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public Typeface getFont(String arg) {
        String string = null;
        switch (arg) {
            case "regular":
                string = getString(R.string.font_roboto_regular);
                break;
            case "bold":
                string = getString(R.string.font_roboto_bold);
                break;
            case "medium":
                string = getString(R.string.font_roboto_medium);
                break;
        }
        Typeface face = Typeface.createFromAsset(getAssets(),
                string);
        return face;
    }

    @Override
    public void onBackPressed() {
        if (viewMindFragment.isVisible() || drawerLayout.isShown()) {
            manager.popBackStack();
            drawerLayout.closeDrawers();
        } else {
            if (exit) {
                if (!isFinishing()) {
                    finish();
                }
            } else {
                Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
                exit = true;
            }
        }
    }

    @Override
    public void respond(String respondMessage) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        viewMindFragment.setRespondMessage(respondMessage);
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right,
                R.anim.slide_in_left, R.anim.slide_in_right); // more animation for popBackStack()
        transaction.replace(R.id.container, viewMindFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}
