##RefreshLoader

####开发时遇到的问题:

1. 下拉刷新上拉加载功能在app内会出现多次出现，该功能的业务逻辑处理包括分页参数、页面为空、页面错误、页面加载中、判断是否加载完毕等等，如果在每个fragment或activity中都进行这一系列的重复处理，代码就会变的非常糟.
2. app内部列表的api接口字段都是大同小异，如果将列表页面的分页请求接口处理逻辑封装起来，后期写代码也会相当舒服。

####解决办法：

* 首先将上拉刷新和状态切换显示放在一起封装成一个控件RefreshLoaderView，之后可以在fragment或activity中直接使用。

```
<com.min.refreshloader.view.StateLayout  xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/view_state">
    <android.support.v4.widget.SwipeRefreshLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/view_refresh">
        <android.support.v7.widget.RecyclerView
            android:id="@+id/rv_list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
    </android.support.v4.widget.SwipeRefreshLayout>
</com.min.refreshloader.view.StateLayout>
```

* 刷新和加载的处理完全由抽象类RefreshLoaderDelegate来实现，该类主要的几个方法：

```
//实现刷新具体请求逻辑
protected abstract void onRefreshData();

//实现加载具体逻辑
protected abstract void onLoadMoreData();

//刷新成功后需主动回调
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

//刷新失败后需主动回调
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

//加载成功后需主动回调
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

//加载失败后需主动回调
protected void setLoadLoadMoreFail(String errorMess){
    if(mPageLoader!=null) mPageLoader.setLoadFail();
}

//你可以重写此方法，实现自己的判断是否加载完毕逻辑
protected void judgeLoadFinally(List<DATATYPE> receiveList){
    if(mPageLoader==null) return;
    if(receiveList==null||receiveList.size()< AppConstants.PAGE_SIZE){
        mPageLoader.setLoadFianlly(true);
    }else{
        mPageLoader.setLoadFianlly(false);
    }
}

//开启加载
public RefactorRefreshLoaderDelegate startLoad(){
    initViews();
    initData();
    return this;
}

```

* 推荐继承RefreshLoaderDelegate，根据具体的分页逻辑实现onRefreshData和onLoadMoreData方法，方便以后直接传递adapter，即可实现分页功能。

####使用方法

```
AnimalListAdapter adapter=new AnimalListAdapter(this);
mRRLoader=new RefreshLoaderDelegate(mRefreshLoaderView,adapter,true) {
    @Override
    protected void onRefreshData() {
        count=0;
        mListRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                List animalBeanList=getFakeData(15);
                setRefreshDataSuccess(animalBeanList);
            }
        },3000);
    }

    @Override
    protected void onLoadMoreData() {
        mListRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<AnimalBean> animalBeanList;
                if(count>=45){
                    animalBeanList=getFakeData(10);
                }else {
                    animalBeanList=getFakeData(15);
                }
                setLoadMoreDataSuccess(animalBeanList);
            }
        },3000);
    }
}.startLoad();
```