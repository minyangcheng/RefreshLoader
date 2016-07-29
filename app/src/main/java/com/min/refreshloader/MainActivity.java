package com.min.refreshloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.min.refreshloader.base.BaseFragment;
import com.min.refreshloader.fragment.OnePageListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseFragment fragment=new OnePageListFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,fragment)
                .commit();
    }

}
