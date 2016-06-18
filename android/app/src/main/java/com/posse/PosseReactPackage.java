package com.posse;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.posse.reactmodules.FacebookLogin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PosseReactPackage implements ReactPackage {

    private Activity mActivity = null;

    public PosseReactPackage(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new FacebookLogin(reactContext, mActivity));

        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
