package com.jasperhale.myprivacy.Activity.View;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.ViewModel.MainViewModel;
import com.jasperhale.myprivacy.R;




public class AppListFragment extends Fragment {

    private String Tag = "AppListFragment";
    private com.jasperhale.myprivacy.AppListFragment binding;
    private int position;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.d("UI", "onCreate" + String.valueOf(position));

        Bundle bundle = getArguments();
        position = bundle.getInt("MainActivity");

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        switch (position) {
            case 0: {
                this.getLifecycle().addObserver(mainViewModel.items_user);
                break;
            }
            case 1: {
                this.getLifecycle().addObserver(mainViewModel.items_system);
                break;
            }
            case 2: {
                this.getLifecycle().addObserver(mainViewModel.items_limit);
                break;
            }
            default:
                break;
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d("UI", "onCreateView" + String.valueOf(position));

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_app_list, container, false);

        switch (position) {
            case 0: {
                binding.setFragmentViewModel(mainViewModel.items_user);
                break;
            }
            case 1: {
                binding.setFragmentViewModel(mainViewModel.items_system);
                break;
            }
            case 2: {
                binding.setFragmentViewModel(mainViewModel.items_limit);
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
            mainViewModel.items_user.set();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }
}
