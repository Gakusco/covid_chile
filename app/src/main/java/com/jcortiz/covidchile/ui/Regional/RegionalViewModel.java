package com.jcortiz.covidchile.ui.Regional;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegionalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegionalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}