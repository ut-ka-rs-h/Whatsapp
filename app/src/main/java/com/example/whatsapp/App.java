package com.example.whatsapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.livequery.ParseLiveQueryClient;

import java.net.URI;
import java.net.URISyntaxException;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("wUE0aE0IVhQFzMQs5DNABeZ8sdoqaBBU74fGYnuI")
                // if defined
                .clientKey("RURWl2QVeH1bMneCrf4QAwh9WGWHao8HYSWUYvkI")
                .server("https://parseapi.back4app.com/")
                .build()
        );


    }

}
