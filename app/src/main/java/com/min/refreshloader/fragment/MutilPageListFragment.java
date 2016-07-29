package com.min.refreshloader.fragment;

import com.min.refreshloader.adapter.AnimalListAdapter;
import com.min.refreshloader.base.BaseRLFragment_;
import com.min.refreshloader.bean.response.AnimalBean;

import java.util.ArrayList;
import java.util.List;

public class MutilPageListFragment extends BaseRLFragment_<AnimalBean,
        AnimalListAdapter.ItemViewHolder,
        AnimalListAdapter>{

    private int count=0;

    @Override
    protected void onRefreshData() {
        mListRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<AnimalBean> animalBeanList=new ArrayList<AnimalBean>();
                for (int i=0;i<15;i++){
                    AnimalBean bean=new AnimalBean();
                    bean.setName("animal_"+count);
                    bean.setAge(count);
                    animalBeanList.add(bean);
                    count++;
                }
                setRefreshDataSuccess(animalBeanList);
            }
        },3000);
    }

    @Override
    protected void onLoadMoreData() {
        mListRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<AnimalBean> animalBeanList=new ArrayList<AnimalBean>();
                for (int i=0;i<15;i++){
                    AnimalBean bean=new AnimalBean();
                    bean.setName("animal_"+count);
                    bean.setAge(count);
                    animalBeanList.add(bean);
                    count++;
                }
                setLoadMoreDataSuccess(animalBeanList);
            }
        },3000);
    }

    @Override
    protected AnimalListAdapter getRecycleViewAdapter() {
        return new AnimalListAdapter(mContext);
    }

    @Override
    protected boolean getPaginationEnable() {
        return true;
    }
}
