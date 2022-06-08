package com.umeng.demo.hm.ability;

import com.umeng.demo.hm.slice.SubProcessSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class SubProcessAbility extends Ability {
    public static final String ACTION_SUBPROCESS_SLICE = "custom.action.subprocess_slice";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SubProcessSlice.class.getName());
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
