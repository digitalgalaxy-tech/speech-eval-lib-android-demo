package com.digitalgalaxy.voice.comparison.demo;

import android.app.Application;

import com.digitalgalaxy.voice.comparison.VoiceComparison;
import com.digitalgalaxy.voice.comparison.VoiceComparisonSDK;

public class App extends Application {
    private boolean setUp = false;

    @Override
    public void onCreate() {
        super.onCreate();
        String email = null;
        String uuid = null;
        if (email != null && uuid != null) {
            VoiceComparisonSDK.setup(this, email, uuid);
            VoiceComparison.setHome(getFilesDir().toString());
            setUp = true;
        }
    }

    public boolean isSetUp() {
        return setUp;
    }
}
