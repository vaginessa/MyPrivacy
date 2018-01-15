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
    public static void bindList(RecyclerView recyclerView, ObservableList items) {
        LogUtil.d("binditems", "");

        if (recyclerView.getAdapter() == null) {
            LogUtil.d("binditems", "view.getAdapter() == null");
            LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
            BindAdapter_applist adapter = new BindAdapter_applist(items);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            items.addOnListChangedCallback(new ObservableList.OnListChangedCallback() {
                @Override
                public void onChanged(ObservableList newItems) {
                    LogUtil.d("binditems", "onChanged");

                    if (!recyclerView.isComputingLayout()) {
                        Observable
                                .create((ObservableOnSubscribe<String>)
                                        emitter -> emitter.onNext("")
                                )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(diffResult -> {
                                    adapter.notifyDataSetChanged();
                                });
                        //adapter.notifyDataSetChanged();
                    }
                    //notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(ObservableList newItems, int positionStart, int itemCount) {
                    LogUtil.d("binditems", "onItemRangeChanged");

                    if (!recyclerView.isComputingLayout()) {
                        Observable
                                .create((ObservableOnSubscribe<String>)
                                        emitter -> emitter.onNext("")
                                )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(diffResult -> {
                                    adapter.notifyItemRangeChanged(positionStart, itemCount);
                                });
                        //adapter.notifyItemRangeChanged(positionStart, itemCount);
                    }
                    //notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeInserted(ObservableList newItems, int positionStart, int itemCount) {
                    LogUtil.d("binditems", "onItemRangeInserted");
                    if (!recyclerView.isComputingLayout()) {
                        Observable
                                .create((ObservableOnSubscribe<String>)
                                        emitter -> emitter.onNext("")
                                )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(diffResult -> {
                                    adapter.notifyItemRangeInserted(positionStart, itemCount);
                                });
                        //adapter.notifyItemRangeInserted(positionStart, itemCount);
                    }
                    //setItems(newItems);
                    //notifyItemRangeInserted(positionStart, itemCount);
                    //notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeMoved(ObservableList newItems, int fromPosition, int toPosition, int itemCount) {
                    LogUtil.d("binditems", "onItemRangeMoved");
                    if (!recyclerView.isComputingLayout()) {
                        Observable
                                .create((ObservableOnSubscribe<String>)
                                        emitter -> emitter.onNext("")
                                )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(diffResult -> {
                                    adapter.notifyDataSetChanged();
                                });
                        //adapter.notifyDataSetChanged();
                    }
                    // Note:不支持一次性移动"多个"item
                    //notifyDataSetChanged();
                }

                @Override
                public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                    LogUtil.d("binditems", "onItemRangeRemoved");
                    if (!recyclerView.isComputingLayout()) {
                        Observable
                                .create((ObservableOnSubscribe<String>)
                                        emitter -> emitter.onNext("")
                                )
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(diffResult -> {
                                    adapter.notifyItemRangeRemoved(positionStart, itemCount);
                                });
                        //adapter.notifyItemRangeRemoved(positionStart, itemCount);
                    }
                    //notifyItemRangeRemoved(positionStart, itemCount);
                }
            });
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            LogUtil.d("binditems", "bindAdapter_applist.setItems(items);");
            //BindAdapter_applist bindAdapter_applist = (BindAdapter_applist) recyclerView.getAdapter();
            //bindAdapter_applist.setItems(items);
        }
    }
}
