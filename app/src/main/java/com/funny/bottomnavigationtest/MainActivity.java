package com.funny.bottomnavigationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.funny.bottomnavigationtest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    FunnyViewModel funnyViewModel;

    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        funnyViewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(FunnyViewModel.class);
        activityMainBinding.setData(funnyViewModel);
        activityMainBinding.setLifecycleOwner(this);

        activityMainBinding.funnyButtomNavigation.initIconButtons(funnyViewModel.getIconIds().getValue());
        activityMainBinding.funnyButtomNavigation.setOnItemClickListener(position -> {
            Log.i(TAG,""+position+"isClicked");
            funnyViewModel.setText("第"+position+"页");
        });

        funnyViewModel.setText("起始第"+activityMainBinding.funnyButtomNavigation.getStartPage()+"页");
    }
}