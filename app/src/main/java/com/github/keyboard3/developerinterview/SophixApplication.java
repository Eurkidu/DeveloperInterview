package com.github.keyboard3.developerinterview;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.github.keyboard3.developerinterview.hotRepair.HostCallbacks;
import com.github.keyboard3.developerinterview.utils.VersionUtil;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginConfig;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Created by keyboard3 on 2017/9/5.
 */

public class SophixApplication extends Application {
    private static String TAG = "DIApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        sophixInit(this);
        RePlugin.App.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RePluginConfig c = new RePluginConfig();
        c.setCallbacks(new HostCallbacks(base));
        RePlugin.App.attachBaseContext(this, c);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        RePlugin.App.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        RePlugin.App.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RePlugin.App.onConfigurationChanged(newConfig);
    }

    private static void sophixInit(SophixApplication application) {
        String appVersion = VersionUtil.getVersion(application);
        SophixManager.getInstance().setContext(application)
                .setAppVersion(appVersion)
                //.setAesKey(null)
                .setEnableDebug(true)
                .setSecretMetaData("24614018-1", "2697eb93fd779a418e51548c885c3105",
                        "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCDNqBnRui2NKwSuRLjZQmddjxl+Qi9bVqryLQZ6S402sSU3OQds//NXO7lmhbwHM0Qu8MggUEDCHzwJ62w/e1WW91EwAhAbUETP5ZgTLbc0Awr9ZlIau26IkbKF3AcBx94/HxbXaOZXd/9nvSwEchEP4k8nfApXoMxfN5DUl3nYQmt7b+raYb6DL2vFXeGuHHAgtAt/mSb0JqHwmBSZO4H0l9jdCf5+bSPliEh3MVCkFlODhxl4RXveSnEdo9xC/2qNFuInQ31O5R0UFam/gSwGHiGj3FPXrm3kNIK5Kyd7GpXyH6PmI2YGGsJyac0fxHdm1pItoQXnvowis38VX0RAgMBAAECggEANLvonXk3H7ttiJzPBYre4WTag1Uh+ReRAEdKcrtvcZiWtwlLuNBVt1BpRue13kyE8fu0QUURPsnThKU9vktfbny/IdlXt6TxKW493ngWagpHSgeL0jn/TvZlouTmjq0iLfqzc/jfPk0nL4QJ7RVIen1ah30mP0oXyfTYUxAwY0PX3wKEcIyGxuH1QWzaYoc8fi1g4OfvLZekx/AzSm6fxsjL+JrCJZh4ApY8rIJLG2FZmYSHPO1tNKybKKrh0c/lp90dwQcPWmG/BlW7Mk+krSKiaeIyZOqywKVfl9BPzTHDWVUQmzUYYcZ/ZeXRfCp9oxrqzVGQ/HY9uon3h/e6HQKBgQD2G+3L5cpAhouyIpsExUP3zxwR4LluKYHiOgZaMwn7WIFamM4RG+xeRECBSbUcluGqe5xf7nrnh1ewB3IJquBxV+gh+xCeh7Uy3TxKRMP/hOLw4gx4+0nylWgjpK6SSckgT22MuN8fJ7UFmBPyZdOuqemIEFzUY23RwwW1TV9WiwKBgQCIfJqEE15CP5he0jHYkpxsSH0Y9j5S0JjvESTMD+vwvKpXtr77sroEKiFoulsAKIscKDyGUyOkCj4wN8sHBCxPuSGqyuY/sQGFvGTA2oaKluqx1GIxhDX7joKGetFEfJMsiIL4SjGsp4IgnfhPTrak/yVgWFLcP2utPvUD04EKUwKBgHMSnf5vZEEFZ/4Tpi312pDc+v/09l7m838GqH+2S52FRX5J4lgnmT5+ZTbOcut6NOvUvkowpLDrHHoHETAqAKWed8CjtqKZS4UL4qvLOWWZCc+dsj1DKdFOQJIh5yopa+w6lztsGY9kroR+Fh2JtQ9/DN184RrBntCsglfCZKh/AoGAfdpf5G+zl+TqTG0H14sBP/vrlmvhdh2xF6i4RI5d4mMls7HoE1Fvo01x9w73wIKiG3mirb3V/HoRdAbOaI7ZiT3NpJ5ph8tg+v+H7CjUrNCSYx3S2ZPu7yIui3COrNcuIn9SPT605V6kaA0iHYcYdbZrsZZf+YVldZ+68CdEMs8CgYBYtCrMpEDUkBYmqDec2kA3xvLp3VLCnJCT89tFBQEqC+z2jdSntnqgpAThMeofjaM2xXFC7GoPJLuAhMRIPyiqYhitwv8vIPaauJ/nCd8h7qGIuY2xs0tooKav+LFbGrf3Agm9ob2v4Hl9RsYA04A0B/88Lkyl84Kk4N0qiTULJQ=="
                )
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.d(TAG, code + "：表明补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            Log.d(TAG, code + "：表明新补丁生效需要重启. 开发者可提示用户或者强制重启");
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.d(TAG, code + "：其它错误信息, 查看PatchStatus类说明");
                        }
                    }
                }).initialize();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
