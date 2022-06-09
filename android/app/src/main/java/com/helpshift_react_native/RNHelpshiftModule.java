package com.helpshift_test;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;


import com.helpshift.Helpshift;
import com.helpshift.HelpshiftAuthenticationFailureReason;
import com.helpshift.HelpshiftEvent;
import com.helpshift.HelpshiftEventsListener;
import com.helpshift.UnsupportedOSVersionException;


import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RNHelpshiftModule extends ReactContextBaseJavaModule implements HelpshiftEventsListener {

    //Helpshift local varibales declaration
    private final Application app;
    private ReactApplicationContext mReactContext;
    private Handler countSuccessHandler;
    private Handler countErrorHandler;

    //constructor
    public RNHelpshiftModule(ReactApplicationContext reactContext){
    super(reactContext);
        mReactContext= reactContext;
        this.app = (Application)reactContext.getApplicationContext();
    }

    @ReactMethod
    public void init(String appid, String domain) throws UnsupportedOSVersionException {
        Map<String, Object> installConfig = new HashMap<>();
        Helpshift.install(this.app,appid,domain,installConfig);
    }
    //Mandatory function getName that specifies the module name
    @Override
    public String getName() {
        return "RNHelpshift";
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void onEventOccurred(@NonNull String s, Map<String, Object> map) {

    }


    @Override
    public void onUserAuthenticationFailure(HelpshiftAuthenticationFailureReason helpshiftAuthenticationFailureReason) {

    }

    @ReactMethod
    public void showConversation() {
        final Activity activity = getCurrentActivity();
        Map<String, Object> config = new HashMap<>();
        Helpshift.showConversation(activity,config);
    }

    @ReactMethod
    public void showFAQs(){
        final Activity activity = getCurrentActivity();
        Map<String, Object> config = new HashMap<>();
        Helpshift.showFAQs(activity,config);
    }

    @ReactMethod
    public void showFAQbyId(){
        final Activity activity = getCurrentActivity();
        Map<String, Object> config = new HashMap<>();
        Helpshift.showSingleFAQ(activity,"3",config);
    }

}
