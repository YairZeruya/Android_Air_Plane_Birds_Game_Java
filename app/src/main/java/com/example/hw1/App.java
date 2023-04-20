package com.example.hw1;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class App extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        SignalGenerator.init(this);
    }
}

    

