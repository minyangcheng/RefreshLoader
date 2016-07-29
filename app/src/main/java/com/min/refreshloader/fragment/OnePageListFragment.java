package com.min.refreshloader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.min.refreshloader.R;
import com.min.refreshloader.adapter.AnimalListAdapter;
import com.min.refreshloader.base.BaseFragment;
import com.min.refreshloader.bean.response.AnimalBean;
import com.min.refreshloader.view.refresh.RefreshLoaderDelegate;
import com.min.refreshloader.view.refresh.RefreshLoaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by minyangcheng on 2016/7/29.
 */
public class OnePageListFragment extends BaseFragment{

    @Bind(R.id.rlv)
    RefreshLoaderView mRefreshLoaderView;

    private RefreshLoaderDelegate mRefreshLoaderDelegate;

    private int count=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_one_page,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRefreshLoaderDelegate =new RefreshLoaderDelegate<AnimalBean, AnimalListAdapter.ItemViewHolder, AnimalListAdapter>(mRefreshLoaderView) {
            @Override
            protected void onRefreshData() {
                count=0;
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
        };

    }


}
