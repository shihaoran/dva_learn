package com.dvastarter;

import android.app.Application;

import com.facebook.react.ReactApplication;
import com.learnium.RNDeviceInfo.RNDeviceInfo;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.facebook.react.modules.UuapAdapter.UuapReactPackage;

import com.baidu.bpit.uuap.LoginTheme;
import com.baidu.bpit.uuap.MopConfig;
import com.baidu.bpit.uuap.UuapSessionManager;
import com.baidu.bpit.uuap.IUuapSessionManager;
import com.baidu.bpit.uuap.exceptions.UuapAuthException;
import com.baidu.bpit.uuap.models.Token;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  public static String uuapAppKey = "uuapclient-35-N2dzyodwt4o3pYFHo7or";
  public static String mopAppKey = "6a8371098a9a4b4ba0f512f4ce3445bf";


  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
            new RNDeviceInfo(),
          new UuapReactPackage()
      );
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);

    MopConfig config = new MopConfig.Builder(this, uuapAppKey, mopAppKey)
            // 测试地址
            //.useDebugBaseUrl("http://bjkjy-art-test01.bjkjy.baidu.com:8280/api_proxy/system/sdk/1.0")
            // 测试密钥
            //.useCustomPublicKeyPath("file:///android_asset/publicKey-test")
            .create();
    UuapSessionManager.init(config);

    // 配置自定义界面
    if (false) {
        LoginTheme.getDefault().readFrom("{"
                + "'bg_color':'#ffaaaa', "
                + "'logo_uri':'drawable:new_message', "
                + "'btn_bg_color':'#ff0000', "
                + "'btn_bg_color_pressed':'#00ff00', "
                + "'btn_text_color':'#0000ff'"
                + "}");
    }
  }
}
