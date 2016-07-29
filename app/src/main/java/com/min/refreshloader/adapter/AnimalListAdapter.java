package com.min.refreshloader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.min.refreshloader.R;
import com.min.refreshloader.base.HFRecyclerViewAdapter;
import com.min.refreshloader.bean.AnimalBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public class AnimalListAdapter extends HFRecyclerViewAdapter<AnimalBean,AnimalListAdapter.ItemViewHolder> {

    public AnimalListAdapter(Context context) {
        super(context);
    }

    @Override
    public ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_animal,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, int position) {
        AnimalBean bean=getData().get(position);
        holder.nameTv.setText(String.valueOf(bean.getName()));
        holder.ageTv.setText(String.valueOf(bean.getAge()));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_name) TextView nameTv;
        @Bind(R.id.tv_age) TextView ageTv;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickLitener!=null){
                        mOnItemClickLitener.onItemClick(itemView,itemPositionInData(getLayoutPosition()));
                    }
                }
            });
        }

    }

}
