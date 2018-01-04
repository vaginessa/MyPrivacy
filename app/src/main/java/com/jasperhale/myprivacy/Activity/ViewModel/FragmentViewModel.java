package com.jasperhale.myprivacy.Activity.ViewModel;

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
import com.jasperhale.myprivacy.Activity.adapter.BindingHolder;
import com.jasperhale.myprivacy.Activity.item.ApplistItem;
import com.jasperhale.myprivacy.Activity.item.DiffCallBack_ApplistItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZHANG on 2018/1/3.
 */

public class FragmentViewModel extends RecyclerView.Adapter<BindingHolder> {
    public ObservableArrayList<ApplistItem> items;
    protected FragmentViewModel.ListChangedCallback itemsChangeCallback;

    /*
    public BindAdapter_applist(ObservableArrayList<ApplistItem> items) {
        super();
        this.items = items;
        this.itemsChangeCallback = new ListChangedCallback();
    }*/

    public FragmentViewModel() {
        super();
        this.items = new ObservableArrayList<>();
        this.itemsChangeCallback = new FragmentViewModel.ListChangedCallback();
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

    //绑定ObservableArrayList
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
                .map(s -> DiffUtil.calculateDiff(new DiffCallBack_ApplistItem(FragmentViewModel.this.getItems(), items), false))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    diffResult.dispatchUpdatesTo(FragmentViewModel.this);
                    FragmentViewModel.this.setItems(items);
                });
    }



    class ListChangedCallback extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<ApplistItem>> {
        @Override
        public void onChanged(ObservableArrayList<ApplistItem> newItems) {
            Refresh(newItems);
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<ApplistItem> newItems, int positionStart, int itemCount) {
            //notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<ApplistItem> newItems, int positionStart, int itemCount) {
            //notifyItemRangeInserted(positionStart, itemCount);
            //notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<ApplistItem> newItems,int fromPosition, int toPosition, int itemCount) {
            // Note:不支持一次性移动"多个"item
            //notifyItemMoved(fromPosition, toPosition);
            //notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<ApplistItem> sender, int positionStart, int itemCount) {
            // Note:不支持一次性移动"多个"item
            //notifyItemRangeRemoved(positionStart, itemCount);
            //notifyItemRangeChanged(positionStart, itemCount);
        }
    }
}
