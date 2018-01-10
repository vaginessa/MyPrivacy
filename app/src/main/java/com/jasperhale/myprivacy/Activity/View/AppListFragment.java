package com.jasperhale.myprivacy.Activity.View;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.ViewModel.MainViewModel;

import com.jasperhale.myprivacy.Activity.item.ApplistItem;
import com.jasperhale.myprivacy.R;




public class AppListFragment extends Fragment {

    private String Tag = "AppListFragment";
    private com.jasperhale.myprivacy.AppListFragment binding;
    private int position;

    private MainViewModel mainViewModel;

    private ObservableList<ApplistItem> items;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.d("UI", "onCreate" + String.valueOf(position));

        Bundle bundle = getArguments();
        position = bundle.getInt("MainActivity");

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        items = new ObservableArrayList<>();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d("UI", "onCreateView" + String.valueOf(position));

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_app_list, container, false);

        //binding.setModelList(mainViewModel.items_user);

        switch (position) {
            case 0: {
                //fragmentViewModel.setItems(mainViewModel.getItems_user());
                binding.setModelList(mainViewModel.items_user);
                break;
            }
            case 1: {
                //fragmentViewModel.setItems(mainViewModel.getItems_system());
                binding.setModelList(mainViewModel.getItems_system());
                break;
            }
            case 2: {
                //fragmentViewModel.setItems(mainViewModel.getItems_limit());
                binding.setModelList(mainViewModel.getItems_limit());
                break;
            }
            default:
                break;
        }

        initSwipeRefresh();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        LogUtil.d("UI", "onStart()" + String.valueOf(position));
        super.onStart();
    }

    @Override
    public void onPause() {
        LogUtil.d("UI", "onPause" + String.valueOf(position));
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.d("UI", "onStop(" + String.valueOf(position));
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtil.d("UI", "onDestroyView" + String.valueOf(position));
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.d("UI", "onDestroy" + String.valueOf(position));
        super.onDestroy();
    }


    private void initSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            LogUtil.d(Tag,"Refresh");
            //mainViewModel.getItems_user().items = new ObservableList<>();
            //mainViewModel.getItems_user().clear();
            //items.addAll(mainViewModel.getItems_user());
            //items = mainViewModel.getItems_user();
            //mainViewModel.items_user.addAll(mainViewModel.getItems_system());
            //mainViewModel.items_user.clear();
            mainViewModel.items_user.addAll(mainViewModel.getItems_system());
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }
}
