package com.funny.bottomnavigationtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FunnyViewModel : ViewModel() {
    val iconIds : MutableLiveData<IntArray> by lazy {
        val ids  = intArrayOf(
            R.drawable.ic_favorites,
            R.drawable.ic_bin,
            R.drawable.ic_run,
            R.drawable.ic_favorites)
        MutableLiveData(ids)
    }

    val text by lazy {
        MutableLiveData("测试")
    }

    fun setText(textString: String) {
        text.value = textString
    }
}