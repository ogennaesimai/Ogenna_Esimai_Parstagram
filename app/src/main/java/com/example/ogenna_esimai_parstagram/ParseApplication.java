package com.example.ogenna_esimai_parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        // set applicationId, and server server based on the values in the back4app settings.
        //ClientKey is not needed unless configured explicitly
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hi2iXY6nnfQ4bCcvlLqRACSJl44EhkdTNAsZtQO6")
                .clientKey("pqJYTnkViIwpc2CXtmx6sxpGTw1uklCH0aYgnw2O")
                .server("https://parseapi.back4app.com").build()
        );
    }
}
