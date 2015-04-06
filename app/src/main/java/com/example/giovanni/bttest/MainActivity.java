    package com.example.giovanni.bttest;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;


    public class MainActivity extends FragmentActivity
implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    private static final int REQUEST_ENABLE_BT = 0;
    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
    private Button button1;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    CPanel cPanel = new CPanel();
    Settings settings = new Settings();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (findViewById(R.id.container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }


            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //cPanel.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, cPanel).commit();
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        Fragment fragment;
        // update the main content by replacing fragments
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(position)
        {
            default:
            case 0:
                CPanel firstFragment = new CPanel();
                transaction.replace(R.id.container, firstFragment);
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case 1:

                CPanel secondFragment = new CPanel();
                transaction.replace(R.id.container, secondFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case 2:
                if (cPanel.isAdded())
                {
                    transaction.hide(cPanel);
                }
                /*
                if (settings.isAdded())
                {
                    // if the fragment is already in container
                    transaction.show(settings);
                } else
                */
                 // fragment needs to be added to frame container
                    transaction.replace(R.id.container, settings, "settings");
                    //transaction.addToBackStack(null);

                transaction.commit();
                break;
        }
    }

    public void onSectionAttached(int number)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (number)
        {
            case 1:

                // Create a new Fragment to be placed in the activity layout
                CPanel firstFragment = new CPanel();
                transaction.replace(R.id.container, firstFragment);
                transaction.commit();
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                /*
                BluetoothChatFragment fragment = new BluetoothChatFragment();
                transaction.replace(R.id.sample_content_fragment, fragment);
                transaction.commit();*/
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                Settings thirdFragment = new Settings();
                transaction.replace(R.id.container, thirdFragment);
                transaction.commit();
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
