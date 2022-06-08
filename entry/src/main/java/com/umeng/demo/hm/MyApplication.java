package com.umeng.demo.hm;
import com.umeng.analytics.MobclickAgent;
import com.umeng.demo.hm.utils.Const;
import ohos.aafwk.ability.AbilityPackage;
import ohos.app.Context;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import com.umeng.commonsdk.UMConfigure;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyApplication extends AbilityPackage {
    public static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00001, "hm_analytics");
    private static final String dk = "44f4da88-f684-455e-bead-713ab618fdf7";
    @Override
    public void onInitialize() {
        super.onInitialize();
        HiLog.info(LABEL, "--->>> app onInitialize.");

        UMConfigure.setCustomDomain(Const.PRIMARY_URL, Const.SECOND_URL);
        UMConfigure.setLogEnabled(true);
        UMConfigure.setProcessEvent(true);
        Context appContext = this.getApplicationContext();
        UMConfigure.preInit(this);
        MobclickAgent.disableAbilityPageCollection();
        UMConfigure.setDebugKey(appContext, dk);

        // 禁止采集android_id,oaid,DVID,networkId
        //UMConfigure.ignoreCollectIdFlag(UMConfigure.FLAG_ANDROID_ID | UMConfigure.FLAG_OAID | UMConfigure.FLAG_DVID | UMConfigure.FLAG_NETWORK_ID);
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                HiLog.info(LABEL, "--->>> 调用延迟初始化接口：UMConfigure.init() ");
                UMConfigure.init(appContext, Const.APPKEY, Const.CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);
            }
        }, 5, TimeUnit.SECONDS);
    }
}
