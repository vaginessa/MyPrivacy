package com.jasperhale.myprivacy.Activity.ViewModel;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;


import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.Repository.MainRepository;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by ZHANG on 2018/1/3.
 */

public class FragmentViewModel extends BaseObservable implements LifecycleObserver {

    private MainRepository mainRepository;
    //@Bindable
    public ObservableList<ApplistItem> items;
    private final String TAG = "FragmentViewModel";
    //订阅缓存
    public BehaviorSubject<ObservableList> behaviorSubject ;
    public Observer<ObservableList> setObserver;

    public FragmentViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
        items = new ObservableArrayList<>();
        behaviorSubject = BehaviorSubject.create();

        setObserver = new Observer<ObservableList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ObservableList observableList) {
                LogUtil.d(TAG,"setObserver"+observableList.toString());
                items.clear();
                items.addAll(observableList);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void set(){
        //订阅结果
        behaviorSubject.subscribe(items -> {
            LogUtil.d(TAG,"reitems"+"setItems(items)");
            setItems(items);
        });
    }


    public void setItems(ObservableList<ApplistItem> items) {
        LogUtil.d(TAG, "setItems");
        if (this.items.size() == 0){
            this.items.addAll(items);
        }else {
            this.items.clear();
            this.items.addAll(items);
        }
    }

    public ObservableList<ApplistItem> getItems() {
        LogUtil.d(TAG, "getItems");
        return items;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void creat() {
        LogUtil.d(TAG, getClass().getSimpleName() + "start");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        LogUtil.d(TAG, getClass().getSimpleName() + "start");
        behaviorSubject.subscribe(setObserver);
        //this.setItems(mainRepository.getItems_user());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void resume() {
        LogUtil.d(TAG, getClass().getSimpleName() + "resume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pause() {
        LogUtil.d(TAG, getClass().getSimpleName() + "pause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop() {
        LogUtil.d(TAG, getClass().getSimpleName() + "stop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destory() {
        LogUtil.d(TAG, getClass().getSimpleName() + "destory");
        behaviorSubject.onComplete();
    }


}
