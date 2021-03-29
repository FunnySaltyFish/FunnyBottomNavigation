package com.funny.bottomnavigationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.funny.bottomnavigationtest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    FunnyViewModel funnyViewModel;
    ActivityMainBinding activityMainBinding;

    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.mene_item_move_with_anim){
            activityMainBinding.funnyButtomNavigation.moveTo(2,true,true);
        }else if(id == R.id.mene_item_move_without_anim){
            activityMainBinding.funnyButtomNavigation.moveTo(2,false,true);
        }
        return super.onContextItemSelected(item);
    }

}