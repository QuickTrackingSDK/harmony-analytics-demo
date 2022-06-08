package com.umeng.demo.hm.ability;

import ohos.aafwk.ability.Ability;
import ohos.global.resource.RawFileDescriptor;
import ohos.utils.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataAbility extends Ability {


    @Override
    public RawFileDescriptor openRawFile(Uri uri, String mode) throws FileNotFoundException {
        if (uri == null) {;
            return super.openRawFile(uri, mode);
        }
        String path = uri.getEncodedPath();
        int splitIndex = path.indexOf('/', 1);
        String providerName = Uri.decode(path.substring(1, splitIndex));
        String rawFilePath = Uri.decode(path.substring(splitIndex + 1));
        RawFileDescriptor rawFileDescriptor = null;
        try {
            rawFileDescriptor = getResourceManager().getRawFileEntry(rawFilePath).openRawFileDescriptor();
        } catch (IOException e) {
            // 异常处理
        }
        return rawFileDescriptor;
    }
}
