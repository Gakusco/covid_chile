package com.ubb.covidchile.UI.Fragments.Nacional;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NacionalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NacionalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}