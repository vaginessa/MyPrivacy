package com.jasperhale.myprivacy.Activity.ViewModel;

import android.arch.lifecycle.*;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.Repository.MainRepository;


/**
 * Created by ZHANG on 2017/12/31.
 */

public class MainViewModel extends android.arch.lifecycle.ViewModel implements LifecycleObserver {
    private MainRepository mainRepository;
    public FragmentViewModel items_system;
    public FragmentViewModel items_user;
    public FragmentViewModel items_limit;
    private String TAG = "MainViewModel";

    public MainViewModel() {
        mainRepository = new MainRepository();
        this.items_user = new FragmentViewModel(mainRepository);
        this.items_system = new FragmentViewModel(mainRepository);
        this.items_limit = new FragmentViewModel(mainRepository);
    }


    //activity creat 初始化数据
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void creat() {
         mainRepository.Items_user_Obtain(items_user.behaviorSubject);
         mainRepository.Items_system_Obtain(items_system.behaviorSubject);
         mainRepository.Items_limit_Obtain(items_limit.behaviorSubject);
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

    public void SearchRecyclerview(String query,int position){
        switch (position){
            case 0:{
                LogUtil.d(TAG,"SearchRecyclerview"+position);
                break;
            }
            case 1:{
                LogUtil.d(TAG,"SearchRecyclerview"+position);
                break;
            }
            case 2:{
                //LogUtil.d(TAG,"SearchRecyclerview"+position);
                break;
            }
            default: break;
        }
    }
}
