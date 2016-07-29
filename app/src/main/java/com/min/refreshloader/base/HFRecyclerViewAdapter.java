package com.min.refreshloader.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class HFRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseRecyclerViewAdapter<T>{

    public HFRecyclerViewAdapter(Context context) {
        super(context);
    }

    private static final int TYPE_HEADER = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
    private static final int TYPE_ADAPTEE_OFFSET = 2;
    private RecyclerView.ViewHolder headerViewHolder;
    private RecyclerView.ViewHolder footerViewHolder;

    public static class HFViewHolder extends RecyclerView.ViewHolder {
        public HFViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setHeaderView(View header){
        if (headerViewHolder == null || header != headerViewHolder.itemView){
            headerViewHolder = new HFViewHolder(header);
            notifyItemInserted(0);
        }
    }

    public void setFooterView(View foot){
        if (footerViewHolder == null || foot != footerViewHolder.itemView){
            footerViewHolder = new HFViewHolder(foot);
            notifyItemInserted(getItemCount());
        }
    }

    public void removeHeader(){
        if (headerViewHolder != null){
            headerViewHolder = null;
            notifyItemRemoved(0);
        }
    }
    public void removeFooter(){
        if (footerViewHolder != null){
            footerViewHolder = null;
            notifyItemRemoved(getItemCount());
        }
    }

    public boolean isHeader(int position){
        return hasHeader() && position == 0;
    }

    public boolean isFooter(int position){
        return hasFooter() && position == getDataItemCount() + (hasHeader() ? 1 : 0);
    }

    protected int itemPositionInData(int rvPosition){
        return rvPosition - (hasHeader() ? 1 : 0);
    }
    protected int itemPositionInRV(int dataPosition){
        return dataPosition + (hasHeader() ? 1 : 0);
    }

    @Override
    public void notifyMyItemInserted(int itemPosition) {
        notifyItemInserted(itemPositionInRV(itemPosition));
    }

    @Override
    public void notifyMyItemRemoved(int itemPosition) {
        notifyItemRemoved(itemPositionInRV(itemPosition));
    }

    @Override
    public void notifyMyItemChanged(int itemPosition){
        notifyItemChanged(itemPositionInRV(itemPosition));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return headerViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            return footerViewHolder;
        }
        return onCreateDataItemViewHolder(parent, viewType - TYPE_ADAPTEE_OFFSET);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isHeader(position) && !isFooter(position))
            onBindDataItemViewHolder((VH)holder, itemPositionInData(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = getDataItemCount();
        if (hasHeader()) {
            itemCount += 1;
        }
        if (hasFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        }
        if (isFooter(position)) {
            return TYPE_FOOTER;
        }
        int dataItemType = getDataItemType(itemPositionInData(position));
        if (dataItemType >= Integer.MAX_VALUE - TYPE_ADAPTEE_OFFSET) {
            throw new IllegalStateException("getDataItemType() must be less than (Integer.MAX_VALUE － " + TYPE_ADAPTEE_OFFSET + ").");
        }
        return dataItemType + TYPE_ADAPTEE_OFFSET;
    }

    public int getDataItemCount() {
        return super.getItemCount();
    }

    /**
     * make sure your dataItemType < Integer.MAX_VALUE-1
     *
     * @param position item view position in rv
     * @return item viewType
     */
    public int getDataItemType(int position){
        return 0;
    }


    public boolean hasHeader(){
        return headerViewHolder != null;
    }
    public boolean hasFooter(){
        return footerViewHolder != null;
    }

    public abstract VH onCreateDataItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindDataItemViewHolder(VH holder, int position);

//    public class ItemViewHolder extends RecyclerView.ViewHolder{
//
//        public ItemViewHolder(final View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mOnItemClickLitener!=null){
//                        mOnItemClickLitener.onItemClick(itemView,itemPositionInData(getLayoutPosition()));
//                    }
//                }
//            });
//        }
//
//    }
}
