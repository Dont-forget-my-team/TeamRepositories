// StyleViewModel.java
package com.example.gamepractice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StyleViewModel extends ViewModel {

    private final MutableLiveData<String> selectedWindowStyle = new MutableLiveData<>();
    private final MutableLiveData<String> selectedSofaStyle = new MutableLiveData<>();

    public void setSelectedWindowStyle(String styleKey) {
        selectedWindowStyle.setValue(styleKey);
    }

    public LiveData<String> getSelectedWindowStyle() {
        return selectedWindowStyle;
    }

    public void setSelectedSofaStyle(String styleKey) {
        selectedSofaStyle.setValue(styleKey);
    }

    public LiveData<String> getSelectedSofaStyle() {
        return selectedSofaStyle;
    }
}

