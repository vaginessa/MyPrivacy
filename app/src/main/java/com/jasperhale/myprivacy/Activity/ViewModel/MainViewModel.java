package com.jasperhale.myprivacy.Activity.ViewModel;

import android.arch.lifecycle.*;
import android.databinding.ObservableArrayList;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.Repository.MainRepository;
import com.jasperhale.myprivacy.Activity.adapter.BindingAdapterItem;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHANG on 2017/12/31.
 */

public class MainViewModel extends android.arch.lifecycle.ViewModel implements LifecycleObserver {
    private MainRepository mainRepository;
    private ObservableArrayList<ApplistItem> items_system;
    private ObservableArrayList<ApplistItem> items_user;
    private ObservableArrayList<ApplistItem> items_limit;
    private String TAG = "MainViewModel";

    public MainViewModel() {
        mainRepository = new MainRepository();
        this.items_user = new ObservableArrayList<>();
        this.items_system = new ObservableArrayList<>();
        this.items_limit = new ObservableArrayList<>();
    }

    public ObservableArrayList<ApplistItem> getItems_user() {
        return items_user;
    }

    public ObservableArrayList<ApplistItem> getItems_system() {
        return items_system;
    }

    public ObservableArrayList<ApplistItem> getItems_limit() {
        return items_limit;
    }


    //activity creat 初始化数据
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void creat() {
        items_user = mainRepository.getItems_user();
        items_system = mainRepository.getItems_system();
        items_limit = mainRepository.getItems_limit();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        LogUtil.d(TAG, "start");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void resume() {
        LogUtil.d(TAG, "resume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pause() {
        LogUtil.d(TAG, "pause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop() {
        LogUtil.d(TAG, "stop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destory() {
        LogUtil.d(TAG, "destory");
    }
}
