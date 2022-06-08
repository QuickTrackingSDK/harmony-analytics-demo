package com.umeng.demo.hm.slice;

import com.umeng.analytics.MobclickAgent;
import com.umeng.demo.hm.MyApplication;
import com.umeng.demo.hm.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;

public class AnalyticsHomeSlice2 extends AbilitySlice {

    private Context appContext = null;
    private static final String PAGE_NAME = "AnalyticsHomeSlice2";
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_analytics_home2);
        appContext = this.getApplicationContext();
        initComponents();
    }

    private void initComponents() {
        Component jump2Analytics = findComponentById(ResourceTable.Id_jump_to_analytics);
        // 同一个Ability内部AbilitySlice之间导航跳转
        jump2Analytics.setClickedListener(listener -> present(new AnalyticsHomeSlice(), new Intent()));
    }

    @Override
    protected void onActive() {
        super.onActive();
        HiLog.info(MyApplication.LABEL, "--->>> AnalyticsHomeSlice2::onActive.");
        MobclickAgent.onPageStart(PAGE_NAME);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.info(MyApplication.LABEL, "--->>> AnalyticsHomeSlice2::onInactive.");
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
}
