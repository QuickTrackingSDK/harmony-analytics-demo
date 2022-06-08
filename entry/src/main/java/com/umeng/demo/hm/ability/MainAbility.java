package com.umeng.demo.hm.ability;

import com.umeng.demo.hm.MyApplication;
import com.umeng.demo.hm.slice.AnalyticsHomeSlice;
import com.umeng.demo.hm.slice.AnalyticsHomeSlice2;
import com.umeng.demo.hm.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;

public class MainAbility extends Ability {
    public static final String ACTION_ANALYTICS_HOME = "custom.action.analytics_home";
    public static final String ACTION_ANALYTICS_HOME2 = "custom.action.analytics_home2";
    public static final String ACTION_WEBVIEW = "custom.action.webview";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        super.addActionRoute(MainAbility.ACTION_ANALYTICS_HOME2, AnalyticsHomeSlice2.class.getName());
    }

    @Override
    protected void onActive() {
        super.onActive();
        HiLog.info(MyApplication.LABEL, "MainAbility: onActive enter...");
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.info(MyApplication.LABEL, "MainAbility: onInactive enter...");
    }
}
