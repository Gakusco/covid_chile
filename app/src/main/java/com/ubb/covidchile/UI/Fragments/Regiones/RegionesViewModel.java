package com.ubb.covidchile.UI.Fragments.Regiones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}