package com.umeng.demo.hm.ability;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import com.umeng.demo.hm.slice.AnalyticsHomeSlice;

public class AnalyticsAbility extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(AnalyticsHomeSlice.class.getName());
        // set the action route
        super.addActionRoute(MainAbility.ACTION_ANALYTICS_HOME, AnalyticsHomeSlice.class.getName());
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

}
