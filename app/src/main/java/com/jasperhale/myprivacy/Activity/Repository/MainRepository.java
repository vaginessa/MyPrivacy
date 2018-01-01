package com.jasperhale.myprivacy.Activity.Repository;

import android.content.pm.PackageInfo;
import android.databinding.ObservableArrayList;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
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

/**
 * Created by ZHANG on 2017/12/31.
 */

public class MainRepository {
    private final Model model;
    private ObservableArrayList<ApplistItem> items_system;
    private ObservableArrayList<ApplistItem> items_user;
    private ObservableArrayList<ApplistItem> items_limit;

    public MainRepository() {
        model = new mModel();
        this.items_user = new ObservableArrayList<>();
        this.items_system = new ObservableArrayList<>();
        this.items_limit = new ObservableArrayList<>();
    }

    public ObservableArrayList<ApplistItem> getItems_user() {
        items_user = getitems(0);
        return items_user;
    }

    public ObservableArrayList<ApplistItem> getItems_system() {
        items_system = getitems(1);
        return items_system;
    }

    public ObservableArrayList<ApplistItem> getItems_limit(){
        items_limit = getitems(2);
        return items_limit;
    }

    private ObservableArrayList<ApplistItem> getitems(int position) {
        ObservableArrayList<ApplistItem> Appitems = new ObservableArrayList<>();
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

}

