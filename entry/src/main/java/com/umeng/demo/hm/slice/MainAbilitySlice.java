package com.umeng.demo.hm.slice;

import com.umeng.analytics.MobclickAgent;
import com.umeng.demo.hm.ability.AnalyticsAbility;
import com.umeng.demo.hm.ability.MainAbility;
import com.umeng.demo.hm.MyApplication;
import com.umeng.demo.hm.ResourceTable;
import com.umeng.demo.hm.ability.WebViewAbiliby;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;

import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.multimodalinput.event.KeyEvent;
import ohos.os.ProcessManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainAbilitySlice extends AbilitySlice {

    private Context appContext = null;
    private static final String PAGE_NAME = "MainSlice";

    private static boolean backClicked = false;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        appContext = this.getApplicationContext();
        initComponents();
    }

    private void initComponents() {
        Component analyticsHome = findComponentById(ResourceTable.Id_analytics_entry);
        analyticsHome.setClickedListener(this::startAnalytics);

        Component jumptest = findComponentById(ResourceTable.Id_jump_test);
        jumptest.setClickedListener(this::jumpTest);

        Component jsBridgeTest = findComponentById(ResourceTable.Id_webview_entry);
        jsBridgeTest.setClickedListener(this::startH5TestAbility);

    }

    private void startAnalytics(Component component) {
        // 跳转到统计分析页
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(appContext.getBundleName())
                .withAbilityName(AnalyticsAbility.class)
                .withAction(MainAbility.ACTION_ANALYTICS_HOME)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    private void jumpTest(Component component) {
        // 跳转到测试Slice页面
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(appContext.getBundleName())
                .withAbilityName(MainAbility.class)
                .withAction(MainAbility.ACTION_ANALYTICS_HOME2)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    private void startH5TestAbility(Component component) {
        // 跳转到H5桥接页面
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(appContext.getBundleName())
                .withAbilityName(WebViewAbiliby.class)
                .withAction(MainAbility.ACTION_WEBVIEW)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    @Override
    public void onActive() {
        super.onActive();
        appContext = this.getApplicationContext();
        HiLog.info(MyApplication.LABEL, "--->>> MainAbilitySlice::onActive.");
        Map<String, Object> args = new HashMap<>();
        args.put("p1", "value1");
        args.put("p2", 1350);
        args.put("p3", 13.875);
        MobclickAgent.onPageStart(PAGE_NAME, args);

    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.info(MyApplication.LABEL, "--->>> MainAbilitySlice::onInactive.");
        MobclickAgent.onPageEnd(PAGE_NAME);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEY_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    private void back() {
        Timer timer = null;
        if (!backClicked) {
            backClicked = true;
            new ToastDialog(appContext)
                    .setText("再点一次退出应用")
                    .show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backClicked = false;
                }
            }, 2000);//设置在2两秒内再次点击back键能够退出程序
        } else {
            MobclickAgent.onKillProcess(appContext);
            ProcessManager.kill(ProcessManager.getPid());
        }
    }
}
