package com.umeng.demo.hm.slice;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.jsbridge.BridgeAgent;
import com.umeng.demo.hm.MyApplication;
import com.umeng.demo.hm.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.webengine.*;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.utils.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class WebViewSlice extends AbilitySlice {

    private Context appContext = null;
    private static final String PAGE_NAME = "WebViewSlice";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_webview);
        appContext = this.getApplicationContext();
        WebView webview = (WebView) findComponentById(ResourceTable.Id_webview);
        WebConfig webConfig = webview.getWebConfig();
        webview.setWebAgent(new WebAgent() {
            @Override
            public ResourceResponse processResourceRequest(WebView webview, ResourceRequest request) {
                final String authority = "example.com";
                final String rawFile = "/rawfile/";
                final String local = "/local/";
                Uri requestUri = request.getRequestUrl();
                if (authority.equals(requestUri.getDecodedAuthority())) {
                    String path = requestUri.getDecodedPath();
                    if (TextTool.isNullOrEmpty(path)) {
                        return super.processResourceRequest(webview, request);
                    }
                    if (path.startsWith(rawFile)) {
                        // 根据自定义规则访问资源文件
                        String rawFilePath = "entry/resources/rawfile/" + path.replace(rawFile, "");
                        String mimeType = URLConnection.guessContentTypeFromName(rawFilePath);
                        try {
                            Resource resource = getResourceManager().getRawFileEntry(rawFilePath).openRawFile();
                            ResourceResponse response = new ResourceResponse(mimeType, resource, null);
                            return response;
                        } catch (IOException e) {
                            HiLog.info(MyApplication.LABEL, "open raw file failed");
                        }
                    }
                    if (path.startsWith(local)) {
                        // 根据自定义规则访问本地文件
                        String localFile = getContext().getFilesDir() + path.replace(local, "/");
                        HiLog.info(MyApplication.LABEL, "open local file " + localFile);
                        File file = new File(localFile);
                        if (!file.exists()) {
                            HiLog.info(MyApplication.LABEL, "file not exists");
                            return super.processResourceRequest(webview, request);
                        }
                        String mimeType = URLConnection.guessContentTypeFromName(localFile);
                        try {
                            InputStream inputStream = new FileInputStream(file);
                            ResourceResponse response = new ResourceResponse(mimeType, inputStream, null);
                            return response;
                        } catch (IOException e) {
                            HiLog.info(MyApplication.LABEL, "open local file failed");
                        }
                    }
                }
                return super.processResourceRequest(webview, request);
            }
        });

        // 配置是否支持访问DataAbility资源，默认为true
        webConfig.setDataAbilityPermit(true);

        // 绑定WebView控件到，此WebView内访问H5页面中的aplus_cloud.js SDK可以和
        // JS SDK配合，实现H5页面埋点统计数据统一通过Native统计SDK发送。
        MobclickAgent.attach(webview);
        webview.load("https://example.com/rawfile/index.html");
    }

    @Override
    protected void onActive() {
        super.onActive();
        MobclickAgent.onPageStart(PAGE_NAME);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
}
