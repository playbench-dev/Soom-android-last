package com.kmw.soom2.Common;

import com.bumptech.glide.module.AppGlideModule;

public class MyGlideApp extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
