package com.schoolstuff.adrian.koshertracker;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


//TODO: add all items to your options menu (total 4 items) (NO need to implement functionality yet)
//TODO: now that you've created the basic MainActivity, move on to implement the next activity for locating and scanning jobs: the one with the map
//TODO: set the scan layout to contain a map view, preferences in the form of spinners and a button
//TODO: add functionality for the second activity.

public class MainActivity extends AppCompatActivity {
    private DataHandleTask task=null;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private FragmentManager manager;
    private TabLayout tab_layout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_data();
        init_components();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    public void init_data(){
        deleteDatabase("restaurants.db");
        task=new DataHandleTask(MainActivity.this);
        task.execute();
    }
    public void init_components(){
        manager=getSupportFragmentManager();
        tab_layout=findViewById(R.id.tabLayout);
        pager=findViewById(R.id.viewPager);
        pagerAdapter=new PagerAdapter(manager,instantiate_fragments(),getFragmentTitles());
        pager.setAdapter(pagerAdapter);
        tab_layout.setupWithViewPager(pager);

        toolbar=findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);



    }

    public List<Fragment> instantiate_fragments(){
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(Fragment.instantiate(MainActivity.this,RecordFragment.class.getName()));
        fragments.add(Fragment.instantiate(MainActivity.this,HomeFragment.class.getName()));
        fragments.add(Fragment.instantiate(MainActivity.this,FavoriteFragment.class.getName()));
        return fragments;
    }

    public List<String> getFragmentTitles(){
        List<String> title=new ArrayList<>();
        title.add("Records");
        title.add("Home");
        title.add("Favorites");
        return title;
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;
        public PagerAdapter(FragmentManager manager,List<Fragment> fragmentsList,List<String> titles) {
            super(manager);
            this.fragments=fragmentsList;
            this.titles=titles;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
