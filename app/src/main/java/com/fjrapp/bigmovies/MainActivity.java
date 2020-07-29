package com.fjrapp.bigmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fjrapp.bigmovies.Movie.FavoriteFragment;
import com.fjrapp.bigmovies.Movie.MovieFragment;
import com.fjrapp.bigmovies.TvShow.FragmentFavoriteTvShow;
import com.fjrapp.bigmovies.TvShow.TvShowFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentManager fragmentManager;


    private TabLayout tabs;
    private int[] tabIcons = {
            R.drawable.tab_selector_movie,
            R.drawable.tab_selector_tvshow,
            R.drawable.tab_selector_favorite

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setIcon();

    }

    private void setIcon() {
        Objects.requireNonNull(tabs.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabs.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(tabs.getTabAt(2)).setIcon(tabIcons[2]);
        Objects.requireNonNull(tabs.getTabAt(3)).setIcon(tabIcons[2]);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    //adding fragment to tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(MovieFragment.newInstance(), getString(R.string.movies));
        adapter.addFragment(TvShowFragment.newInstance(fragmentManager), getString(R.string.tvshow));
        adapter.addFragment(FavoriteFragment.newInstance(fragmentManager), getString(R.string.movies));
        adapter.addFragment(FragmentFavoriteTvShow.newInstance(fragmentManager), getString(R.string.tvshow));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
