package com.umeng.demo.hm.slice;

import com.umeng.analytics.MobclickAgent;
import com.umeng.demo.hm.MyApplication;
import com.umeng.demo.hm.ResourceTable;

import com.umeng.demo.hm.ability.SubProcessAbility;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;

import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.multimodalinput.event.KeyEvent;
import ohos.os.ProcessManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;

public class AnalyticsHomeSlice extends AbilitySlice {

    private Context appContext = null;
    private static final String PAGE_NAME = "AnalyticsHomeSlice";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_analytics_home);
        appContext = this.getApplicationContext();
        initComponents();
    }

    private void initComponents() {
        // 账号接口测试
        Component signInDefault = findComponentById(ResourceTable.Id_account_signin_defalut);
        signInDefault.setClickedListener(this::handleSignInDefault);
        Component signIn = findComponentById(ResourceTable.Id_account_signin);
        signIn.setClickedListener(this::handleSignIn);
        Component signOut = findComponentById(ResourceTable.Id_account_signout);
        signOut.setClickedListener(this::handleSignOut);

        // 事件接口测试
        Component normalEvent = findComponentById(ResourceTable.Id_event);
        normalEvent.setClickedListener(this::handleNormalEvent);
        Component normalEventWithPage = findComponentById(ResourceTable.Id_event_with_page);
        normalEventWithPage.setClickedListener(this::handleNormalEventWithPage);
        Component kvEvent = findComponentById(ResourceTable.Id_kv_event);
        kvEvent.setClickedListener(this::handleKvEvent);

        Component kvEventWithPage = findComponentById(ResourceTable.Id_kv_event_with_page);
        kvEventWithPage.setClickedListener(this::handleKvEventWithPage);

        Component eventMore2k = findComponentById(ResourceTable.Id_event_more_2000);
        eventMore2k.setClickedListener(this::handleEventMore2k);

        Component threadEvent = findComponentById(ResourceTable.Id_thread_event);
        threadEvent.setClickedListener(this::handleEventInSubThread);

        // 全局属性和超级属性 接口测试
        Component setGP = findComponentById(ResourceTable.Id_set_gp);
        setGP.setClickedListener(this::handleSetGlobalProperties);
        Component clearGP = findComponentById(ResourceTable.Id_clear_gp);
        clearGP.setClickedListener(this::handleClearGlobalProperties);

        Component setSP = findComponentById(ResourceTable.Id_set_sp);
        setSP.setClickedListener(this::handleSetSuperProperties);
        Component clearSP = findComponentById(ResourceTable.Id_clear_sp);
        clearSP.setClickedListener(this::handleClearSuperProperties);

        Component setPvProperty = findComponentById(ResourceTable.Id_set_pv_property);
        setPvProperty.setClickedListener(this::handleSetPvProperty);

        // 子进程事件埋点
        Component go2subprocessAbility = findComponentById(ResourceTable.Id_go2subprocessAbility);
        go2subprocessAbility.setClickedListener(this::handle2SubProcessTestPage);
    }

    private void handleSignInDefault(Component component) {
        MobclickAgent.onProfileSignIn("example_id");
    }

    private void handleSignIn(Component component) {
        MobclickAgent.onProfileSignIn("example_id", "uid");
    }

    private void handleSignOut(Component component) {
        MobclickAgent.onProfileSignOff();
    }

    private void handleNormalEvent(Component component) {
        MobclickAgent.onEvent(appContext, "normalEvent");
    }

    private void handleNormalEventWithPage(Component component) {
        MobclickAgent.onEvent(appContext, "normalEvent", "bandingPageName");
    }

    private void handleKvEvent(Component component) {
        Map<String, Object> args = new HashMap();
        args.put("Product_ID", 1001);
        args.put("Product_Name", "GameBook");
        args.put("price", 23.99);
        MobclickAgent.onEventObject(appContext, "pay", args);
    }

    private void handleKvEventWithPage(Component component) {
        Map<String, Object> args = new HashMap();
        args.put("title", "film_magazine");
        args.put("author", "mike");
        String bandingPageName = "favorite";
        MobclickAgent.onEventObject(appContext, "like", args, bandingPageName);
    }

    private void handleEventMore2k(Component component) {
        for (int i = 0; i < 2100; i++) {
            MobclickAgent.onEvent(appContext, "click" + i);
            HiLog.info(MyApplication.LABEL, "----click---" + i);
        }
    }

    private void handleEventInSubThread(Component component) {
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 5; i++) {
                    MobclickAgent.onEvent(appContext, "ekvInSubThread" + i);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                    HiLog.info(MyApplication.LABEL, "ekvInSubThread" + i);
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    private void handleSetGlobalProperties(Component component) {
        Map<String, Object> globalProperties = new HashMap<>();
        globalProperties.put("gpkey1", "gpStringValue1");
        globalProperties.put("gpkey2", 3.2576);
        globalProperties.put("gpkey3", 1234);
        // 如下为非法输入属性值，会被SDK忽略
//        globalProperties.put("", "aaa");
//        globalProperties.put(null, "bbb");
//        globalProperties.put("error", null);
//        globalProperties.put("  ", " ");

        MobclickAgent.registerGlobalProperties(appContext, globalProperties);
    }

    private void handleClearGlobalProperties(Component component) {
        MobclickAgent.clearGlobalProperties(appContext);
    }

    private void handleSetSuperProperties(Component component) {
        MobclickAgent.registerSuperProperty(appContext, "umsp_1", "aaa");
        MobclickAgent.registerSuperProperty(appContext, "umsp_2", "bbb");
    }

    private void handleSetPvProperty(Component component) {
        Map<String, Object> args = new HashMap<>();
        args.put("PV_Property1", "value1");
        args.put("PV_Property2", 0.5);
        MobclickAgent.setPageProperty(appContext, PAGE_NAME, args);
    }

    private void handleClearSuperProperties(Component component) {
        MobclickAgent.clearSuperProperties(appContext);
    }

    private void handle2SubProcessTestPage(Component component) {
        // 跳转到子进程自定义事件测试页面
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(appContext.getBundleName())
                .withAbilityName(SubProcessAbility.class)
                .withAction(SubProcessAbility.ACTION_SUBPROCESS_SLICE)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    @Override
    public void onActive() {
        super.onActive();
        HiLog.info(MyApplication.LABEL, "--->>> AnalyticsHomeSlice::onActive.");
        MobclickAgent.onPageStart(PAGE_NAME);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.info(MyApplication.LABEL, "--->>> AnalyticsHomeSlice::onInactive.");
        MobclickAgent.onPageEnd(PAGE_NAME);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEY_BACK) {
            hook();
            return true;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    private void hook() {
        final float DIALOG_BOX_CORNER_RADIUS = 36.0f;
        final int DIALOG_BOX_WIDTH = 984;
        CommonDialog commonDialog = new CommonDialog(this);
//        commonDialog.setTitleText("提示");
//        commonDialog.setContentText("检测到Back键被点击，你打算");
        commonDialog.setCornerRadius(DIALOG_BOX_CORNER_RADIUS);
        commonDialog.setAlignment(TextAlignment.CENTER);
        commonDialog.setSize(DIALOG_BOX_WIDTH, MATCH_CONTENT);
        commonDialog.setAutoClosable(true);
        commonDialog.setButton(IDialog.BUTTON1, "返回", (iDialog, var) -> {
            iDialog.destroy();
            Ability parent = this.getAbility();
            parent.terminateAbility();
        });
        commonDialog.setButton(IDialog.BUTTON2, "点错了", (iDialog, var) -> {
            iDialog.destroy();
        });
        commonDialog.setButton(IDialog.BUTTON3, "退出应用", (iDialog, var) -> {
            iDialog.destroy();
            MobclickAgent.onKillProcess(appContext);
            ProcessManager.kill(ProcessManager.getPid());
        });
        commonDialog.show();
    }
}
