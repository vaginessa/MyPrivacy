package com.jasperhale.myprivacy.Activity.Repository;

import android.content.pm.PackageInfo;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ObservableList;
import android.text.TextUtils;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.Base.MyApplicantion;
import com.jasperhale.myprivacy.Activity.adapter.BindingAdapterItem;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;
import com.jasperhale.myprivacy.Activity.model.Model;
import com.jasperhale.myprivacy.Activity.model.mModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by ZHANG on 2017/12/31.
 */

public class MainRepository {
    private final String TAG = "MainRepository";
    private final Model model;
    private ObservableList<ApplistItem> items_system;
    private ObservableList<ApplistItem> items_user;
    private ObservableList<ApplistItem> items_limit;

    public MainRepository() {
        model = new mModel();
        this.items_user = new ObservableArrayList<>();
        this.items_system = new ObservableArrayList<>();
        this.items_limit = new ObservableArrayList<>();
    }

    public ObservableList<ApplistItem> getItems_user() {
        return items_user;
    }

    public void Items_user_Obtain() {
        items_user = getitems(0);
    }

    public void Items_user_Obtain(BehaviorSubject<ObservableList> behaviorSubject) {
        getitems(0, behaviorSubject);
    }

    public ObservableList<ApplistItem> getItems_user(String query) {
        return searchitems(query, items_user);
    }

    public ObservableList<ApplistItem> getItems_system() {
        return items_system;
    }

    public void Items_system_Obtain() {
        items_system = getitems(1);
    }

    public void Items_system_Obtain(BehaviorSubject<ObservableList> behaviorSubject) {
        getitems(1, behaviorSubject);
        ;
    }

    public ObservableList<ApplistItem> getItems_system(String query) {
        return searchitems(query, items_system);
    }

    public ObservableList<ApplistItem> getItems_limit() {
        return items_limit;
    }

    public void Items_limit_Obtain() {
        items_limit = getitems(2);
    }

    public void Items_limit_Obtain(BehaviorSubject<ObservableList> behaviorSubject) {
        getitems(2, behaviorSubject);
    }

    public ObservableList<ApplistItem> getItems_limit(String query) {
        return searchitems(query, items_limit);
    }

    private ObservableList<ApplistItem> getitems(int position) {
        ObservableList<ApplistItem> Appitems = new ObservableArrayList<>();
        Observable
                .create((ObservableOnSubscribe<List<PackageInfo>>) emitter -> emitter.onNext(model.getPackages()))
                //等待
                .subscribeOn(Schedulers.trampoline())
                //cpu密集
                .observeOn(Schedulers.computation())
                //筛选系统应用
                .map(packages -> {
                    List<PackageInfo> items = new ArrayList<>();
                    switch (position) {
                        case 0: {
                            for (PackageInfo packageInfo : packages) {
                                if (!model.isSystemApp(packageInfo)) {
                                    items.add(packageInfo);
                                }
                            }
                            break;
                        }
                        case 1: {
                            for (PackageInfo packageInfo : packages) {
                                if (model.isSystemApp(packageInfo)) {
                                    items.add(packageInfo);
                                }
                            }
                            break;
                        }
                        case 2: {
                            for (PackageInfo packageInfo : packages) {
                                if (model.isLimited(packageInfo)) {
                                    items.add(packageInfo);
                                }
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    return items;
                })
                //创建Appitems
                .map(packages -> {
                    for (PackageInfo pac : packages) {
                        Appitems.add(model.creatApplistItem(pac));
                    }
                    return Appitems;
                })
                .subscribe(items -> {
                    Collections.sort(Appitems);
                });
        return Appitems;
    }

    private void getitems(int position, BehaviorSubject<ObservableList> behaviorSubject) {


        Observable<ObservableList> applistObservable = Observable
                .create((ObservableOnSubscribe<List<PackageInfo>>) emitter -> emitter.onNext(model.getPackages()))
                //等待
                .subscribeOn(Schedulers.trampoline())
                //cpu密集
                .observeOn(Schedulers.computation())
                //筛选系统应用
                .map(packages -> {
                    List<PackageInfo> items = new ArrayList<>();
                    switch (position) {
                        case 0: {
                            for (PackageInfo packageInfo : packages) {
                                if (!model.isSystemApp(packageInfo)) {
                                    items.add(packageInfo);
                                }
                            }
                            break;
                        }
                        case 1: {
                            for (PackageInfo packageInfo : packages) {
                                if (model.isSystemApp(packageInfo)) {
                                    items.add(packageInfo);
                                }
                            }
                            break;
                        }
                        case 2: {
                            for (PackageInfo packageInfo : packages) {
                                if (model.isLimited(packageInfo)) {
                                    items.add(packageInfo);
                                }
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    return items;
                })
                //创建Appitems
                .map(packages -> {
                    ObservableList<ApplistItem> Appitems = new ObservableArrayList<>();
                    for (PackageInfo pac : packages) {
                        Appitems.add(model.creatApplistItem(pac));
                    }
                    return Appitems;
                })
                .map(items -> {
                    Collections.sort(items);
                    return items;
                });
        applistObservable.subscribe(behaviorSubject);
        LogUtil.d(TAG, "reitems" + String.valueOf(position));
    }

    private ObservableList<ApplistItem> searchitems(String query, ObservableList<ApplistItem> items_main) {
        if (query.equals("")) {
            return items_main;
        } else {
            ObservableList<ApplistItem> items = new ObservableArrayList<>();
            Observable
                    .create((ObservableOnSubscribe<String>)
                            emitter -> emitter.onNext(MyApplicantion.transformPinYin(query))
                    )
                    .subscribeOn(Schedulers.newThread())
                    //cpu密集 搜索
                    .observeOn(Schedulers.computation())
                    .subscribe(result -> {
                        for (ApplistItem item : items_main) {
                            if ((item).getAppName_compare().contains(result)) {
                                items.add(item);
                            }
                        }
                    });
            return items;
        }
    }

}

