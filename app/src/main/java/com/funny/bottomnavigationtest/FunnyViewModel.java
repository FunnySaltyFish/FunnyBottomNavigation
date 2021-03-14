package com.funny.bottomnavigationtest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.funny.bottomnavigation.bean.IconButton;

import java.util.ArrayList;

public class FunnyViewModel extends ViewModel {
    MutableLiveData<int[]> iconIds;
    MutableLiveData<String> text;

    public LiveData<int[]> getIconIds(){
        if (iconIds == null){
            int[] ids = new int[]{
                    R.drawable.ic_favorites,
                    R.drawable.ic_bin,
                    R.drawable.ic_run,
                    R.drawable.ic_favorites
            };
            iconIds = new MutableLiveData<>();
            iconIds.setValue(ids);
        }
        return iconIds;
    }

    public MutableLiveData<String> getText() {
        if(text == null){
            text = new MutableLiveData<>();
            text.setValue("默认");
        }
        return text;
    }

    public void setText(String text) {
        getText().setValue(text);
    }
}
