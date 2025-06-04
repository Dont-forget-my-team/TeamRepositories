package com.example.gamepractice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StyleViewModel extends ViewModel {

    private final MutableLiveData<String> selectedWindowStyle = new MutableLiveData<>();
    private final MutableLiveData<String> selectedSofaStyle = new MutableLiveData<>();
    private final MutableLiveData<String> selectedCarpetStyle = new MutableLiveData<>();
    private final MutableLiveData<String> selectedWallHangerStyle = new MutableLiveData<>();
    private final MutableLiveData<String> selectedWallpaperStyle = new MutableLiveData<>();

    // 창문
    public void setSelectedWindowStyle(String styleKey) {
        selectedWindowStyle.setValue(styleKey);
    }
    public LiveData<String> getSelectedWindowStyle() {
        return selectedWindowStyle;
    }

    // 소파
    public void setSelectedSofaStyle(String styleKey) {
        selectedSofaStyle.setValue(styleKey);
    }
    public LiveData<String> getSelectedSofaStyle() {
        return selectedSofaStyle;
    }

    // 카펫
    public void setSelectedCarpetStyle(String styleKey) {
        selectedCarpetStyle.setValue(styleKey);
    }
    public LiveData<String> getSelectedCarpetStyle() {
        return selectedCarpetStyle;
    }

    // 벽걸이
    public void setSelectedWallHangerStyle(String styleKey) {
        selectedWallHangerStyle.setValue(styleKey);
    }
    public LiveData<String> getSelectedWallHangerStyle() {
        return selectedWallHangerStyle;
    }

    // 벽지
    public void setSelectedWallpaperStyle(String styleKey) {
        selectedWallpaperStyle.setValue(styleKey);
    }
    public LiveData<String> getSelectedWallpaperStyle() {
        return selectedWallpaperStyle;
    }
}



