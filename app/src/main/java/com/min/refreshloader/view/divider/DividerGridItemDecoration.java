package com.min.refreshloader.view.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 网格分割线，分割线出现的地方为两个item相交部分
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration{

    private static final int[] ATTRS = new int[] { android.R.attr.listDivider };
    private Drawable mDivider;
    private int mIndex=0;

    public DividerGridItemDecoration(Context context){
        setDefaultDrawable(context);
    }

    public DividerGridItemDecoration(Context context,int drawableId){
        if(drawableId>0){
            mDivider = context.getResources().getDrawable(drawableId);
        }else{
            setDefaultDrawable(context);
        }
    }

    public DividerGridItemDecoration(Context context,Drawable drawable){
        if(drawable!=null){
            mDivider = drawable;
        }else{
            setDefaultDrawable(context);
        }
    }

    private void setDefaultDrawable(Context context){
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent){
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent){
        int childCount = parent.getChildCount();
        for (int i = mIndex; i < childCount-1; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent){
        int childCount = parent.getChildCount();
        for (int i = mIndex; i < childCount-1; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 如果是最后一行，则不需要绘制底部
     * @param parent
     * @param pos
     * @param spanCount
     * @param adapterCount
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,int adapterCount){
        int tempCount = adapterCount - adapterCount % spanCount;
        if(tempCount==adapterCount){
            for(int i=adapterCount-spanCount;i<adapterCount;i++){
                if(pos==i){
                    return true;
                }
            }
        }
        if (pos >= tempCount){
            return true;
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int adapterCount = parent.getAdapter().getItemCount();
        int itemPosition=parent.getChildAdapterPosition(view);

        outRect.left=0;
        outRect.top=0;
        if((itemPosition+1)%spanCount==0){  //最后一列
            outRect.right=0;
        }else{
            outRect.right=mDivider.getIntrinsicHeight();
        }

        if (isLastRaw(parent, itemPosition, spanCount, adapterCount)){  //最后一行
            outRect.bottom=0;
        }else{
            outRect.bottom=mDivider.getIntrinsicHeight();
        }
    }
}
