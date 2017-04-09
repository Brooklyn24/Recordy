package com.bigapps.brooklyn.recordy;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bigapps.brooklyn.recordy.fragments.SettingsFragment;
import com.bigapps.brooklyn.recordy.fragments.add.AddProcedureFragment;
import com.bigapps.brooklyn.recordy.fragments.add.AddRecordFragment;
import com.bigapps.brooklyn.recordy.fragments.show.ShowCalendarFragment;
import com.bigapps.brooklyn.recordy.fragments.show.ShowClietnsFragment;
import com.bigapps.brooklyn.recordy.fragments.show.ShowProceduresFragment;
import com.bigapps.brooklyn.recordy.fragments.show.ShowRecordAdvanced;
import com.bigapps.brooklyn.recordy.fragments.show.ShowRecordsFragment;
import com.bigapps.brooklyn.recordy.interfaces.RvClickedNewFragment;
import com.bigapps.brooklyn.recordy.interfaces.ShowSnackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ShowSnackbar, RvClickedNewFragment {

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nvView) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initNavigationView();
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mFragment = ShowCalendarFragment.newInstance();
            setTitle(R.string.menu_item_calendar);
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentLayout, mFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Class fragmentClass;
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch(menuItem.getItemId()) {
            case R.id.clients_fragment:
                fragmentClass = ShowClietnsFragment.class;
                break;
            case R.id.procedures_fragment:
                fragmentClass = ShowProceduresFragment.class;
                break;
            case R.id.records_fragment:
                fragmentClass = ShowRecordsFragment.class;
                break;
            case R.id.calendar_fragment:
                fragmentClass = ShowCalendarFragment.class;
                break;
            default:
                fragmentClass = ShowRecordsFragment.class;
        }

        try {
            mFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mFragmentManager.beginTransaction().replace(R.id.fragmentLayout, mFragment).commit();
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenu:
                mFragment = SettingsFragment.newInstance();
                setTitle(item.getTitle());
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragmentLayout, mFragment)
                        .addToBackStack(null)
                        .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void initNavigationView(){

        setupDrawerContent(navigationView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.view_navigation_open,
                        R.string.view_navigation_close
                );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void showSnackbar(String message) {
        ((ShowSnackbar)mFragment).showSnackbar(message);
    }

    @Override
    public void ReplaceFragment(long id) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragmentLayout, ShowRecordAdvanced.newInstance(id))
                .addToBackStack(null)
                .commit();
    }
}

