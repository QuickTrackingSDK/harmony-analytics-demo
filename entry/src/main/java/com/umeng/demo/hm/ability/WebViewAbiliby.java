package com.umeng.demo.hm.ability;

import com.umeng.demo.hm.slice.WebViewSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.Context;

public class WebViewAbiliby extends Ability {

    public static final String ACTION_WEBVIEW = "custom.action.webview";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(WebViewSlice.class.getName());
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
