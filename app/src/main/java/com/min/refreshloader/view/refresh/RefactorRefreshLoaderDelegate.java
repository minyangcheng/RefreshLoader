package com.min.refreshloader.view.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.min.refreshloader.R;
import com.min.refreshloader.app.AppConstants;
import com.min.refreshloader.base.HFRecyclerViewAdapter;
import com.min.refreshloader.base.PageLoader;
import com.min.refreshloader.util.ListUtils;
import com.min.refreshloader.util.UIUtils;
import com.min.refreshloader.view.StateLayout;
import com.min.refreshloader.view.divider.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public abstract class RefactorRefreshLoaderDelegate<DATATYPE
        ,HOLDER extends RecyclerView.ViewHolder
        ,ADAPTER extends HFRecyclerViewAdapter<DATATYPE,HOLDER>>{

    protected static final int STATE_CONTENT=1;
    protected static final int STATE_EMPTY=2;
    protected static final int STATE_EROOR=3;
    protected static final int STATE_LOADING=4;

    @Bind(R.id.view_state)
    protected StateLayout mStateView;
    @Bind(R.id.view_refresh)
    protected SwipeRefreshLayout mRefreshView;
    @Bind(R.id.rv_list)
    protected RecyclerView mListRv;

    protected ADAPTER mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mDecoration;
    protected PageLoader mPageLoader;

    protected int mNextPage=0;
    protected int mState;

    protected Context mContext;
    protected boolean mPageEnable;

    public RefactorRefreshLoaderDelegate(RefreshLoaderView refreshLoaderView,ADAPTER adapter,boolean pageEnable){
        mContext=refreshLoaderView.getContext();
        mAdapter=adapter;
        mPageEnable=pageEnable;
        initOtherParams();
        ButterKnife.bind(this, refreshLoaderView);
    }

    protected void initOtherParams(){
        mLayoutManager=new LinearLayoutManager(mContext);
        mDecoration=new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST
                ,R.drawable.shape_line);
    }

    public RefactorRefreshLoaderDelegate startLoad(){
        initViews();
        initData();
        return this;
    }

    public RefactorRefreshLoaderDelegate setLayoutManager(RecyclerView.LayoutManager layoutManager){
        mLayoutManager=layoutManager;
        return this;
    }

    public RefactorRefreshLoaderDelegate setItemDecoration(RecyclerView.ItemDecoration decoration){
        mDecoration=decoration;
        return this;
    }

    private void initData() {
        changeState(STATE_LOADING);
        onRefreshData();
    }

    protected void initViews(){
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        mStateView.setOnRetryListener(new StateLayout.OnRetryListener() {
            @Override
            public void onRetry() {
                initData();
            }
        });
        initRecycleView();
    }

    protected abstract void onRefreshData();

    protected abstract void onLoadMoreData();

    protected void setRefreshDataSuccess(List<DATATYPE> receiveList){
        if(ListUtils.isEmpty(receiveList)){
            changeState(STATE_EMPTY);
        }else{
            changeState(STATE_CONTENT);
            mAdapter.setData(receiveList);
        }
        judgeLoadFinally(receiveList);
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }

        if(mPageEnable){
            mNextPage=1;
        }
    }

    protected void setRefreshDataFail(String errorMess){
        if(ListUtils.isEmpty(mAdapter.getData())){
            changeState(STATE_EROOR);
        }else{
            UIUtils.toast(mContext, errorMess);
        }
        if(mRefreshView.isRefreshing()){
            mRefreshView.setRefreshing(false);
        }
    }

    protected void setLoadMoreDataSuccess(List<DATATYPE> receiveList){
        if(!ListUtils.isEmpty(receiveList)){
            mAdapter.getData().addAll(receiveList);
            mAdapter.notifyDataSetChanged();

            mNextPage++;
        }
        if(mPageLoader!=null) {
            mPageLoader.setLoadSuccess();
        }
        judgeLoadFinally(receiveList);
    }

    protected void setLoadLoadMoreFail(String errorMess){
        if(mPageLoader!=null) mPageLoader.setLoadFail();
    }

    protected void judgeLoadFinally(List<DATATYPE> receiveList){
        if(mPageLoader==null) return;
        if(receiveList==null||receiveList.size()< AppConstants.PAGE_SIZE){
            mPageLoader.setLoadFianlly(true);
        }else{
            mPageLoader.setLoadFianlly(false);
        }
    }

    protected void initRecycleView() {
        mListRv.setLayoutManager(mLayoutManager);
        mListRv.addItemDecoration(mDecoration);
        mListRv.setAdapter(mAdapter);
        if(mPageEnable){
            mPageLoader=new PageLoader(mListRv);
            //如果分页
            mPageLoader.setLoadListener(new PageLoader.OnLoadListener() {
                @Override
                public void onLoad() {
                    onLoadMoreData();
                }
            });
        }
    }

    protected void changeState(int state){
        if(mState==state) return;
        switch (state){
            case STATE_CONTENT:
                mStateView.showContentStatus();
                break;
            case STATE_LOADING:
                mStateView.showLoadingStatus();
                break;
            case STATE_EMPTY:
                mStateView.showEmptyStatus();
                break;
            case STATE_EROOR:
                mStateView.showErrorStatus();
                break;
        }
    }

}
