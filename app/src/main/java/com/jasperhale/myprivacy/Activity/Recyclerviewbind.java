package com.jasperhale.myprivacy.Activity;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jasperhale.myprivacy.Activity.ViewModel.FragmentViewModel;
import com.jasperhale.myprivacy.Activity.adapter.BindAdapter_applist;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;


/**
 * Created by ZHANG on 2018/1/3.
 */

public class Recyclerviewbind {
    @BindingAdapter("loadAppIcon")
    public static void LoadDrawable(ImageView imageView, Drawable Icon) {
        Context context = imageView.getContext();
        //BitmapDrawable bd = (BitmapDrawable)Icon;
        //Bitmap bitmap = bd.getBitmap();

        RequestOptions options = new RequestOptions()
                .error(Icon)
                .placeholder(Icon);
        Glide.with(context)
                .load("")
                .apply(options)
                .into(imageView);
    }

    @BindingAdapter("binditems")
    public  static void bindList(RecyclerView view,ObservableArrayList<ApplistItem> items) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setAdapter(new BindAdapter_applist(items));
        view.setNestedScrollingEnabled(false);
    }

}
