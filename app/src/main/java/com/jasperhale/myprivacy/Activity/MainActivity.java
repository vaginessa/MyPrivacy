package com.jasperhale.myprivacy.Activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.jasperhale.myprivacy.Activity.Base.BaseActivity;
import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.View.AboutActivity;
import com.jasperhale.myprivacy.Activity.View.AppListFragment;
import com.jasperhale.myprivacy.Activity.View.SettingActivity;
import com.jasperhale.myprivacy.Activity.ViewModel.MainViewModel;
import com.jasperhale.myprivacy.R;
import com.jasperhale.myprivacy.databinding.ActivityMainBinding;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, SearchView.OnQueryTextListener {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private Toolbar toolbar;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化 binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //设置toolbar
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        this.getLifecycle().addObserver(mainViewModel);

        //ViewPage适配器
        mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(mSectionsPagerAdapter);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setOffscreenPageLimit(2);

        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent_about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onQueryTextChange(String query) {
        LogUtil.d("Search", query);
        mainViewModel.SearchRecyclerview(query,binding.tabLayout.getSelectedTabPosition());
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position,
                               float positionOffset,
                               int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (!searchView.getQuery().equals("")) {
            LogUtil.d("UI", String.valueOf(position));
            mainViewModel.SearchRecyclerview(searchView.getQuery().toString(),position);
        }
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("MainActivity", position);
            //新建Fragment 实例,传入postion
            AppListFragment fragment = new AppListFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "用户应用";
                case 1:
                    return "系统应用";
                case 2:
                    return "已限制应用";
                default:
                    return "";
            }
        }
    }

}


