package com.jasperhale.myprivacy.Activity.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.Base.MyApplicantion;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;
import com.jasperhale.myprivacy.Activity.item.DiffCallBack_ApplistItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZHANG on 2017/12/10.
 */

public class BindAdapter_applist extends RecyclerView.Adapter<BindingHolder> {

    private ObservableArrayList<ApplistItem> items;
    protected ListChangedCallback itemsChangeCallback;

    public BindAdapter_applist() {
        super();
        this.items = new ObservableArrayList<>();
        this.itemsChangeCallback = new ListChangedCallback();
    }

    //获取ObservableArrayList<ApplistItem> 实例
    public ObservableArrayList<ApplistItem> getItems() {
        return items;
    }

    //显示list<item>
    public void setItems(ObservableArrayList<ApplistItem> items) {
        this.items = items;
    }

    //清除item
    public void clearItems() {
        items.clear();
    }

    //绑定ObservableArrayList 回掉
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        this.items.addOnListChangedCallback(itemsChangeCallback);
    }

    //解绑
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        super.onDetachedFromRecyclerView(recyclerView);
        this.items.removeOnListChangedCallback(itemsChangeCallback);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new BindingHolder(binding);
    }

    /*
    * 数据绑定
    * */
    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    /*
    * 定向刷新
    * */
    @Override
    public void onBindViewHolder(BindingHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            Bundle payload = (Bundle) payloads.get(0);//取出我们在getChangePayload（）方法返回的bundle
            LogUtil.d("Adatper", "");
            for (String key : payload.keySet()) {
                switch (key) {
                    case "AppId":
                        items.get(position).setAppId(payload.getString("AppId"));
                        break;
                    case "AppName":
                        items.get(position).setAppName(payload.getString("AppName"));
                        break;
                    case "AppIcon":
                        Bitmap bitmap = payload.getParcelable("AppIcon");
                        items.get(position).setAppIcon(new BitmapDrawable(MyApplicantion.getContext().getResources(), bitmap));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    //刷新列表
    private void Refresh(ObservableArrayList<ApplistItem> items) {
        Observable
                .create((ObservableOnSubscribe<String>)
                        emitter -> emitter.onNext("")
                )
                .subscribeOn(Schedulers.trampoline())
                //cpu密集
                .observeOn(Schedulers.newThread())
                .map(s -> DiffUtil.calculateDiff(new DiffCallBack_ApplistItem(BindAdapter_applist.this.getItems(), items), false))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    diffResult.dispatchUpdatesTo(BindAdapter_applist.this);
                    BindAdapter_applist.this.setItems(items);
                });
    }

    class ListChangedCallback extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<ApplistItem>> {
        @Override
        public void onChanged(ObservableArrayList<ApplistItem> newItems) {
            Refresh(newItems);
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<ApplistItem> newItems, int i, int i1) {
            Refresh(newItems);
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<ApplistItem> newItems, int i, int i1) {
            Refresh(newItems);
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<ApplistItem> newItems, int i, int i1, int i2) {
            Refresh(newItems);
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<ApplistItem> sender, int positionStart, int itemCount) {
            Refresh(sender);
        }
    }
}
