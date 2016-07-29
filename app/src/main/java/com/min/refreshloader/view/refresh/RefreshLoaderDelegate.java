package com.min.refreshloader.view.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.min.refreshloader.R;
import com.min.refreshloader.base.HFRecyclerViewAdapter;
import com.min.refreshloader.base.PageLoader;
import com.min.refreshloader.bean.api.BaseApi;
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
public abstract class RefreshLoaderDelegate<DATATYPE
        ,HOLDER extends RecyclerView.ViewHolder
        ,ADAPTER extends HFRecyclerViewAdapter<DATATYPE,HOLDER>>{

    protected static final int STATE_CONTENT=1;
    protected static final int STATE_EMPTY=2;
    protected static final int STATE_EROOR=3;
    protected static final int STATE_LOADING=4;

    private Context mContext;

    @Bind(R.id.view_state)
    protected StateLayout mStateView;
    @Bind(R.id.view_refresh)
    protected SwipeRefreshLayout mRefreshView;
    @Bind(R.id.rv_list)
    protected RecyclerView mListRv;

    protected ADAPTER mAdapter;
    protected DividerItemDecoration mDecoration;
    protected PageLoader mPageLoader;

    protected int mNextPage=0;
    protected int mState;

    public RefreshLoaderDelegate(RefreshLoaderView refreshLoaderView){
        mContext=refreshLoaderView.getContext();
        baseInit(refreshLoaderView);
    }

    /**
     * 初始化
     * @param view
     */
    private void baseInit(View view){
        ButterKnife.bind(this, view);
        initViews();

        initData();
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
        mAdapter=getRecycleViewAdapter();
        mDecoration=getItemDecoration();
        initRecycleView();
    }

    protected abstract void onRefreshData();

    protected abstract void onLoadMoreData();

    protected boolean getPaginationEnable(){
        return false;
    }

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

        if(getPaginationEnable()){
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
        if(receiveList==null||receiveList.size()< BaseApi.PAGE_SIZE){
            mPageLoader.setLoadFianlly(true);
        }else{
            mPageLoader.setLoadFianlly(false);
        }
    }

    protected void initRecycleView() {
        LinearLayoutManager lm=new LinearLayoutManager(mContext);
        mListRv.setLayoutManager(lm);
        mListRv.addItemDecoration(mDecoration);
        mListRv.setAdapter(mAdapter);
        if(getPaginationEnable()){
            mPageLoader=new PageLoader(mListRv);
            //如果不分页
            mPageLoader.setLoadListener(new PageLoader.OnLoadListener() {
                @Override
                public void onLoad() {
                    onLoadMoreData();
                }
            });
        }
    }

    protected abstract ADAPTER getRecycleViewAdapter();

    protected DividerItemDecoration getItemDecoration(){
        DividerItemDecoration decoration=new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST
                ,R.drawable.shape_line);
        return decoration;
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
