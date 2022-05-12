package com.funny.bottomnavigationtest

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.funny.bottomnavigation.FunnyBottomNavigation
import com.funny.bottomnavigationtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var funnyViewModel: FunnyViewModel
    private lateinit var activityMainBinding: ActivityMainBinding
    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        funnyViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FunnyViewModel::class.java
        )
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        with(activityMainBinding){
            data = funnyViewModel
            lifecycleOwner = this@MainActivity

            funnyViewModel.iconIds.value?.let { funnyButtomNavigation.initIconButtons(it) }
            funnyButtomNavigation.setOnItemClickListener { position ->
                Log.i(TAG, "" + position + "isClicked")
                funnyViewModel.setText("第" + position + "页")
            }
        }

        funnyViewModel.setText(
            "起始第" + activityMainBinding.funnyButtomNavigation.startPage.toString() + "页"
        )

        // 开启调试输出，默认关闭
        FunnyBottomNavigation.DEBUG = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this).inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.mene_item_move_with_anim) {
            activityMainBinding.funnyButtomNavigation.moveTo(2, hasAnimation = true, performClick = true)
        } else if (id == R.id.mene_item_move_without_anim) {
            activityMainBinding.funnyButtomNavigation.moveTo(2, hasAnimation = false, performClick = true)
        }
        return super.onContextItemSelected(item)
    }
}