package ua.com.yakovchuk.reminder.app.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.fragment.ListFragment;
import ua.com.yakovchuk.reminder.app.fragment.ViewMindFragment;

public class MainActivity extends ActionBarActivity {

    private Drawer drawerResult;
    private IProfile profile;
    private AccountHeader headerResult = null;
    private ListFragment listFragment;
    private ViewMindFragment viewMindFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSystemBarColor(this);
        setupNavigationDrawer(savedInstanceState);
        listFragment = new ListFragment();
        viewMindFragment = new ViewMindFragment();
    }

    public void toListViewFragment(MenuItem item) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        transaction.add(R.id.container, listFragment, listFragment.TAG);
        transaction.commit();
    }

    public void toViewMindFragment(MenuItem item) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        transaction.add(R.id.container, viewMindFragment, viewMindFragment.TAG);
        transaction.commit();
    }

    private void setupNavigationDrawer(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profile = new ProfileDrawerItem()
                .withName("Roman Yakovchuk")
                .withEmail("fromthesecond@gmail.com");

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withSavedInstance(savedInstanceState)
                .build();
        drawerResult = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .withAnimateDrawerItems(true)
                .withSavedInstance(savedInstanceState)
                .build();
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


}
