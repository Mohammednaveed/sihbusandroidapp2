package com.example.finalbustraking;

import android.app.Application;

import com.mappls.sdk.maps.Mappls;
import com.mappls.sdk.services.account.MapplsAccountManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MapplsAccountManager.getInstance().setRestAPIKey("e0369e41d2449f22c5b27c74b80b867b");
        MapplsAccountManager.getInstance().setMapSDKKey("e0369e41d2449f22c5b27c74b80b867b");
        MapplsAccountManager.getInstance().setAtlasClientId("33OkryzDZsJSnnTx0xABAaFbEOi8sECUAshbdACZpK0MNi4dZ_wgrdER8HPVu0I5pdTCDl638RCP1ev-w-dyE-FKltmr9eaJ");
        MapplsAccountManager.getInstance().setAtlasClientSecret("lrFxI-iSEg8-T7D0IIqRjkpd4rOLzd9idJGDpA2DHrMQZ7-syvBglksLDhkUfKDZvSjCGRhCWsvjqUh-IIpcTWpG6j4Ygy6g9FB8S1_PK2c=");

        Mappls.getInstance(getApplicationContext());
    }
}
