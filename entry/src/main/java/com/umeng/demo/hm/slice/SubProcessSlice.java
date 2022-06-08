package com.umeng.demo.hm.slice;

import com.umeng.analytics.MobclickAgent;
import com.umeng.demo.hm.MyApplication;
import com.umeng.demo.hm.ResourceTable;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.os.ProcessManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubProcessSlice extends AbilitySlice {

    private Context appContext = null;
    private static final String PAGE_NAME = "SubProcessSlice";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_sub_process);
        appContext = this.getApplicationContext();
        initComponents();
    }

    private void initComponents() {
        Component oneEvent = findComponentById(ResourceTable.Id_one_event);
        oneEvent.setClickedListener(this::handleOneEvent);
        Component eventInThread = findComponentById(ResourceTable.Id_event_in_thread);
        eventInThread.setClickedListener(this::handleEventInThread);
        Component killCurrentProcess = findComponentById(ResourceTable.Id_kill_myself);
        killCurrentProcess.setClickedListener(this::handleKillCurrentProcess);
    }

    private void handleOneEvent(Component component) {
        MobclickAgent.onEvent(appContext, "ekv_in_sub_precess");
    }

    private void handleEventInThread(Component component) {
        startNewThread("#1");
        startNewThread("#2");
    }

    private void handleKillCurrentProcess(Component component) {
        Ability parentAbility = this.getAbility();
        this.terminate();
        parentAbility.terminateAbility();
        ProcessManager.kill(ProcessManager.getPid());
    }

    private void startNewThread(String threadName){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(date);
                MobclickAgent.onEvent(appContext, threadName + ":"+ dateString);
            }
        });
        thread.setName(threadName);
        thread.start();
    }

    @Override
    public void onActive() {
        super.onActive();
        HiLog.info(MyApplication.LABEL, "--->>> SubProcessSlice::onActive.");
        MobclickAgent.onPageStart(PAGE_NAME);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        HiLog.info(MyApplication.LABEL, "--->>> SubProcessSlice::onInactive.");
        MobclickAgent.onPageEnd(PAGE_NAME);
    }

}
