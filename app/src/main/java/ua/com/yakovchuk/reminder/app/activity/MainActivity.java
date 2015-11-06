package ua.com.yakovchuk.reminder.app.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.entity.Mind;
import ua.com.yakovchuk.reminder.app.fragment.CreateMindFragment;
import ua.com.yakovchuk.reminder.app.fragment.ListFragment;
import ua.com.yakovchuk.reminder.app.fragment.MainFragment;
import ua.com.yakovchuk.reminder.app.fragment.ViewMindFragment;
import ua.com.yakovchuk.reminder.app.interfaces.Message;
import ua.com.yakovchuk.reminder.app.interfaces.MindMessage;

public class MainActivity extends AppCompatActivity implements Message, MindMessage {

    private ListFragment listFragment;
    private ViewMindFragment viewMindFragment;
    private MainFragment mainFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private CreateMindFragment createMindFragment;
    private boolean exit;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private android.support.v7.app.ActionBar actionBar;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        setContentView(R.layout.activity_main);
        setSystemBarColor(this);
        prefs = getSharedPreferences("com.ua.com.yakovchuk.reminder", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        listFragment = new ListFragment();
        toListViewFragment();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void respondMind(Mind mind) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        viewMindFragment.setRespondMind(mind);
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right,
                R.anim.slide_in_left, R.anim.slide_in_right); // more animation for popBackStack()
        transaction.replace(R.id.container, viewMindFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
