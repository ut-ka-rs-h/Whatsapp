package com.example.whatsapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5gCxSC0swvHX8i62d1qKddUj0WEVzyB5IGhynudJ")
                // if defined
                .clientKey("7FyO3YJaDdiMf60Od3tyi5kEmxNVcxMEeqSVba6C")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }

}
