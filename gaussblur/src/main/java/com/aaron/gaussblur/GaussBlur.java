package com.aaron.gaussblur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;

import java.io.InputStream;

/**
 * @date 2021/01/26
 * @description 使用RenderScript方式对图片进行高斯模糊
 */
public class GaussBlur {
    public static final String TAG = "GaussBlur";
    private Context mContext;
    private float mBlurRadius = 0f;
    private float mScale = 1.0f;
    private Bitmap mSource;
    private static volatile GaussBlur sInstance = null;


    private GaussBlur(Context context) {
        mContext = context.getApplicationContext();
    }

    public static GaussBlur with(Context context) {
        if (sInstance == null) {
            synchronized (GaussBlur.class) {
                if (sInstance == null) {
                    sInstance = new GaussBlur(context);
                }
            }
        }
        return sInstance;
    }

    public GaussBlur blurRadius(@FloatRange(from = 0.0f,fromInclusive = false, to = 25.0f) float mBlurRadius) {
        this.mBlurRadius = mBlurRadius;
        return this;
    }

    public GaussBlur scale(@ScaleValue float mScale) {
        this.mScale = mScale;
        return this;
    }

    public GaussBlur bitmap(Bitmap mSource) {
        this.mSource = mSource;
        return this;
    }

    public GaussBlur bitmap(@DrawableRes int resId){
        this.mSource = BitmapFactory.decodeResource(mContext.getResources(), resId);
        return this;
    }

    public GaussBlur bitmap(InputStream inputStream){
        this.mSource = BitmapFactory.decodeStream(inputStream);
        return this;
    }

    public GaussBlur bitmap(String fileName){
        this.mSource = BitmapFactory.decodeFile(fileName);
        return this;
    }

    /**
     * @throws NullPointerException if bitmap is null
     * @throws IllegalArgumentException if blur radius is out of range (0.0, 25.0]
     * @return
     */
    public Bitmap blur() {
        if (mSource == null) {
            throw new NullPointerException("Bitmap cannot be null");
        }

        if (mBlurRadius <= 0f || mBlurRadius > 25.0f){
            throw new IllegalArgumentException("blur radius is out of range");
        }

        int scaledWidth = (int) (mSource.getWidth() * mScale);
        int scaledHeight = (int) (mSource.getHeight() * mScale);
        Log.d(TAG, String.format("scaledWidth = %s, scaledHeight = %s", scaledWidth, scaledHeight));

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mSource, scaledWidth, scaledHeight, false);
        RenderScript renderScript = RenderScript.create(mContext);

        Allocation input = Allocation.createFromBitmap(renderScript, scaledBitmap);
        Allocation output = Allocation.createTyped(renderScript, input.getType());

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        intrinsicBlur.setInput(input);
        intrinsicBlur.setRadius(mBlurRadius);
        intrinsicBlur.forEach(output);
        output.copyTo(scaledBitmap);
        renderScript.destroy();
        return Bitmap.createScaledBitmap(scaledBitmap, mSource.getWidth(), mSource.getHeight(), false);
    }
}
