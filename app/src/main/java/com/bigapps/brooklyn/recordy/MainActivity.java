package com.bigapps.brooklyn.recordy;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bigapps.brooklyn.recordy.fragments.add.AddProcedureFragment;
import com.bigapps.brooklyn.recordy.fragments.add.AddRecordFragment;
import com.bigapps.brooklyn.recordy.fragments.show.ShowCalendarFragment;
import com.bigapps.brooklyn.recordy.fragments.show.ShowProceduresFragment;
import com.bigapps.brooklyn.recordy.fragments.show.ShowRecordsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentManager fragmentManager;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nvView) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initNavigationView();
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragment = ShowRecordsFragment.newInstance();
            setTitle(R.string.menu_item_records);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentLayout, fragment)
                    .commit();
        }

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

    public void selectDrawerItem(MenuItem menuItem) {
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_second_fragment:
                fragmentClass = ShowProceduresFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ShowRecordsFragment.class;
                break;
            case R.id.nav_fourd_fragment:
                fragmentClass = ShowCalendarFragment.class;
                break;
            default:
                fragmentClass = ShowRecordsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }


}

