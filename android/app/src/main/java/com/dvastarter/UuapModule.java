package com.facebook.react.modules.UuapAdapter;

import com.baidu.bpit.android.pin.PinManager;
import com.baidu.bpit.push.IPushErrorCallback;
import com.baidu.bpit.push.IPushManager;
import com.baidu.bpit.push.PushConfig;
import com.baidu.bpit.push.PushManager;
import com.baidu.bpit.push.exceptions.PushSyncFatalException;
import com.baidu.bpit.uuap.LoginTheme;
import com.baidu.bpit.uuap.MopConfig;
import com.baidu.bpit.uuap.UuapSessionManager;
import com.baidu.bpit.uuap.IUuapSessionManager;
import com.baidu.bpit.uuap.exceptions.UuapAuthException;
import com.baidu.bpit.uuap.models.Token;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;

import android.app.Activity;
import android.content.Intent;

import java.util.Map;
import java.util.HashMap;

public class UuapModule extends ReactContextBaseJavaModule {

    public static final String SERVICE_KEY = "uuapclient-35-N2dzyodwt4o3pYFHo7or";

    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_LOGIN_CANCELED = "E_LOGIN_CANCELED";
    private static final String E_LOGOUT_FAILED = "E_LOGOUT_FAILED";
    private static final String E_GETTOKEN_FAILED = "E_GETTOKEN_FAILED";
    private static final String E_FORCEUPDATE_FAILED = "E_FORCEUPDATE_FAILED";
    private static final String E_GETTICKET_FAILED = "E_GETTICKET_FAILED";

    private static final int REQ_LOGIN = 1;


    private static final int SERVICE_TYPE_AUTH = 0;
    private static final int SERVICE_TYPE_SSO = 1;
    
    private Promise mLoginPromise;
    private Promise mLogoutPromise;
    private Promise mGetTokenPromise;
    private Promise mForceUpdatePromise;
    private Promise mGetTicketPromise;

    public UuapModule(ReactApplicationContext reactContext) {
        super(reactContext);

        // Add the listener for `onActivityResult`
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == REQ_LOGIN) {
                if (mLoginPromise != null) {
                    if (intent != null) {
                        WritableMap map = Arguments.createMap();
                        map.putBoolean("status",true);
                        map.putString("username",intent.getStringExtra("username"));
                        mLoginPromise.resolve(map);
                    } else {
                        WritableMap map = Arguments.createMap();
                        map.putBoolean("status",false);
                        map.putString("username","");
                        mLoginPromise.resolve(map);
                    }
                    mLoginPromise = null;
                }
            }
        }
    };

    @Override
    public String getName() {
        return "UuapAdapter";
    }

    /*
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("uuapAppKey", uuapAppKey);
        constants.put("mopAppKey", mopAppKey);
        return constants;
    }
    */

    @ReactMethod
    public void login(final Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        mLoginPromise = promise;

        UuapSessionManager.getDefault().login(currentActivity, REQ_LOGIN);
    }

    @ReactMethod
    public void logout(final Promise promise) {
        mLogoutPromise = promise;

        try {
            UuapSessionManager.getDefault().logout(null);
            mLogoutPromise.resolve("ok");
        } catch (UuapAuthException e) {
            mLogoutPromise.reject(E_LOGOUT_FAILED, "Log out failed");
        }

        mLogoutPromise = null;
    }

    @ReactMethod
    public void getToken(final Promise promise) {
        mGetTokenPromise = promise;
        try {
            Token token = UuapSessionManager.getDefault().getToken();
            Object o = UuapSessionManager.getDefault().getToken();
            if (o == null) {
                mGetTokenPromise.reject(E_GETTOKEN_FAILED, "Get token failed");
            }
            else if (o instanceof UuapAuthException) {
                WritableMap map = Arguments.createMap();
                map.putBoolean("status", false);
                map.putString("token", "");
                map.putString("message", ((UuapAuthException) o).getLocalizedMessage());
                mGetTokenPromise.resolve(map);
            }
            else if (o instanceof Token) {
                WritableMap map = Arguments.createMap();
                map.putBoolean("status",true);
                map.putString("token", ((Token) o).getId());
                map.putString("message", "");
                mGetTokenPromise.resolve(map);
            }
        } catch (UuapAuthException e) {
            mGetTokenPromise.reject(E_GETTOKEN_FAILED, "Get token failed");
        }
        mGetTokenPromise = null;
    }

    @ReactMethod
    public void forceUpdateToken(final Promise promise) {
        mForceUpdatePromise = promise;
        try {
            UuapSessionManager.getDefault().invalidateToken(null);
            Object o = UuapSessionManager.getDefault().getToken();
            if (o == null) {
                mForceUpdatePromise.reject(E_FORCEUPDATE_FAILED, "Force update token failed");
            }
            else if (o instanceof UuapAuthException) {
                WritableMap map = Arguments.createMap();
                map.putBoolean("status", false);
                map.putString("token", "");
                map.putString("message", ((UuapAuthException) o).getLocalizedMessage());
                mForceUpdatePromise.resolve(map);
            }
            else if (o instanceof Token) {
                WritableMap map = Arguments.createMap();
                map.putBoolean("status",true);
                map.putString("token", ((Token) o).getId());
                map.putString("message", "");
                mForceUpdatePromise.resolve(map);
            }
        } catch (UuapAuthException e) {
            mForceUpdatePromise.reject(E_FORCEUPDATE_FAILED, "Force update token failed");
        }
        mForceUpdatePromise = null;
    }

    @ReactMethod
    public void getTicket(final Promise promise) {
        mGetTicketPromise = promise;
        try {
            Object o = UuapSessionManager.getDefault().getTokenForService(SERVICE_KEY, SERVICE_TYPE_AUTH);
            if (o == null) {
                mGetTicketPromise.reject(E_GETTICKET_FAILED, "Get ticket failed");
            }
            else if (o instanceof Exception) {
                WritableMap map = Arguments.createMap();
                map.putBoolean("status", false);
                map.putString("ticket", "");
                map.putString("message", ((Exception) o).getLocalizedMessage());
                mGetTicketPromise.resolve(map);
            }
            else if (o instanceof Token) {
                WritableMap map = Arguments.createMap();
                map.putBoolean("status",true);
                map.putString("ticket", ((Token) o).getId());
                map.putString("message", "");
                mGetTicketPromise.resolve(map);
            }
        } catch (UuapAuthException e) {
            mGetTicketPromise.reject(E_GETTICKET_FAILED, "Get ticket failed");
        }
        mGetTicketPromise = null;
    }

}
