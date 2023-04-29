package com.example.hw1;

import android.app.Application;

import com.example.hw1.Utilities.MySPv;
import com.example.hw1.Utilities.SignalGenerator;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySPv.init(this);
        SignalGenerator.init(this);
    }
}

    

