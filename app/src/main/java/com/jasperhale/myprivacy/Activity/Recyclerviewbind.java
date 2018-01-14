package com.jasperhale.myprivacy.Activity;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.ViewModel.FragmentViewModel;
import com.jasperhale.myprivacy.Activity.adapter.BindAdapter_applist;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;
import com.jasperhale.myprivacy.Activity.item.DiffCallBack_ApplistItem;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by ZHANG on 2018/1/3.
 */

public class Recyclerviewbind {
    @BindingAdapter("loadAppIcon")
    public static void LoadDrawable(ImageView imageView, Drawable Icon) {
        Context context = imageView.getContext();
        RequestOptions options = new RequestOptions()
                .error(Icon)
                .placeholder(Icon);
        Glide.with(context)
                .load("")
                .apply(options)
                .into(imageView);
    }

    @BindingAdapter("binditems")
    public  static void bindList(RecyclerView view, ObservableList items) {

        if (view.getAdapter() == null) {
            LogUtil.d("binditems","view.getAdapter() == null");
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            view.setLayoutManager(layoutManager);
            view.setAdapter(new BindAdapter_applist(items));
            view.setNestedScrollingEnabled(false);
        }else {
            LogUtil.d("binditems","bindAdapter_applist.setItems(items);");
            BindAdapter_applist bindAdapter_applist = (BindAdapter_applist)view.getAdapter();
            bindAdapter_applist.setItems(items);
        }
    }
}
