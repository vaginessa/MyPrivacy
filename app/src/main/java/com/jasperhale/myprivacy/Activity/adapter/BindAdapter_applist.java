package com.jasperhale.myprivacy.Activity.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jasperhale.myprivacy.Activity.Base.LogUtil;
import com.jasperhale.myprivacy.Activity.Base.MyApplicantion;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;
import com.jasperhale.myprivacy.Activity.item.DiffCallBack_ApplistItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZHANG on 2017/12/10.
 */

public class BindAdapter_applist extends RecyclerView.Adapter<BindingHolder> {

    private ObservableList<ApplistItem> items;
    protected ListChangedCallback itemsChangeCallback;
    RecyclerView recyclerView;
    private final String TAG ="BindAdapter_applist";



    public BindAdapter_applist(@Nullable ObservableList<ApplistItem> items) {
        super();
        this.itemsChangeCallback = new ListChangedCallback();
        this.setItems(items);
    }

    public BindAdapter_applist() {
        super();
        this.items = new ObservableArrayList<>();
        this.itemsChangeCallback = new ListChangedCallback();
    }

    //获取ObservableList<ApplistItem> 实例
    public ObservableList<ApplistItem> getItems() {
        return items;
    }

    //显示list<item>
    public void setItems(@Nullable ObservableList<ApplistItem> items) {
        this.items = items;
        if (this.items == items)
        {
            return;
        }

        if (this.items != null)
        {
            this.items.removeOnListChangedCallback(itemsChangeCallback);
            notifyItemRangeRemoved(0, this.items.size());
        }

        if (items instanceof ObservableList)
        {
            this.items = (ObservableList<ApplistItem>) items;
            notifyItemRangeInserted(0, this.items.size());
            this.items.addOnListChangedCallback(itemsChangeCallback);
        }
        else if (items != null)
        {
            this.items = new ObservableArrayList<>();
            this.items.addOnListChangedCallback(itemsChangeCallback);
            this.items.addAll(items);
        }
        else
        {
            this.items = null;
        }
    }

    //清除item
    public void clearItems() {
        items.clear();
    }

    //绑定ObservableList 回掉
    /*
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        this.items.addOnListChangedCallback(itemsChangeCallback);
        this.recyclerView = recyclerView;
    }*/

    //解绑
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        super.onDetachedFromRecyclerView(recyclerView);
        this.items.removeOnListChangedCallback(itemsChangeCallback);
        this.recyclerView = null;
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
    private void Refresh(ObservableList<ApplistItem> items) {
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


    class ListChangedCallback extends ObservableList.OnListChangedCallback<ObservableList<ApplistItem>> {
        @Override
        public void onChanged(ObservableList<ApplistItem> newItems) {
            LogUtil.d(TAG,"onChanged");
            Observable
                    .create((ObservableOnSubscribe<String>)
                            emitter -> emitter.onNext("")
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diffResult -> {
                        if (!(recyclerView == null)){
                            if (!recyclerView.isComputingLayout()){
                                Refresh(newItems);
                            }
                        }
                    });

            //Refresh(newItems);
            //notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<ApplistItem> newItems, int positionStart, int itemCount) {
            LogUtil.d(TAG,"onItemRangeChanged");
            Observable
                    .create((ObservableOnSubscribe<String>)
                            emitter -> emitter.onNext("")
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diffResult -> {

                        if (!(recyclerView == null)){
                            if (!recyclerView.isComputingLayout()){
                                notifyItemRangeChanged(positionStart, itemCount);
                            }
                        }
                    });
            //notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<ApplistItem> newItems, int positionStart, int itemCount) {
            LogUtil.d(TAG,"onItemRangeInserted");
            Observable
                    .create((ObservableOnSubscribe<String>)
                            emitter -> emitter.onNext("")
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diffResult -> {
                        if (!(recyclerView == null)){
                            if (!recyclerView.isComputingLayout()){
                                notifyItemRangeInserted(positionStart, itemCount);
                            }
                        }
                    });


            //notifyItemRangeInserted(positionStart, itemCount);
            //notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<ApplistItem> newItems,int fromPosition, int toPosition, int itemCount) {
            LogUtil.d(TAG,"onItemRangeMoved");
            Observable
                    .create((ObservableOnSubscribe<String>)
                            emitter -> emitter.onNext("")
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diffResult -> {
                        if (!(recyclerView == null)){
                            if (!recyclerView.isComputingLayout()){
                                notifyDataSetChanged();
                            }
                        }
                    });

            // Note:不支持一次性移动"多个"item
            //notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(ObservableList<ApplistItem> sender, int positionStart, int itemCount) {
            LogUtil.d(TAG,"onItemRangeRemoved");
            Observable
                    .create((ObservableOnSubscribe<String>)
                            emitter -> emitter.onNext("")
                    )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(diffResult -> {
                        if (!(recyclerView == null)){
                            if (!recyclerView.isComputingLayout()){
                                notifyItemRangeRemoved(positionStart, itemCount);
                            }
                        }
                    });
            //notifyItemRangeRemoved(positionStart, itemCount);
        }
    }
}
