package com.ubb.covidchile.UI.Fragments.PorRegion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PorRegionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PorRegionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}