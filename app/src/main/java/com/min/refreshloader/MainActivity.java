package com.min.refreshloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.min.refreshloader.adapter.AnimalListAdapter;
import com.min.refreshloader.bean.AnimalBean;
import com.min.refreshloader.util.UIUtils;
import com.min.refreshloader.view.divider.DividerItemDecoration;
import com.min.refreshloader.view.refresh.RefreshLoaderDelegate;
import com.min.refreshloader.view.refresh.RefreshLoaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rlv)
    RefreshLoaderView mRefreshLoaderView;

    private RefreshLoaderDelegate<AnimalBean, AnimalListAdapter.ItemViewHolder, AnimalListAdapter> mRRLoader;

    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
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
    }

    @OnClick(R.id.btn_retry)
    void onClickBtnRetry(){
        UIUtils.toast(this, "重新加载页面");
        initData();
    }

    public List<AnimalBean> getFakeData(int num){
        List<AnimalBean> animalBeanList=new ArrayList();
        for (int i=0;i<num;i++){
            AnimalBean bean=new AnimalBean();
            bean.setName("animal_"+count);
            bean.setAge(count);
            animalBeanList.add(bean);
            count++;
        }
        return animalBeanList;
    }

}
