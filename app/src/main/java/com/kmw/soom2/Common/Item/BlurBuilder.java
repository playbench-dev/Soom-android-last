package com.kmw.soom2.Common.Item;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.5f;
    public static Bitmap blur(Context context, Bitmap image, float radius) {
//        int width = Math.round(image.getWidth() * BITMAP_SCALE);
//        int height = Math.round(image.getHeight() * BITMAP_SCALE);
//
//        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
//        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
//
//
//        RenderScript rs = RenderScript.create(context);
//        ScriptIntrinsicBlur theIntrinsic = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//            theIntrinsic.setRadius(radius + 1);
//            theIntrinsic.setInput(tmpIn);
//            theIntrinsic.forEach(tmpOut);
//            tmpOut.copyTo(outputBitmap);
//        }
//        return outputBitmap;

        if (radius <= 0) {
            radius = 0.1f;
        } else if (radius > 25) {
            radius = 25.0f;
        }

        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, image);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }
}
