package com.helpshift_test;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;

import com.helpshift.Helpshift;
import com.helpshift.HelpshiftAuthenticationFailureReason;
import com.helpshift.HelpshiftEventsListener;

import com.helpshift.UnsupportedOSVersionException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class RNHelpshiftView extends ViewGroupManager<ReactViewGroup> implements HelpshiftEventsListener {

    public static final String REACT_CLASS = "RNTHelpshift";

    private ThemedReactContext mReactContext;
    private Application mApplication;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ReactViewGroup createViewInstance(ThemedReactContext context)  {
        final ReactViewGroup reactView = new ReactViewGroup(context);

        mReactContext = context;

        mApplication = (Application)context.getApplicationContext();

        return reactView;
    }

    @ReactProp(name = "config")
    public void setConfig(final ReactViewGroup reactView, ReadableMap config) {
        Map<String, Object> installConfig = new HashMap<>();
        //InstallConfig installConfig = new InstallConfig.Builder().build();
        try {
            Helpshift.install( mApplication, config.getString("appId"), config.getString("domain"), installConfig   );
            //Core.install(mApplication,  config.getString("apiKey"),  config.getString("domain"),  config.getString("appId") , installConfig);
        } catch (UnsupportedOSVersionException e) {
            Log.e("Helpshift", "invalid install credentials : ", e); // Android OS versions prior to Lollipop (< SDK 21) are not supported.
        }

        if (config.hasKey("user")) {
            this.login(config.getMap("user"));
        }

        Activity activity = mReactContext.getCurrentActivity();
        final FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
        final Fragment helpshiftFragment = null;
        Map<String, Object> extras = new HashMap<>();
        extras.put("enableDefaultConversationalFiling", true);

      /*  if (config.hasKey("cifs")) {
            ApiConfig apiConfig = new ApiConfig.Builder().setExtras(extras).setCustomIssueFields(getCustomIssueFields(config.getMap("cifs"))).build();
            helpshiftFragment = Support.getConversationFragment(activity, apiConfig);
        } else {
            ApiConfig apiConfig = new ApiConfig.Builder().setExtras(extras).build();
            helpshiftFragment = Support.getConversationFragment(activity, apiConfig);
        }
    */
        reactView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fragmentManager.executePendingTransactions();
                for (int i = 0; i < reactView.getChildCount(); i++) {
                    View child = reactView.getChildAt(i);
                    child.measure(View.MeasureSpec.makeMeasureSpec(reactView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(reactView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
                    child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
                }
            }
        });

        fragmentManager.beginTransaction().replace(reactView.getId(), helpshiftFragment).commit();
    }


    private void login(ReadableMap user){
        Map<String, String> userData;
        String email = user.hasKey("email") ? user.getString("email") : null;
        String identifier = user.hasKey("identifier") ? user.getString("identifier") : null;
        if(user.hasKey("name") && user.hasKey("authToken")) {
            userData  = new HashMap<>();
            userData.put("userId",user.getString("identifier"));
            userData.put("userEmail",user.getString("email"));
            userData.put("userAuthToken",user.getString("authToken"));

        } else if (user.hasKey("name")) {
            userData = new HashMap<>();
                   userData.put("name",user.getString("name"));
        } else if (user.hasKey("authToken")) {
            userData = new HashMap<>();
                    userData.put("authToken",user.getString("authToken"));
        } else {
            userData = new HashMap<>();
        }
        Helpshift.login(userData);
    }

    private Map<String, String[]> getCustomIssueFields(ReadableMap cifs) {
        ReadableMapKeySetIterator iterator = cifs.keySetIterator();
        Map<String, String[]> customIssueFields = new HashMap<>();

        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableArray array = cifs.getArray(key);
            customIssueFields.put(key, new String[]{array.getString(0), array.getString(1)});
        }

        return customIssueFields;
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
}