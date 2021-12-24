package com.kmw.soom2.Common;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

//@GlideModule
public class YourGlideModule extends AppGlideModule
{
    Context context;
    public YourGlideModule(Context context) {
        this.context = context;
    }

    @Override
    public void applyOptions(Context _context, GlideBuilder _builder )
    {
        // 메모리 캐시
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).setMemoryCacheScreens(2).build();
        _builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        // 비트맵 풀
        int bitmapPoolSizeBytes = 1024 * 1024 * 30; // 30mb
        _builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));

        // 디스크 캐시
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        _builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "cacheFolderName", diskCacheSizeBytes));

        // 리퀘스트 옵션 기본 값
        _builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig());

        // 로그 레벨
        _builder.setLogLevel(Log.DEBUG);
    }
}
